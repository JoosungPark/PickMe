package com.joosung.pickme.common

import com.google.gson.GsonBuilder
import com.joosung.pickme.http.AppServer
import com.joosung.pickme.http.ImageRequest
import com.joosung.pickme.http.MediaResponse
import com.joosung.pickme.http.model.*
import com.joosung.pickme.model.VarDict
import io.reactivex.Observable
import io.reactivex.observables.ConnectableObservable
import java.util.*


interface AppServerInterface {
    fun <T : MediaResponse> request(request: ImageRequest<T>): ConnectableObservable<ImageRequest<T>>
}

interface MediaRepository {
    fun observeMedia(url: MediaUrl): Observable<AppSharedMedia>
    fun getMedia(url: MediaUrl): AppSharedMedia?
    fun getDefaultCount(): Int
}

interface AppSharedInterface: AppServerInterface, MediaRepository

class AppShared(config: AppConfig) : AppSharedInterface {
    val gson = GsonBuilder().registerTypeAdapterFactory(OptionalTypeAdapter.FACTORY).registerTypeAdapter(Date::class.java, DateDeserializer()).create()!!
    val medias = VarDict { AppSharedMedia.createPlaceholder(it, this) }
    val server = AppServer(this, config)

    override fun observeMedia(url: MediaUrl): Observable<AppSharedMedia> = medias.observe(url)

    override fun getMedia(url: MediaUrl): AppSharedMedia? = medias.getOrPlaceholder(url)

    override fun <T : MediaResponse> request(request: ImageRequest<T>): ConnectableObservable<ImageRequest<T>> {
        return server.request(request)
    }

    override fun getDefaultCount(): Int {
        return 15
    }
}