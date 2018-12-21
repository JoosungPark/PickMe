package com.joosung.pickme.http.model

import android.databinding.BaseObservable
import android.databinding.ObservableField
import com.google.gson.annotations.SerializedName
import com.joosung.imagelist.util.LogUtil
import com.joosung.pickme.common.AppShared
import com.joosung.pickme.model.AppJsonObject
import com.joosung.pickme.model.AppObject
import java.util.*

typealias MediaUrl = String

interface MediaPresentable {
    fun thumbnailUrl() : MediaUrl
    var dateTime: Date
}

class SharedMedia(appShared: AppShared, createPlaceHolder: Boolean, placeHolderId: String) : AppJsonObject, MediaPresentable {
    @SerializedName("thumbnail")
    var videoThumbnail: MediaUrl? = null

    @SerializedName("thumbnail_url")
    var imageUrl: MediaUrl? = null

    @SerializedName("datetime")
    override lateinit var dateTime: Date

    @SerializedName("width")
    var width: Int? = null

    @SerializedName("height")
    var height: Int? = null

    override fun id(): String {
        return thumbnailUrl()
    }

    override fun thumbnailUrl(): MediaUrl {
        return videoThumbnail ?: imageUrl ?: ""
    }

}

class AppSharedMedia(sm: SharedMedia, override val appShared: AppShared) : AppObject<SharedMedia>, BaseObservable() {
    override val id: String = sm.id()

    val url = ObservableField<MediaUrl>()
    override val isPlaceHolder = ObservableField<Boolean>()

    val width = ObservableField<Int>()
    val height = ObservableField<Int>()
    val dateTime = ObservableField(0L)
    val starred = ObservableField(false)

    init {
        url.set(sm.thumbnailUrl())
        dateTime.set(sm.dateTime.time)

        sm.width?.also { width.set(it) }
        sm.height?.also { height.set(it) }
    }

    override fun update(data: SharedMedia) {
        isPlaceHolder.set(false)
        dateTime.set(data.dateTime.time)
        data.width?.also { width.set(it) }
        data.height?.also { height.set(it) }
    }

    companion object {
        fun createPlaceholder(id: String, appShared: AppShared): AppSharedMedia {
            val image = SharedMedia(appShared, true, id)
            return AppSharedMedia(image, appShared)
        }
    }
}