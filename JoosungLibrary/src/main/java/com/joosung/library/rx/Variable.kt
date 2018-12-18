package com.joosung.library.rx

import android.databinding.ObservableField
import io.reactivex.Observable
import io.reactivex.functions.Consumer
import io.reactivex.subjects.BehaviorSubject

/**
 * From RxJava2, BehaviorSubject no longer accept null as emittable value.
 * So added simple Nullable class to represent nullable value.
 */
class Nullable<T>(_value: T?) {

    var value = _value

    fun <R> map(mapper: (T?) -> R?): Nullable<R> {
        return Nullable(mapper(value))
    }

    companion object {
        fun <T> from(value: T?): Nullable<T> {
            return Nullable(value)
        }
    }
}

// https://gist.github.com/operando/2add7bbad535bc30f7340bf8c04660d7
class Variable<T>(private var _value: T) : ObservableField<T>(), Consumer<T> {

    private val serializedSubject = BehaviorSubject.createDefault(_value)

    override fun get(): T {
        return _value
    }

    override fun set(value: T) {
        this._value = value
        super.notifyChange()
        serializedSubject.onNext(this._value)
    }

    var value: T
        @Synchronized get() {
            return this._value
        }
        @Synchronized set(value) {
            this._value = value
            super.notifyChange()
            serializedSubject.onNext(this._value)
        }

    fun asObservable(): Observable<T> {
        return serializedSubject
    }

    fun rx(): (T) -> Unit {
        return fun(value: T) {
            this.set(value)
        }
    }

    override fun notifyChange() {
        super.notifyChange()
        serializedSubject.onNext(this._value)
    }

    override fun accept(t: T) {
        this.set(t)
    }

    fun onComplete() {
        serializedSubject.onComplete()
    }
}


class MaybeVariable<T>(_value: T? = null) : ObservableField<T>() {

    private var nullableValue = Nullable(_value)

    private val serializedSubject = BehaviorSubject.createDefault(nullableValue)

    override fun get(): T? {
        return nullableValue.value
    }

    override fun set(value: T?) {
        this.nullableValue = Nullable(value)
        super.notifyChange()
        serializedSubject.onNext(nullableValue)
    }

    var value: T?
        @Synchronized get() {
            return this.nullableValue.value
        }
        @Synchronized set(value) {
            this.nullableValue = Nullable(value)
            super.notifyChange()
            serializedSubject.onNext(this.nullableValue)
        }

    fun asObservable(): Observable<Nullable<T>> {
        return serializedSubject
    }

    fun rx(): (T) -> Unit {
        return fun(value: T) {
            this.set(value)
        }
    }

    fun rxNullable(): (Nullable<T>) -> Unit {
        return fun(value: Nullable<T>) {
            this.set(value.value)
        }
    }

    override fun notifyChange() {
        super.notifyChange()
        serializedSubject.onNext(nullableValue)
    }
}
