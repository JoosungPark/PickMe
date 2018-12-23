package com.joosung.pickme.http.model

import android.databinding.BaseObservable
import android.databinding.ObservableField
import com.google.gson.annotations.SerializedName
import com.joosung.pickme.common.AppShared
import com.joosung.pickme.model.AppJsonObject
import com.joosung.pickme.model.AppObject
import java.util.*

typealias MediaUrl = String

interface MediaPresentable: AppJsonObject {
    var videoThumbnail: MediaUrl?
    var imageUrl: MediaUrl?
    fun thumbnailUrl() : MediaUrl
    var dateTime: Date
    var width: Int?
    var height: Int?
    var starred: Boolean?
}

class SharedMedia() : AppJsonObject, MediaPresentable {
    @SerializedName("thumbnail")
    override var videoThumbnail: MediaUrl? = null

    @SerializedName("thumbnail_url")
    override var imageUrl: MediaUrl? = null

    @SerializedName("datetime")
    override lateinit var dateTime: Date

    @SerializedName("width")
    override var width: Int? = null

    @SerializedName("height")
    override var height: Int? = null

    override var starred: Boolean? = null

    override fun id(): String {
        return thumbnailUrl()
    }

    override fun thumbnailUrl(): MediaUrl {
        return videoThumbnail ?: imageUrl ?: ""
    }

    constructor(appShared: AppShared, createPlaceHolder: Boolean, placeHolderId: String): this() { }

    constructor(media: MediaPresentable): this() {
        this.videoThumbnail = media.videoThumbnail
        this.imageUrl = media.imageUrl
        this.dateTime = media.dateTime
        this.width = media.width
        this.height = media.height
    }
}

class AppSharedMedia(val sm: SharedMedia, override val appShared: AppShared) : AppObject<SharedMedia>, BaseObservable() {
    override val id: String = sm.id()

    val url = ObservableField<MediaUrl>()
    override val isPlaceHolder = ObservableField<Boolean>()

    val width = ObservableField<Int>()
    val height = ObservableField<Int>()
    val dateTime = ObservableField(0L)
    val starred = ObservableField<Boolean>(false)

    init {
        url.set(sm.thumbnailUrl())
        dateTime.set(sm.dateTime.time)

        sm.width?.also { width.set(it) }
        sm.height?.also { height.set(it) }
        sm.starred?.also { starred.set(it) }
    }

    override fun update(data: SharedMedia) {
        isPlaceHolder.set(false)
        dateTime.set(data.dateTime.time)
        data.width?.also { width.set(it) }
        data.height?.also { height.set(it) }
        data.starred?.also { starred.set(it) }
    }

    companion object {
        fun createPlaceholder(id: String, appShared: AppShared): AppSharedMedia {
            val image = SharedMedia(appShared, true, id)
            return AppSharedMedia(image, appShared)
        }
    }
}