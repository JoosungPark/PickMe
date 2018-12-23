package com.joosung.pickme.common

import com.joosung.pickme.http.model.MediaPresentable
import com.joosung.pickme.http.model.MediaUrl
import io.realm.Realm
import io.realm.RealmObject
import io.realm.annotations.*
import java.util.*

@RealmClass
open class AppRealmMedia : RealmObject(), MediaPresentable {
    @PrimaryKey
    @Required
    var id = ""

    override var videoThumbnail: MediaUrl? = null
    override var imageUrl: MediaUrl? = null
    @Required
    override var dateTime = Date()
    override var width: Int? = null
    override var height: Int? = null
    override var starred: Boolean? = null

    override fun thumbnailUrl(): MediaUrl {
        return videoThumbnail ?: imageUrl ?: ""
    }
    override fun id(): String {
        return thumbnailUrl()
    }

    companion object {
        @Ignore const val ID = "id"
        @Ignore const val kVideoThumbnail = "videoThumbnail"
        @Ignore const val kImageUrl = "imageUrl"
        @Ignore const val kDateTime = "dateTime"
        @Ignore const val kWidth = "width"
        @Ignore const val kHeight = "height"
        @Ignore const val kStarred = "starred"

        fun update(realm: Realm, media: MediaPresentable) {
            val realmMedia =
                realm.where(AppRealmMedia::class.java).equalTo(AppRealmMedia.ID, media.thumbnailUrl()).findFirst()
                    ?: realm.createObject(AppRealmMedia::class.java, media.thumbnailUrl())

            realmMedia.videoThumbnail = media.videoThumbnail
            realmMedia.imageUrl = media.imageUrl
            realmMedia.dateTime = media.dateTime
            realmMedia.width = media.width
            realmMedia.height = media.height
            realmMedia.starred = true

            realm.insertOrUpdate(realmMedia)
        }
    }
}

@RealmModule(classes = [(AppRealmMedia::class)])
open class AppRealmModule

class RealmQueryBuilder(val realm: Realm) {
    fun queryMediaUrlList(): List<MediaUrl> {
        return realm.where(AppRealmMedia::class.java).findAll().map { it.id }
    }

    fun queryMediaList(): List<MediaPresentable>? {
        return realm.where(AppRealmMedia::class.java).findAll()
    }

    fun queryMedia(url: MediaUrl): AppRealmMedia? {
        return realm.where(AppRealmMedia::class.java).equalTo(AppRealmMedia.ID, url).findFirst()
    }

}