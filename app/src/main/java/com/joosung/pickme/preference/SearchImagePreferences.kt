package com.joosung.pickme.preference

import android.content.Context
import com.joosung.pickme.preference.BasePreferences

class ImagePreferences(private val context: Context, val name: String) : BasePreferences(context, name) {
    companion object {
        val kImageThreshold = "kImageThreshold"
        val imageThresholdDelta = 50
        val imageThresholdMinimum = 200
        val imageThresholdDefault = 750
        val imageMinimumHeight = 200
    }
}