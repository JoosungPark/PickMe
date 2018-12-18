package com.joosung.pickme.http

open class MediaThrowable(override val message: String? = "", override val cause: Throwable? = null) : Throwable(message, cause)

class MediaJavaError(private val error: Throwable) : MediaThrowable(error.localizedMessage, error)

class MediaServerError(val errorCode: ErrorType = ErrorType.INVALID, val errorMessage: String) : MediaThrowable(errorMessage, Throwable(errorMessage))