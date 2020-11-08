package com.joosung.rxrecycleradapter

interface RxRecyclerAdapterData {
    val id: Long get() = this::class.simpleName.hashCode().toLong()
}


