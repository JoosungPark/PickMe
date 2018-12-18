package com.joosung.pickme.http.api

import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import com.joosung.pickme.common.AppShared
import com.joosung.pickme.http.AppCommonRequest
import com.joosung.pickme.http.CommonMediaResponse
import com.joosung.pickme.http.HTTPMethod
import com.joosung.pickme.http.model.SharedMedia
import java.lang.reflect.Type
import java.net.URLEncoder

typealias ImageId = String

class GetImageRequest(query: String, page: Int = 1, size: Int = 15) : AppCommonRequest<GetImageResponse>() {
    override val responseType: Type get() = object : TypeToken<GetImageResponse>() {}.type
    override val method: HTTPMethod get() = HTTPMethod.get
    override var url: String = "/v2/search/image?query=${URLEncoder.encode(query, "utf-8")}&page=$page&size=$size"
    override val uniqueToken: String? get() = "${GetImageRequest::class.java.simpleName}_${url.hashCode()}"

    override fun processResult(shared: AppShared, data: GetImageResponse) {
        data.processResult(shared)
    }
}

class GetImageResponse : CommonMediaResponse() {
    @SerializedName("documents")
    var medias: ArrayList<SharedMedia>? = null

    override fun processResult(shared: AppShared) {
        super.processResult(shared)

        medias?.apply {
            shared.medias.update(this, shared)
        }
    }
}
