package com.joosung.pickme.ui.starred.item

import android.view.View
import com.joosung.pickme.databinding.ItemStarredMediaBinding
import com.joosung.pickme.ui.starred.StarredCellType
import com.joosung.pickme.ui.starred.StarredViewModel
import com.joosung.rxrecycleradapter.RxRecyclerAdapterViewHolder
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo

class StarredMediaCell(
        itemView: View,
        private val viewModel: StarredViewModel,
        private val parentDisposable: CompositeDisposable
) : RxRecyclerAdapterViewHolder<StarredCellType.Media>(itemView, parentDisposable) {
    override fun onBindItem(item: StarredCellType.Media, position: Int) {
        super.onBindItem(item, position)

        (binding as? ItemStarredMediaBinding)?.also { binding ->
            binding.viewModel = viewModel

            item.repo.observeMedia(item.url)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { binding.image = it }
                    .addTo(parentDisposable)
        }
    }
}