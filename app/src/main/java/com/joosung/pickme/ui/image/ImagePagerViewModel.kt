package com.joosung.pickme.ui.image

import com.joosung.pickme.common.FragmentBundle
import com.joosung.pickme.http.api.ImageId
import com.joosung.library.rx.RxViewModel
import com.joosung.library.rx.Variable
import com.joosung.library.vm.SingleLiveEvent
import com.joosung.pickme.ui.search.MediaServerInterface
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy

class ImagePagerViewModel(
    index: Int,
    private val imageSource: Variable<ArrayList<ImageId>>,
    private val service: MediaServerInterface
) : RxViewModel() {

    val dataSource = imageSource.asObservable().map { list -> list.map { FragmentBundle.Image(it) } }
    val viewCreated = Variable(false)
    val expectedPosition = Variable(index)
    val isDragging = Variable(false)
    val isLoading = Variable(false)

    private val apiErrorEvent = SingleLiveEvent<String>()
    fun getApiErrorEvent() = apiErrorEvent

    fun onViewCreated() {
        viewCreated.value = true
    }

    fun loadNext() {
//        launch {
//            isLoading.value = true
//            service.loadImage()
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeBy(
//                    onSuccess = { imageIdList ->
//                        if (imageIdList.isNotEmpty()) {
//                            expectedPosition.value = imageSource.value.size
//                        }
//                        val list = arrayListOf<ImageId>()
//                        list.addAll(imageSource.value)
//                        list.addAll(imageIdList)
//                        imageSource.value = list
//                        isLoading.value = false
//                    },
//                    onError = { error ->
//                        isLoading.value = false
//                        apiErrorEvent.value = error.message
//                    }
//                )
//        }
    }
}