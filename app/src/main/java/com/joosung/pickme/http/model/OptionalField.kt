package com.joosung.pickme.http.model

import androidx.databinding.ObservableField
import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.TypeAdapterFactory
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import java.lang.reflect.ParameterizedType

/**
 * 서버에서 전달받는 필드를 그냥 E? 형태로 처리할 경우, 해당 필드가 서버로부터 전달되지 않은 것인지, 아니면 명시적으로 null 이 전달된 것인지
 * 파악할 수가 없다. 이를 해결하기 위하여 OptionalField 클래스와 이를 encode/decode 하기 위한 OptionalTypeAdapter 를 구현하였다.
 *
 * 예를 들어 아래와 같이 정의된 필드가 있다고 가정하자.
 *
 * @SerializedName("image")
 * var image: OptionalField<String>? = null
 *
 * 만약 image == null 이라면 필드가 아예 존재하지 않는 것이고, image.value == null 인 경우에는 명시적으로 null 값이 전달된 것이다.
 *
 * 참고 링크 : http://stackoverflow.com/a/33753539/388351
 */
class OptionalField<out E> constructor(val value: E?)

open class OptionalTypeAdapter<E> constructor(val adapter: TypeAdapter<E>) : TypeAdapter<OptionalField<E>>() {
    override fun read(`in`: JsonReader): OptionalField<E> {
        val peek = `in`.peek()
        if (peek != JsonToken.NULL) {
            return OptionalField(adapter.read(`in`))
        }
        `in`.nextNull()
        return OptionalField(null)
    }

    override fun write(out: JsonWriter, value: OptionalField<E>?) {
        if (value?.value != null) {
            adapter.write(out, value.value)
        } else {
            out.nullValue()
        }
    }

    companion object {
        @Suppress("UNCHECKED_CAST")
        val FACTORY = object : TypeAdapterFactory {
            override fun <T : Any> create(gson: Gson, type: TypeToken<T>): TypeAdapter<T>? {
                val rawType = type.rawType
                if (rawType != OptionalField::class.java) {
                    return null
                }

                val parameterizedType: ParameterizedType = (type.type as ParameterizedType)
                val actualType = parameterizedType.actualTypeArguments[0]
                val adapter: TypeAdapter<*> = gson.getAdapter(TypeToken.get(actualType))
                return OptionalTypeAdapter(adapter) as TypeAdapter<T>
            }
        }
    }
}

/**
 * OptionalField 의 값을 ObservableField 로 매핑하기 위한 extension.
 */
fun <T> ObservableField<T?>.set(field: OptionalField<T>?) {
    field?.let { this.set(field.value) }
}

fun <T> ObservableField<T>.setIfNotNull(field: OptionalField<T>?) {
    field?.let {
        it.value?.let {
            this.set(it)
        }
    }
}
