package com.joosung.pickme.preference

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

abstract class BasePreferences(private val context: Context, private val name: String) {
    lateinit var preferences: SharedPreferences
    
    private fun initialize() {
        preferences = if (name.isEmpty()) PreferenceManager.getDefaultSharedPreferences(context)
        else context.getSharedPreferences(name, Context.MODE_PRIVATE)
    }
    
    inline fun <reified T> updateValue(key: String, value: T?) {
        synchronized(preferences) {
            val editor = preferences.edit()
            value?.let { 
                when (it) {
                    is String -> editor.putString(key, it)
                    is Int -> editor.putInt(key, it)
                    is Boolean -> editor.putBoolean(key, it)
                    else -> throw RuntimeException("Not implemented for given type  ${T::class.java.name} yet. (updateValue)")
                }
            } ?: editor.remove(key)
            
            editor.apply()
        }
    }

    inline fun <reified T : Any> getValue(key: String, defaultValue: T? = null): T? {
        synchronized(preferences) {
            if (!preferences.contains(key)) return defaultValue

            @Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN")
            return when (T::class.java) {
                String::class.java -> preferences.getString(key, "") as T
                java.lang.Integer::class.java -> preferences.getInt(key, 0) as T
                java.lang.Boolean::class.java -> preferences.getBoolean(key, false) as T
                else -> {
                    throw RuntimeException("Not implemented for given type yet. (getValue), ${T::class.java.name}")
                }
            }
        }
    }

    protected fun setValue(key: String, value: String): Unit {
        synchronized(preferences) {
            val editor = preferences.edit()
            editor.putString(key, value)
            editor.apply()
        }
    }

    protected fun getValue(key: String, defaultValue: String): String {
        var result = defaultValue

        synchronized(preferences) {
            result = preferences.getString(key, defaultValue)
        }

        return result
    }

    protected fun getValue(key: String, defaultValue: Boolean): Boolean {
        var result = defaultValue

        synchronized(preferences) {
            result = preferences.getBoolean(key, defaultValue)
        }

        return result
    }

    protected fun getValue(key: String, defaultValue: Int): Int {
        var result = defaultValue

        synchronized(preferences) {
            result = preferences.getInt(key, defaultValue)
        }

        return result
    }

    protected fun getValue(key: String, defaultValue: Long): Long {
        var result = defaultValue

        synchronized(preferences) {
            result = preferences.getLong(key, defaultValue)
        }

        return result
    }

    protected fun getValue(key: String, defaultValue: Float): Float {
        var result = defaultValue

        synchronized(preferences) {
            result = preferences.getFloat(key, defaultValue)
        }

        return result
    }

    protected fun getValue(key: String, defaultValue: MutableSet<String>): MutableSet<String> {
        var result = defaultValue

        synchronized(preferences) {
            result = preferences.getStringSet(key, defaultValue)
        }

        return result
    }

    protected open fun clean() {
        preferences.edit().clear().apply()
    }

    init {
        initialize()
    }
}