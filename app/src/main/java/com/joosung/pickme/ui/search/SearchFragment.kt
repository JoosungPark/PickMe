package com.joosung.pickme.ui.search

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.joosung.library.rx.debug
import com.joosung.pickme.R
import com.joosung.pickme.common.BaseFragment
import com.joosung.pickme.common.ErrorCatchable
import com.joosung.pickme.databinding.FragmentSearchBinding
import com.joosung.pickme.extensions.observe
import com.joosung.pickme.extensions.withViewModel
import com.joosung.pickme.ui.home.HomeViewModel
import com.joosung.pickme.ui.search.item.SearchDescriptionCell
import com.joosung.pickme.ui.search.item.SearchLoadingCell
import com.joosung.pickme.ui.search.item.SearchLoadingCellDelegate
import com.joosung.pickme.ui.search.item.SearchMediaCell
import com.joosung.pickme.util.KeyboardUtils
import com.joosung.rxrecycleradapter.RxRecyclerAdapter
import com.joosung.rxrecycleradapter.RxRecyclerAdapterViewHolder
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import org.koin.android.viewmodel.ext.android.sharedViewModel

class SearchFragment : BaseFragment(), ErrorCatchable {
    private var binding: FragmentSearchBinding? = null
    private val viewModel: SearchViewModel by sharedViewModel()
    private val finderViewModel: FinderViewModel by sharedViewModel()
    private val homeViewModel: HomeViewModel by sharedViewModel()

    private lateinit var adapter: RxRecyclerAdapter<SearchCellType>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false)
        return binding?.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        withViewModel({ viewModel }) {
            observe(getErrorEvent()) { e -> e?.also { handleError(activity, it) }}
            observe(getIsRefreshingEvent()) { _ -> homeViewModel.didRefresh() }
        }

        withViewModel({ finderViewModel }) {
            observe(getErrorEvent()) { e -> e?.also { handleError(activity, it) } }
            observe(getFindEvent()) { keyword -> keyword?.also { query(it) }}
            observe(getClearEvent()) { _ -> binding?.finder?.findInput?.setText("") }
        }

        withViewModel({ homeViewModel }) {
            observe(getOnRefreshEvent()) { _ -> reload() }
        }

        binding?.viewModel = viewModel
        binding?.finderViewModel = finderViewModel

        bindList()
    }

    private fun query(keyword: String) {
        viewModel.queryMedia(keyword)
        binding?.finder?.findInput?.clearFocus()
        KeyboardUtils.hideKeyboard(binding?.finder?.findInput)
    }

    private fun reload() {
        viewModel.queryMedia()
    }

    private fun bindList() {
        binding?.also { binding ->
            activity?.also { binding.recycler.layoutManager = LinearLayoutManager(it) }

            adapter = RxRecyclerAdapter(object : RxRecyclerAdapter.Delegate<SearchCellType> {
                override fun getItemViewType(position: Int, item: SearchCellType): Int {
                    return when (item) {
                        is SearchCellType.Media -> R.layout.item_search_media
                        is SearchCellType.Description -> R.layout.item_description
                        is SearchCellType.Next -> R.layout.item_search_loading
                    }
                }

                override fun viewHolderForViewType(parent: ViewGroup, viewType: Int): RxRecyclerAdapterViewHolder<SearchCellType> {
                    val viewHolder = when (viewType) {
                        R.layout.item_search_media -> SearchMediaCell(inflate(parent, viewType), viewModel, disposables)
                        R.layout.item_description -> SearchDescriptionCell(inflate(parent, viewType), disposables)
                        R.layout.item_search_loading -> SearchLoadingCell(inflate(parent, viewType), delegate, disposables)
                        else -> throw RuntimeException("Fatal Error")
                    }

                    @Suppress("UNCHECKED_CAST")
                    return viewHolder as RxRecyclerAdapterViewHolder<SearchCellType>
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

    private val delegate = object : SearchLoadingCellDelegate {
        override fun reload() {
            viewModel.queryNext()
        }
    }

    companion object {
        fun newInstance(): SearchFragment = SearchFragment()
    }
}