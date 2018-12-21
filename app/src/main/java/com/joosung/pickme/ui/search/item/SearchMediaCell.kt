package com.joosung.pickme.ui.search.item

import android.view.View
import com.joosung.imagelist.util.LogUtil
import com.joosung.library.rx.debug
import com.joosung.pickme.databinding.ItemSearchMediaBinding
import com.joosung.pickme.ui.search.SearchCellType
import com.joosung.pickme.ui.search.SearchViewModel
import com.joosung.rxrecycleradapter.RxRecyclerAdapterViewHolder
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo

class SearchMediaCell(
        itemView: View,
        private val viewModel: SearchViewModel,
        private val parentDisposable: CompositeDisposable
) : RxRecyclerAdapterViewHolder<SearchCellType.Media>(itemView, parentDisposable) {
    override fun onBindItem(item: SearchCellType.Media, position: Int) {
        super.onBindItem(item, position)

        (binding as? ItemSearchMediaBinding)?.also { binding ->
            binding.viewModel = viewModel

            item.repo.observeMedia(item.url)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { binding.image = it }
                    .addTo(parentDisposable)
        }
    }
}