package com.joosung.pickme.ui.search.item

import android.view.View
import com.joosung.pickme.ui.search.SearchCellType
import com.joosung.rxrecycleradapter.RxRecyclerAdapterViewHolder
import io.reactivex.disposables.CompositeDisposable

class SearchLoadingCell(
        itemView: View,
        private val delegate: SearchLoadingCellDelegate,
        parentDisposable: CompositeDisposable
) : RxRecyclerAdapterViewHolder<SearchCellType.Next>(itemView, parentDisposable) {

    override fun onBindItem(item: SearchCellType.Next, position: Int) {
        super.onBindItem(item, position)

        delegate.reload()
    }
}

interface SearchLoadingCellDelegate {
    fun reload()
}