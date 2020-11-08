package com.joosung.rxrecycleradapter

import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import android.view.View
import androidx.recyclerview.widget.RecyclerView
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
