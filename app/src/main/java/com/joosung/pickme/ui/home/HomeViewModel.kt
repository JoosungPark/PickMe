package com.joosung.pickme.ui.home

import androidx.databinding.ObservableField
import com.joosung.imagelist.util.LogUtil
import com.joosung.library.rx.RxViewModel
import com.joosung.library.vm.SingleLiveEvent

class HomeViewModel : RxViewModel() {
    private val onRefreshEvent = SingleLiveEvent<Unit>()
    fun getOnRefreshEvent() = onRefreshEvent

    val isLoading = ObservableField(false)
    val isEnabled = ObservableField(true)

    fun onRefresh() {
        isLoading.set(true)
        onRefreshEvent.call()
    }

    fun didRefresh() {
        isLoading.set(false)
    }
}