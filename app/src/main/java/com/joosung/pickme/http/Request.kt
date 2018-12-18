package com.joosung.pickme.http

import com.google.gson.reflect.TypeToken
import com.joosung.pickme.common.AppShared
import com.joosung.library.rx.MaybeVariable
import com.joosung.library.rx.Variable
import java.lang.reflect.Type

enum class HTTPMethod {
    get,
    post,
    ;
}

sealed class RequestState {
    companion object {
        val Initializing = RequestInitializing
        val Ready = RequestReady
    }
}

object RequestInitializing : RequestState()
object RequestReady : RequestState()
data class RequestError(val error: Throwable) : RequestState()

interface ImageRequest<ResponseType: MediaResponse> {
    val responseType: Type get() = object : TypeToken<ResponseType>() {}.type
    val method: HTTPMethod
    val url: String
    val uniqueToken: String?
    val response: MaybeVariable<ResponseType>
    val isNetworking: Variable<Boolean>
    val requestState: Variable<RequestState>
    val header: HashMap<String, String>

    fun processResult(shared: AppShared, data: ResponseType)
    fun getParams(): Any?
}

abstract class AppCommonRequest<ResponseType : MediaResponse> : ImageRequest<ResponseType> {
    override val response = MaybeVariable<ResponseType>(null)
    override val isNetworking = Variable(false)
    override val requestState = Variable<RequestState>(RequestState.Ready)

    override val header = hashMapOf<String, String>()

    override fun getParams(): Any? = null
}