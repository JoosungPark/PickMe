package com.joosung.pickme.ui.image

import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding2.support.v4.view.RxViewPager
import com.jakewharton.rxbinding2.view.RxView
import com.joosung.pickme.common.BaseFragment
import com.joosung.pickme.common.ErrorCatchable
import com.joosung.pickme.extensions.observe
import com.joosung.pickme.extensions.withViewModel
import com.joosung.pickme.ui.home.HomeViewModel
import com.joosung.library.rx.RxUtils
import com.joosung.pickme.R
import com.joosung.pickme.common.adapter.ViewPagerAdapter
import com.joosung.pickme.databinding.FragmentImagePagerBinding
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class ImagePagerFragment() : BaseFragment(), ErrorCatchable {
    private val homeViewModel: HomeViewModel by sharedViewModel()
    private val index: Int by lazy { arguments?.getInt(kIndex) ?: 0 }
//    private val viewModel: ImagePagerViewModel by viewModel { parametersOf(index, homeViewModel.getMediaSource()) }
    private var binding: FragmentImagePagerBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_image_pager, container, false)
        return binding?.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

//        binding?.viewModel = withViewModel({ viewModel }) {
//            observe(getApiErrorEvent()) { error -> error?.also { handleError(activity, it) } }
//        }

//        homeViewModel.monitor()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        val adapter = ImagePagerAdapter(childFragmentManager)
//        binding?.also { binding ->
//            binding.viewPager.adapter = adapter
//
//            viewModel.dataSource
//                .observeOn(AndroidSchedulers.mainThread())
//                .doAfterNext { binding.viewPager.setCurrentItem(viewModel.expectedPosition.value, false) }
//                .subscribe(adapter.rx())
//                .addTo(disposables)
//        }
//
//        initialize(adapter)
//
//        viewModel.onViewCreated()
    }

    private fun initialize(adapter: ViewPagerAdapter) {
//        binding?.also { binding ->
//
//            RxUtils.combineLatest(viewModel.viewCreated.asObservable(), viewModel.expectedPosition.asObservable()) { c, p -> Pair(c, p) }
//                .filter { it.first }
//                .map { it.second }
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe { binding.viewPager.currentItem = it }
//                .addTo(disposables)
//
//            var currentIndex = viewModel.expectedPosition.value
//            var isDraggingAtaLast = false
//
//            RxViewPager.pageSelections(binding.viewPager)
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe { currentIndex = it }
//                .addTo(disposables)
//
//            RxViewPager.pageScrollStateChanges(binding.viewPager)
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe {
//                    val lastIndex = adapter.count - 1
//                    viewModel.isDragging.value = it == ViewPager.SCROLL_STATE_DRAGGING
//
//                    if (currentIndex == lastIndex && it == ViewPager.SCROLL_STATE_DRAGGING) {
//                        isDraggingAtaLast = true
//                    } else if (isDraggingAtaLast && it == ViewPager.SCROLL_STATE_IDLE) {
//                        viewModel.loadNext()
//                    } else {
//                        isDraggingAtaLast = false
//                    }
//                }
//                .addTo(disposables)
//
//            binding.close.bringToFront()
//            RxView.clicks(binding.close)
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe { close(currentIndex) }
//                .addTo(disposables)
//        }
    }

    private fun close(index: Int) {
        baseFragment.setFragmentResult(Intent().putExtra(kLatestIndex, index))
        baseFragment.popFragment()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        disposables.clear()
    }

    companion object {
        const val kIndex = "index"
        const val kLatestIndex = "kLatestIndex"
        const val codeLatestIndex = 1004

        fun newInstance(index: Int): ImagePagerFragment {
            val fragment = ImagePagerFragment()
            val arg = Bundle()
            arg.putInt(kIndex, index)
            fragment.arguments = arg

            return fragment
        }
    }
}