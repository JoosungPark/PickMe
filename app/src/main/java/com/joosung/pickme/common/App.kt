package com.joosung.pickme.common

import android.app.Application
import android.content.Context
import com.joosung.pickme.model.Persist
import com.joosung.pickme.preference.ImagePreferences
import com.joosung.imagelist.util.Tag
import com.joosung.pickme.BuildConfig
import com.joosung.pickme.di.pickMeApp
import org.koin.android.ext.android.startKoin
import org.koin.android.logger.AndroidLogger

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin(this, pickMeApp, logger = AndroidLogger(showDebug =  Tag.DEBUG.getValue()))
    }

    companion object {
        fun getPersist(context: Context) = Persist(ImagePreferences(context, BuildConfig.APPLICATION_ID))
    }
}