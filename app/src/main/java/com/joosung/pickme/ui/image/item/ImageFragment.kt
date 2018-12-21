package com.joosung.pickme.ui.image.item

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.joosung.pickme.common.BaseFragment
import com.joosung.pickme.http.api.ImageId
import com.joosung.library.rx.RxViewModel
import com.joosung.pickme.R
import com.joosung.pickme.common.MediaRepository
import com.joosung.pickme.databinding.FragmentImageBinding
import com.joosung.pickme.http.model.AppSharedMedia
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class ImageFragment : BaseFragment() {
    private var binding: FragmentImageBinding? = null
    private val id: ImageId by lazy { arguments?.getString(kImageId) ?: "" }
    private val viewModel: ImageViewModel by viewModel { parametersOf(id) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_image, container, false)
        return binding?.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding?.image = viewModel.image
    }

    companion object {
        const val kImageId = "imageId"

        fun newInstance(id: ImageId): ImageFragment {
            val fragment = ImageFragment()

            val argument = Bundle()
            argument.putString(kImageId, id)
            fragment.arguments = argument

            return fragment
        }
    }
}

class ImageViewModel(id: ImageId, repo: MediaRepository) : RxViewModel() {
    var image: AppSharedMedia? = repo.getMedia(id)
}