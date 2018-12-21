package com.joosung.pickme.ui.search.item

import android.view.View
import com.joosung.pickme.databinding.ItemDescriptionBinding
import com.joosung.pickme.ui.search.SearchCellType
import com.joosung.rxrecycleradapter.RxRecyclerAdapterViewHolder
import io.reactivex.disposables.CompositeDisposable

class SearchDescriptionCell(
        itemView: View,
        parentDisposable: CompositeDisposable
) : RxRecyclerAdapterViewHolder<SearchCellType.Description>(itemView, parentDisposable) {
    override fun onBindItem(item: SearchCellType.Description, position: Int) {
        super.onBindItem(item, position)

        (binding as? ItemDescriptionBinding)?.also { binding ->
            binding.description.text = item.description
        }
    }
}