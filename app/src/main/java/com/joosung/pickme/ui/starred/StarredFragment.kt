package com.joosung.pickme.ui.starred

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.joosung.pickme.R
import com.joosung.pickme.common.BaseFragment
import com.joosung.pickme.databinding.FragmentStarredBinding
import com.joosung.pickme.ui.search.SearchCellType
import com.joosung.pickme.ui.search.item.SearchDescriptionCell
import com.joosung.pickme.ui.search.item.SearchLoadingCell
import com.joosung.pickme.ui.search.item.SearchMediaCell
import com.joosung.pickme.ui.starred.item.StarredMediaCell
import com.joosung.rxrecycleradapter.RxRecyclerAdapter
import com.joosung.rxrecycleradapter.RxRecyclerAdapterViewHolder
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import org.koin.android.viewmodel.ext.android.sharedViewModel

class StarredFragment : BaseFragment() {

    private var binding: FragmentStarredBinding? = null
    private val viewModel: StarredViewModel by sharedViewModel()
    private lateinit var adapter: RxRecyclerAdapter<StarredCellType>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_starred, container, false)
        return binding?.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        bindList()
    }

    private fun bindList() {
        binding?.also { binding ->
            activity?.also { binding.recycler.layoutManager = LinearLayoutManager(it) }

            adapter = RxRecyclerAdapter(object : RxRecyclerAdapter.Delegate<StarredCellType> {
                override fun getItemViewType(position: Int, item: StarredCellType): Int {
                    return when (item) {
                        is StarredCellType.Media -> R.layout.item_starred_media
                    }
                }

                override fun viewHolderForViewType(parent: ViewGroup, viewType: Int): RxRecyclerAdapterViewHolder<StarredCellType> {
                    val viewHolder = when (viewType) {
                        R.layout.item_starred_media -> StarredMediaCell(inflate(parent, viewType), viewModel, disposables)
                        else -> throw RuntimeException("Fatal Error")
                    }

                    @Suppress("UNCHECKED_CAST")
                    return viewHolder as RxRecyclerAdapterViewHolder<StarredCellType>
                }
            })

            binding.recycler.adapter = adapter
        }

        viewModel.dataSourceSubject
                .startWith(Observable.empty())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(adapter)
                .addTo(disposables)

        viewModel.init()
    }

    companion object {
        fun newInstance() = StarredFragment()
    }
}