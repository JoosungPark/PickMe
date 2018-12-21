package com.joosung.pickme.common

import com.joosung.pickme.BuildConfig


class AppConfig(private val setting: Setting) {
    enum class Setting {
        Toy
        ;


        fun getAuthorization(): String =
            when (this) {
                Toy -> BuildConfig.Authorization
            }

        fun getServerUrl(): String =
                when (this) {
                    Toy -> BuildConfig.API_URL
                }

    }

    val authorization = setting.getAuthorization()
    val serverUrl = setting.getServerUrl()
}