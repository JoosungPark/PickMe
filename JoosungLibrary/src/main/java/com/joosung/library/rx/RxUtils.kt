package com.joosung.library.rx

import android.annotation.SuppressLint
import android.databinding.Observable.OnPropertyChangedCallback
import android.databinding.ObservableField
import android.os.Looper
import io.reactivex.*
import io.reactivex.Observable
import io.reactivex.Observable.combineLatest
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.*
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

object RxUtils {
    fun <T> toObservable(observableField: ObservableField<T>, initialData: Boolean = true): Observable<T> {
        return Flowable.create<T>({ asyncEmitter ->
            val callback = object : OnPropertyChangedCallback() {
                override fun onPropertyChanged(dataBindingObservable: android.databinding.Observable, propertyId: Int) {
                    if (dataBindingObservable === observableField) {
                        observableField.get()?.let {
                            asyncEmitter.onNext(observableField.get()!!)
                        }
                    }
                }
            }
            observableField.addOnPropertyChangedCallback(callback)
            if (initialData) {
                observableField.get()?.let {
                    asyncEmitter.onNext(observableField.get()!!)
                }
            }
            asyncEmitter.setCancellable { observableField.removeOnPropertyChangedCallback(callback) }
        }, BackpressureStrategy.LATEST).toObservable()
    }

    fun every(s1: Observable<Boolean>, s2: Observable<Boolean>): Observable<Boolean> =
            combineLatest(s1, s2, BiFunction { e1, e2 -> e1 && e2 })

    fun <T1, T2, R> combineLatest(source1: ObservableSource<out T1>,
                                  source2: ObservableSource<out T2>,
                                  resultSelector: (T1, T2) -> R): Observable<R> =
            combineLatest<T1, T2, R>(source1, source2, BiFunction { t1, t2 -> resultSelector(t1, t2) })

    fun <T1, T2, T3, R> combineLatest(source1: ObservableSource<out T1>,
                                      source2: ObservableSource<out T2>,
                                      source3: ObservableSource<out T3>,
                                      resultSelector: (T1, T2, T3) -> R): Observable<R> =
            Observable.combineLatest<T1, T2, T3, R>(source1, source2, source3, Function3 { t1, t2, t3 -> resultSelector(t1, t2, t3) })

    fun <T1, T2, T3, T4, R> combineLatest(source1: ObservableSource<out T1>,
                                          source2: ObservableSource<out T2>,
                                          source3: ObservableSource<out T3>,
                                          source4: ObservableSource<out T4>,
                                          resultSelector: (T1, T2, T3, T4) -> R): Observable<R> =
            Observable.combineLatest<T1, T2, T3, T4, R>(source1, source2, source3, source4,
                    Function4 { t1, t2, t3, t4 -> resultSelector(t1, t2, t3, t4) })

    fun <T1, T2, T3, T4, T5, R> combineLatest(source1: ObservableSource<out T1>,
                                              source2: ObservableSource<out T2>,
                                              source3: ObservableSource<out T3>,
                                              source4: ObservableSource<out T4>,
                                              source5: ObservableSource<out T5>,
                                              resultSelector: (T1, T2, T3, T4, T5) -> R): Observable<R> =
            Observable.combineLatest<T1, T2, T3, T4, T5, R>(source1, source2, source3, source4, source5,
                    Function5 { t1, t2, t3, t4, t5 -> resultSelector(t1, t2, t3, t4, t5) })

    fun <T1, T2, T3, T4, T5, T6, R> combineLatest(source1: ObservableSource<out T1>,
                                                  source2: ObservableSource<out T2>,
                                                  source3: ObservableSource<out T3>,
                                                  source4: ObservableSource<out T4>,
                                                  source5: ObservableSource<out T5>,
                                                  source6: ObservableSource<out T6>,
                                                  resultSelector: (T1, T2, T3, T4, T5, T6) -> R): Observable<R> =
            Observable.combineLatest<T1, T2, T3, T4, T5, T6, R>(source1, source2, source3, source4, source5, source6,
                    Function6 { t1, t2, t3, t4, t5, t6 -> resultSelector(t1, t2, t3, t4, t5, t6) })

    fun <T1, T2, T3, T4, T5, T6, T7, R> combineLatest(source1: ObservableSource<out T1>,
                                                      source2: ObservableSource<out T2>,
                                                      source3: ObservableSource<out T3>,
                                                      source4: ObservableSource<out T4>,
                                                      source5: ObservableSource<out T5>,
                                                      source6: ObservableSource<out T6>,
                                                      source7: ObservableSource<out T7>,
                                                      resultSelector: (T1, T2, T3, T4, T5, T6, T7) -> R): Observable<R> =
            Observable.combineLatest<T1, T2, T3, T4, T5, T6, T7, R>(source1, source2, source3, source4, source5, source6, source7,
                    Function7 { t1, t2, t3, t4, t5, t6, t7 -> resultSelector(t1, t2, t3, t4, t5, t6, t7) })

    fun <T1, T2, T3, T4, T5, T6, T7, T8, R> combineLatest(source1: ObservableSource<out T1>,
                                                          source2: ObservableSource<out T2>,
                                                          source3: ObservableSource<out T3>,
                                                          source4: ObservableSource<out T4>,
                                                          source5: ObservableSource<out T5>,
                                                          source6: ObservableSource<out T6>,
                                                          source7: ObservableSource<out T7>,
                                                          source8: ObservableSource<out T8>,
                                                          resultSelector: (T1, T2, T3, T4, T5, T6, T7, T8) -> R): Observable<R> =
            Observable.combineLatest<T1, T2, T3, T4, T5, T6, T7, T8, R>(source1, source2, source3, source4, source5, source6, source7, source8,
                    Function8 { t1, t2, t3, t4, t5, t6, t7, t8 -> resultSelector(t1, t2, t3, t4, t5, t6, t7, t8) })

    fun <T1, T2, R> zip(source1: ObservableSource<T1>, source2: ObservableSource<T2>, resultSelector: (T1, T2) -> R): Observable<R> =
            Observable.zip<T1, T2, R>(source1, source2, BiFunction(resultSelector))
}

fun <T> Observable<T>.debug(name: String = "", obj: Any? = null): Observable<T> {
    return this.doOnEach {
        if (obj == null) {
            println(" [$name] : $it")
        } else {
            println(" [${obj.javaClass.name}::$name] : $it")
        }
    }
}

@SuppressLint("CheckResult")
fun delay(delayInMs: Long = 0, block: Boolean = false, runner: (() -> Unit)) {
    val isMainThread = Looper.myLooper() == Looper.getMainLooper()
    if (isMainThread || !block) {
        Observable.just(0)
                .delay(delayInMs, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .take(1)
                .subscribe { runner() }
    } else {
        val latch = CountDownLatch(1)
        Observable.just(0)
                .delay(delayInMs, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .take(1)
                .subscribe {
                    runner()
                    latch.countDown()
                }
        latch.await()
    }
}

fun <T> ObservableField<T>.asObservable(initialData: Boolean = true): Observable<T> =
    RxUtils.toObservable(this, initialData)

fun <T> Observable<T>.bindTo(field: ObservableField<T>): Disposable = this.subscribe { field.set(it) }

fun <T> Observable<Nullable<T>>.filterNotNull(): Observable<T> {
    @Suppress("UNCHECKED_CAST")
    return this.filter { it.value != null }.map { it.value!! }
}

fun <T> Flowable<Nullable<T>>.filterNotNull(): Flowable<T> {
    @Suppress("UNCHECKED_CAST")
    return this.filter { it.value != null }.map { it.value!! }
}

fun <T, R> Observable<T>.mapNotNull(mapper: (T) -> R?): Observable<R> =
        this.map { Nullable(mapper(it)) }.filterNotNull()

fun <T, R> Observable<Nullable<T>>.mapNullableToValue(mapper: (T?) -> R): Observable<R> = this.map { mapper(it.value) }

fun <T, R> Flowable<T>.mapNullableToValue(mapper: (T?) -> R) = this.map { Nullable(mapper(it)) }.filterNotNull()

fun <T, R> Observable<Nullable<T>>.mapNullableToNullable(mapper: (T?) -> R?): Observable<Nullable<R>> =
        this.map { Nullable.from(mapper(it.value)) }

fun <I> Observable<Nullable<out Collection<I>>>.firstOrNull(): Observable<Nullable<out I>> {
    return this.map {
        if (it.value == null) {
            Nullable(null)
        } else {
            Nullable(it.value!!.firstOrNull())
        }
    }
}
