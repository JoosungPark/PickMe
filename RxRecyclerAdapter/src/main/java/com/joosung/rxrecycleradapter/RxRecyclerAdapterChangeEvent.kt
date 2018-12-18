package com.joosung.rxrecycleradapter

sealed class RxRecyclerAdapterChangeEvent<D : RxRecyclerAdapterData> {
    data class Reloaded<D : RxRecyclerAdapterData>(val newList: List<D>) : RxRecyclerAdapterChangeEvent<D>()
    data class Removed<D : RxRecyclerAdapterData>(val index: Int) : RxRecyclerAdapterChangeEvent<D>()
    data class RemovedRange<D : RxRecyclerAdapterData>(val startIndex: Int, val itemCount: Int) : RxRecyclerAdapterChangeEvent<D>()
    data class Inserted<D : RxRecyclerAdapterData>(val index: Int, val item: D) : RxRecyclerAdapterChangeEvent<D>()
    data class InsertedRange<D : RxRecyclerAdapterData>(val index: Int, val items: List<D>) : RxRecyclerAdapterChangeEvent<D>()
    data class Changed<D : RxRecyclerAdapterData>(val index: Int, val item: D) : RxRecyclerAdapterChangeEvent<D>()
    data class ChangedRange<D : RxRecyclerAdapterData>(val index: Int, val items: List<D>) : RxRecyclerAdapterChangeEvent<D>()
}
