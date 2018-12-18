package com.joosung.pickme.http

enum class ErrorType(val type: String) {
    INVALID("invalid")
    ;

    companion object {
        fun from(type: String?): ErrorType = ErrorType.values().firstOrNull { it.type == type } ?: INVALID
    }

}