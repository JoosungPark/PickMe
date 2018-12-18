package com.joosung.pickme.http

import com.google.gson.annotations.SerializedName
import com.joosung.pickme.common.AppShared

interface MediaResponse {
    val errorType: String?
    val message: String?
}

abstract class CommonMediaResponse : MediaResponse {
    protected val DEBUG_TAG = this.javaClass.simpleName

    @SerializedName("errorType")
    override val errorType: String? = null

    @SerializedName("message")
    override val message: String? = null

    open fun processResult(shared: AppShared) {
    }
}
