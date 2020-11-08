package com.joosung.pickme.common

import android.os.Handler
import android.os.HandlerThread
import com.google.gson.GsonBuilder
import com.joosung.pickme.http.AppServer
import com.joosung.pickme.http.ImageRequest
import com.joosung.pickme.http.MediaResponse
import com.joosung.pickme.http.model.*
import com.joosung.pickme.model.VarDict
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.observables.ConnectableObservable
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.annotations.RealmModule
import java.util.*
import kotlin.collections.ArrayList


interface AppServerInterface {
    fun <T : MediaResponse> request(request: ImageRequest<T>): ConnectableObservable<ImageRequest<T>>
}

interface MediaRepository {
    fun observeMedia(url: MediaUrl): Observable<AppSharedMedia>
    fun getMedia(url: MediaUrl): AppSharedMedia?
    fun getDefaultCount(): Int
}

interface RealmRepository {
    val realm: Realm
    fun <T> queryRealm(runner: (RealmQueryBuilder) -> T): Single<T>
}

interface AppSharedInterface : AppServerInterface, MediaRepository, RealmRepository

class AppShared(config: AppConfig) : AppSharedInterface {
    val gson = GsonBuilder().registerTypeAdapterFactory(OptionalTypeAdapter.FACTORY).registerTypeAdapter(
        Date::class.java,
        DateDeserializer()
    ).create()!!
    val medias = VarDict { AppSharedMedia.createPlaceholder(it, this) }
    val server = AppServer(this, config)
    private val realmThreadHandler: Handler
    private val realmThread: HandlerThread = HandlerThread("RealmHandlerThread_media")

    val realmConfig: RealmConfiguration
    override val realm: Realm
        get() {
            return Realm.getInstance(realmConfig)
        }

    init {
        realmThread.start()
        realmThreadHandler = Handler(realmThread.looper)

        val builder = RealmConfiguration.Builder()
            .name(config.realmDBName)
            .modules(AppRealmModule())
            .schemaVersion(config.realmSchemeVersion)
            .deleteRealmIfMigrationNeeded()
            .migration(AppRealmMigration())

        if (config.realmDeleteOnMigrate) {
            builder.deleteRealmIfMigrationNeeded()
        }

        realmConfig = builder.build()

        RealmQueryBuilder(realm).queryMediaList()
            ?.map { SharedMedia(it) }
            ?.toMutableList()
            ?.also { medias.update(it, this@AppShared) }
    }

    override fun observeMedia(url: MediaUrl): Observable<AppSharedMedia> = medias.observe(url)

    override fun getMedia(url: MediaUrl): AppSharedMedia? = medias.getOrPlaceholder(url)

    override fun <T : MediaResponse> request(request: ImageRequest<T>): ConnectableObservable<ImageRequest<T>> {
        return server.request(request)
    }

    override fun getDefaultCount(): Int {
        return 15
    }

    override fun <T> queryRealm(runner: (RealmQueryBuilder) -> T): Single<T> {
        return Single.create { sub ->
            realmThreadHandler.post {
                val db = this.realm
                try {
                    sub.onSuccess(runner(RealmQueryBuilder(db)))
                } catch (ex: Exception) {
                    ex.printStackTrace()
                    sub.onError(ex)
                } finally {
                    db.close()
                }
            }
        }
    }
}