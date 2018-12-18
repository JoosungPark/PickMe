package com.joosung.rxrecycleradapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.reactivex.functions.Consumer

class RxRecyclerAdapter<D : RxRecyclerAdapterData> constructor(
        private val delegate: Delegate<D>
) : RecyclerView.Adapter<RxRecyclerAdapterViewHolder<D>>(), Consumer<RxRecyclerAdapterChangeEvent<D>> {

    private var mDataSet: MutableList<D> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RxRecyclerAdapterViewHolder<D> = delegate.viewHolderForViewType(parent, viewType)

    override fun onBindViewHolder(holderRecycler: RxRecyclerAdapterViewHolder<D>, position: Int) {
        val item = mDataSet[position]
        holderRecycler.onBindItem(item, position)
    }

    override fun getItemCount(): Int = mDataSet.count()

    override fun getItemViewType(position: Int): Int = delegate.getItemViewType(position, mDataSet[position])

    override fun accept(eventRecycler: RxRecyclerAdapterChangeEvent<D>) {
        when (eventRecycler) {
            is RxRecyclerAdapterChangeEvent.Reloaded -> {
                mDataSet.clear()
                mDataSet.addAll(eventRecycler.newList)
                notifyDataSetChanged()
            }
            is RxRecyclerAdapterChangeEvent.Removed -> {
                mDataSet.removeAt(eventRecycler.index)
                notifyItemRemoved(eventRecycler.index)
            }
            is RxRecyclerAdapterChangeEvent.RemovedRange -> {
                for (i in (eventRecycler.itemCount - 1) downTo 0) {
                    mDataSet.removeAt(i + eventRecycler.startIndex)
                }
                notifyItemRangeRemoved(eventRecycler.startIndex, eventRecycler.itemCount)
            }
            is RxRecyclerAdapterChangeEvent.Inserted -> {
                mDataSet.add(eventRecycler.index, eventRecycler.item)
                notifyItemInserted(eventRecycler.index)
            }
            is RxRecyclerAdapterChangeEvent.InsertedRange -> {
                mDataSet.addAll(eventRecycler.index, eventRecycler.items)
                notifyItemRangeInserted(eventRecycler.index, eventRecycler.items.count())
            }
            is RxRecyclerAdapterChangeEvent.Changed -> {
                mDataSet[eventRecycler.index] = eventRecycler.item
                notifyItemChanged(eventRecycler.index)
            }
            is RxRecyclerAdapterChangeEvent.ChangedRange -> {
                for (i in 0 until eventRecycler.items.count()) {
                    mDataSet[eventRecycler.index + i] = eventRecycler.items[i]
                }
                notifyItemRangeChanged(eventRecycler.index, eventRecycler.items.count())
            }
        }
    }

    // FIXME: 임시
    interface Delegate<D : RxRecyclerAdapterData> {
        fun inflate(parent: ViewGroup, layoutResId: Int): View = LayoutInflater.from(parent.context).inflate(layoutResId, parent, false)
        fun getItemViewType(position: Int, item: D): Int
        fun viewHolderForViewType(parent: ViewGroup, viewType: Int): RxRecyclerAdapterViewHolder<D>
    }
}
