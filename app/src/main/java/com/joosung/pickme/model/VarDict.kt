package com.joosung.pickme.model

import android.databinding.ObservableField
import com.joosung.pickme.common.AppShared
import com.joosung.imagelist.util.LogUtil
import com.joosung.imagelist.util.Tag
import com.joosung.library.rx.Nullable
import com.joosung.library.rx.Variable
import com.joosung.library.rx.filterNotNull
import com.joosung.pickme.http.model.AppSharedMedia
import com.joosung.pickme.http.model.SharedMedia
import io.reactivex.Observable
import java.util.HashMap

interface AppVariable

interface AppJsonObject {
    val id: String?
}

interface AppObject<in JsonObjectType : AppJsonObject> {
    val id: String
    val isPlaceHolder: ObservableField<Boolean>
    val appShared: AppShared

    fun update(data: JsonObjectType)
}

object AppObjectFactory {
    fun convertFrom(obj: AppJsonObject, appShared: AppShared): AppObject<AppJsonObject>? {
        val ret: Any
        ret = when (obj) {
            is SharedMedia -> AppSharedMedia(obj, appShared)
            else -> {
                LogUtil.e(Tag.AppObjectCreation, "AppObjectFactory.convertFrom()", obj)
                throw RuntimeException("AppObjectFactory not implemented!")
            }
        }

        @Suppress("UNCHECKED_CAST")
        return ret as AppObject<AppJsonObject>
    }
}

class VarDict<in JsonObjectType: AppJsonObject, V: AppObject<JsonObjectType>>(val placeHolderFactory: (String) -> V) :
    AppVariable {
    private val _data = Variable<HashMap<String, V>>(hashMapOf())

    fun observe(key: String): Observable<V> {
        return _data.asObservable()
            .map { Nullable(it[key]) }
            .filterNotNull()
            .take(1)
    }

    fun update(dict: Map<String, JsonObjectType>, appShared: AppShared) {
        update(dict.values, appShared)
    }

    fun update(element: JsonObjectType, appShared: AppShared) {
        val id = element.id
        if (id != null) {
            update(mapOf(Pair(id, element)), appShared)
        }
    }

    fun update(arr: Iterable<JsonObjectType>, appShared: AppShared) {
        for (d in arr) {
            val id = d.id
            if (id != null) {
                @Suppress("UNCHECKED_CAST")
                val v = _data.get()[id]
                if (v != null) {
                    LogUtil.i(Tag.AppObjectCreation, "Updating existing [${d.javaClass.name}] $id")
                    v.update(d)
                } else {
                    LogUtil.i(Tag.AppObjectCreation, "Creating new [${d.javaClass.name}] $id")
                    val peepObj = AppObjectFactory.convertFrom(d as AppJsonObject, appShared)
                    if (peepObj is AppObject<JsonObjectType>) {
                        @Suppress("UNCHECKED_CAST")
                        _data.get()[id] = peepObj as V
                    }
                }
            }

        }
    }

    fun update(arr: Sequence<V>) {
        for (d in arr) {
            _data.get()[d.id] = d
        }
    }

    fun get(): HashMap<String, V> {
        return _data.get()
    }

    fun getOrPlaceholder(id: String): V {
        if (this._data.get()[id] == null) {
            val placeHolder = placeHolderFactory(id)
            placeHolder.isPlaceHolder.set(true)
            this._data.get()[id] = placeHolder
            this._data.notifyChange()
        }
        return this._data.get()[id]!!
    }

    fun clear() {
        _data.value.clear()
    }
}