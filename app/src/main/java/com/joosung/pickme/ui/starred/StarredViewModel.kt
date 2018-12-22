package com.joosung.pickme.ui.starred

import android.databinding.ObservableField
import com.joosung.library.rx.RxViewModel
import com.joosung.library.rx.Variable
import com.joosung.library.vm.SingleLiveEvent
import com.joosung.pickme.common.MediaRepository
import com.joosung.pickme.http.model.MediaUrl
import com.joosung.pickme.ui.search.MediaStarredInterface
import com.joosung.rxrecycleradapter.RxRecyclerAdapterChangeEvent
import io.reactivex.subjects.PublishSubject

class StarredViewModel(
        input: StarredViewModelInput,
        override val repo: MediaRepository
) : RxViewModel(), MediaStarredInterface {

    override val starredMediaSource = input.starredMediaSource

    val dataSourceSubject = PublishSubject.create<RxRecyclerAdapterChangeEvent<StarredCellType>>()
    val isLoading = ObservableField<Boolean>()
    override val isEdit = input.isEdit
    override val checkedMedias = arrayListOf<MediaUrl>()
    override val uncheckedMedias = arrayListOf<MediaUrl>()

    private val tapImageEvent = SingleLiveEvent<Int>()
    fun getTapImageEvent() = tapImageEvent

    fun init() {
        launch {
            starredMediaSource
                    .asObservable()
                    .map {
                        it.sortWith(Comparator { e1, e2 ->
                            val left = repo.getMedia(e1)?.dateTime?.get()
                            val right = repo.getMedia(e2)?.dateTime?.get()

                            if (left != null && right != null) left.compareTo(right) else 0
                        })

                        it
                    }
                    .distinctUntilChanged()
                    .subscribe { urlList ->
                        val cells = urlList.map { StarredCellType.Media(it, repo) }
                        dataSourceSubject.takeIf { !it.hasComplete() }
                                ?.onNext(RxRecyclerAdapterChangeEvent.Reloaded(cells))
                    }
        }
    }

    fun tapMedia(url: MediaUrl?) {
        isEdit.get()?.also { edit ->
            if (edit) {
                onTapEdit(url)
            } else {
                url?.also { tapImageEvent.value = starredMediaSource.value.indexOf(it) }
            }
        }
    }
}

interface StarredViewModelInput {
    val isEdit: ObservableField<Boolean>
    val starredMediaSource: Variable<ArrayList<MediaUrl>>
}

open class StarredViewModelInputImpl(override val isEdit: ObservableField<Boolean>, override val starredMediaSource: Variable<ArrayList<MediaUrl>>) : StarredViewModelInput