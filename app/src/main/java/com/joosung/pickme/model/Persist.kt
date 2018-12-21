package com.joosung.pickme.model

import com.joosung.pickme.preference.BasePreferences
import com.joosung.pickme.preference.ImagePreferences

class Persist(val preferences: BasePreferences? = null) {
    enum class Key(val key: String) {
        ImageThreshold(ImagePreferences.kImageThreshold)
    }

    val internal = hashMapOf<Key, Any?>()

    init {
        initialize()
    }

    private fun initialize() {
        preferences?.let {
            internal[Key.ImageThreshold] = preferences.getValue(Key.ImageThreshold.key, ImagePreferences.imageThresholdDefault)
        } ?: Key.values().forEach { internal[it] = null }
    }

    inline fun <reified T> write(key: Key, value: T?) {
        preferences?.let {
            when (key) {
                Key.ImageThreshold -> it.updateValue(key.key, value as Int?)
//                else -> throw IllegalArgumentException("preference key and value type does not match.")
            }
        }

        value?.let { internal[key] = it }
        ?: internal.remove(key)
    }

    inline fun <reified T> read(key: Key): T? = internal[key] as T?
}