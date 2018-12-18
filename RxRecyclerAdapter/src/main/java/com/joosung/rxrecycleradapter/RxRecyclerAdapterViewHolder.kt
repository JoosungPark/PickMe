package com.joosung.rxrecycleradapter

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.view.View
import io.reactivex.disposables.CompositeDisposable

/**
 * Item interface that can be element of this adapter.
 */
abstract class RxRecyclerAdapterViewHolder<in D : RxRecyclerAdapterData>(itemView: View,
                                                                         private val parentDisposable: CompositeDisposable)
    : RecyclerView.ViewHolder(itemView) {

    val binding: ViewDataBinding = DataBindingUtil.bind(itemView)!!
    var dbag = CompositeDisposable()

    open fun onBindItem(item: D, position: Int) {
        dbag.dispose()
        dbag = CompositeDisposable()
        parentDisposable.add(dbag)
    }
}
