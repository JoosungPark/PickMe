package com.joosung.pickme.ui.home

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.joosung.pickme.R
import com.joosung.pickme.common.BaseFragment
import com.joosung.pickme.common.ErrorCatchable
import com.joosung.pickme.common.adapter.ViewPagerAdapter
import com.joosung.pickme.databinding.FragmentHomeBinding
import com.joosung.pickme.extensions.withViewModel
import com.joosung.pickme.ui.search.SearchFragment
import com.joosung.pickme.ui.starred.StarredFragment
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import org.koin.android.viewmodel.ext.android.sharedViewModel

class HomeFragment : BaseFragment(), ErrorCatchable {
    private var binding: FragmentHomeBinding? = null
    private val viewModel: HomeViewModel by sharedViewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        return binding?.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding?.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.also { binding ->
            val searchFragment = SearchFragment.newInstance()
            val starredFragment = StarredFragment.newInstance()

            val adapter = ViewPagerAdapter(childFragmentManager)
            Observable.fromArray(arrayListOf(searchFragment, starredFragment))
                    .subscribe(adapter.rx())
                    .addTo(disposables)

            binding.pager.adapter = adapter
            binding.homeTab.setupWithViewPager(binding.pager)
            binding.homeTab.getTabAt(TabIndex.Search.ordinal)?.setText(R.string.Home_Tab_Search_Result)
            binding.homeTab.getTabAt(TabIndex.Starred.ordinal)?.setText(R.string.Home_Tab_Starred)

            // 탭 인디케이터에 여백을 주기 위해 뷰 자체 여백사용
            val betweenSpace = resources.getDimensionPixelOffset(R.dimen.Tab_Indicator_Margin)
            val slidingTabStrip = binding.homeTab.getChildAt(TabIndex.Search.ordinal) as ViewGroup

            (0 until slidingTabStrip.childCount)
                    .map { slidingTabStrip.getChildAt(it) }
                    .map { it.layoutParams as ViewGroup.MarginLayoutParams }
                    .forEach {
                        it.rightMargin = betweenSpace
                        it.leftMargin = betweenSpace
                    }
        }
    }

    enum class TabIndex {
        Search,
        Starred
    }

    companion object {
        fun newInstance() = HomeFragment()
    }
}