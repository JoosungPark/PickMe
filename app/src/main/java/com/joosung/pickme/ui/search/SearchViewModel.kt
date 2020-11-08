package com.joosung.pickme.ui.search

import android.content.Context
import androidx.databinding.ObservableField
import com.joosung.library.rx.RxUtils
import com.joosung.library.rx.RxViewModel
import com.joosung.library.rx.Variable
import com.joosung.library.vm.SingleLiveEvent
import com.joosung.pickme.R
import com.joosung.pickme.common.*
import com.joosung.pickme.http.api.GetImageRequest
import com.joosung.pickme.http.api.GetVideoRequest
import com.joosung.pickme.http.model.MediaUrl
import com.joosung.pickme.http.model.SharedMedia
import com.joosung.pickme.ui.starred.StarredViewModelInput
import com.joosung.pickme.ui.starred.StarredViewModelInputImpl
import com.joosung.pickme.util.RealmTransactionRunner
import com.joosung.rxrecycleradapter.RxRecyclerAdapterChangeEvent
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.subjects.PublishSubject
import java.util.ArrayList

class SearchViewModel(
    private val input: SearchViewModelInput,
    override val repo: MediaRepository,
    override val realm: RealmRepository,
    private val service: MediaServerInterface
) : RxViewModel(), MediaStarredInterface {

    private val mediaSource = Variable(arrayListOf<MediaUrl>())
    fun getMediaSource() = mediaSource

    val dataSourceSubject = PublishSubject.create<RxRecyclerAdapterChangeEvent<SearchCellType>>()
    private val errorEvent = SingleLiveEvent<String>()
    fun getErrorEvent() = errorEvent

    val isLoading = ObservableField<Boolean>()
    private var latestItemCount = 0

    private val tapImageEvent = SingleLiveEvent<Int>()
    fun getTapImageEvent() = tapImageEvent

    private val isRefreshingEvent = SingleLiveEvent<Boolean>()
    fun getIsRefreshingEvent() = isRefreshingEvent

    private val defaultSize = repo.getDefaultCount()
    private var page = 1
    private var latestQuery: String? = null

    override val starredMediaSource = input.starredViewModelInput.starredMediaSource
    override val isEdit = input.starredViewModelInput.isEdit

    override val checkedMedias = mutableSetOf<MediaUrl>()
    override val uncheckedMedias = mutableSetOf<MediaUrl>()

    fun monitor() {
        launch {
            mediaSource
                .asObservable()
                .distinctUntilChanged()
                .subscribe {
                    val previous = latestItemCount - 1
                    dataSourceSubject.takeIf { !it.hasComplete() }
                        ?.onNext(RxRecyclerAdapterChangeEvent.Removed(previous))
                    val cells = arrayListOf<SearchCellType>()
                    cells.addAll(it.filterIndexed { index, _ -> index >= previous }.map {
                        SearchCellType.Media(
                            it,
                            repo
                        )
                    })
                    cells.add(SearchCellType.Next)

                    dataSourceSubject.takeIf { !it.hasComplete() }
                        ?.onNext(RxRecyclerAdapterChangeEvent.InsertedRange(previous, cells))
                    latestItemCount = previous + cells.size
                }
        }
    }

    fun init() {
        val cells = arrayListOf(SearchCellType.Description(input.description))
        dataSourceSubject.takeIf { !it.hasComplete() }?.onNext(RxRecyclerAdapterChangeEvent.Reloaded(cells))
    }

    fun queryMedia(query: String? = null) {
        val queryNonNull = query ?: latestQuery

        queryNonNull?.also {
            launch {
                latestItemCount = 0
                mediaSource.value.clear()
                isLoading.set(true)
                page = 1
                latestQuery = it

                service.queryMedia(it, page, defaultSize)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeBy(
                        onSuccess = { urlList ->
                            val cells = arrayListOf<SearchCellType>()

                            if (urlList.isNotEmpty()) {
                                cells.addAll(urlList.map { SearchCellType.Media(it, repo) })
                                cells.add(SearchCellType.Next)
                                latestItemCount = cells.size
                                dataSourceSubject.takeIf { !it.hasComplete() }
                                    ?.onNext(RxRecyclerAdapterChangeEvent.Reloaded(cells))

                                mediaSource.value = urlList
                            }

                            isRefreshingEvent.value = false
                            isLoading.set(false)

                        },
                        onError = { error ->
                            errorEvent.value = error.message
                            isRefreshingEvent.value = false
                            isLoading.set(false)
                        }
                    )
            }
        } ?: kotlin.run {
            errorEvent.value = input.errorEmptyKeyword
            isRefreshingEvent.value = false
        }
    }

    fun queryNext() {
        if (++page > 15) {
            errorEvent.value = "최대 지원 페이지는 15까지입니다."
            return
        }

        latestQuery?.also { query ->
            launch {
                service.queryMedia(query, page, defaultSize)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeBy(
                        onSuccess = { imageIdList ->
                            dataSourceSubject.takeIf { !it.hasComplete() }
                                ?.onNext(RxRecyclerAdapterChangeEvent.Removed(latestItemCount - 1))
                            val cells = arrayListOf<SearchCellType>()

                            if (imageIdList.isNotEmpty()) {
                                cells.addAll(imageIdList.map { SearchCellType.Media(it, repo) })
                                cells.add(SearchCellType.Next)
                                dataSourceSubject.takeIf { !it.hasComplete() }
                                    ?.onNext(RxRecyclerAdapterChangeEvent.InsertedRange(latestItemCount - 1, cells))
                                latestItemCount = latestItemCount - 1 + cells.size

                                val list = arrayListOf<MediaUrl>()
                                list.addAll(mediaSource.value)
                                list.addAll(imageIdList)
                                mediaSource.value = list
                            }
                        },
                        onError = { error ->
                            errorEvent.value = error.message
                        }
                    )
            }
        }

    }

    fun tapMedia(url: MediaUrl?) {
        isEdit.get()?.also { edit ->
            if (edit) {
                onTapEdit(url)
            } else {
                url?.also { tapImageEvent.value = mediaSource.value.indexOf(it) }
            }
        }
    }
}

interface MediaStarredInterface {
    val repo: MediaRepository
    val realm: RealmRepository
    val isEdit: ObservableField<Boolean>
    val checkedMedias: MutableSet<MediaUrl>
    val uncheckedMedias: MutableSet<MediaUrl>
    val starredMediaSource: Variable<ArrayList<MediaUrl>>

    fun longTapMedia(): Boolean {
        return if (isEdit.get() == false) {
            isEdit.set(true)

            true
        } else {

            false
        }
    }

    fun onTapEdit(url: MediaUrl?) {
        url?.also { url ->
            repo.getMedia(url)?.also { media ->
                media.starred.get()?.also {
                    val newValue = !it
                    val deleteTarget = if (newValue) uncheckedMedias else checkedMedias
                    val addTarget = if (newValue) checkedMedias else uncheckedMedias
                    deleteTarget.remove(url)
                    addTarget.add(url)

                    media.starred.set(newValue)
                }
            }
        }
    }

    fun tapCancel() {
        checkedMedias.clear()
        uncheckedMedias.clear()
        isEdit.set(false)
    }

    fun tapSave() {
        val result = arrayListOf<MediaUrl>()

        starredMediaSource.value.filter { uncheckedMedias.contains(it) == false }.map { result.add(it) }
        result.addAll(checkedMedias)

        val realmUpdates = RealmTransactionRunner()
        realmUpdates.add { realm ->
            checkedMedias.map { repo.getMedia(it) }.mapNotNull { it }.forEach { media -> AppRealmMedia.update(realm, media.sm) }
            uncheckedMedias.forEach { url -> RealmQueryBuilder(realm).queryMedia(url)?.deleteFromRealm() }
        }

        realmUpdates.commit(realm)

        starredMediaSource.value = result

        tapCancel()
    }
}

interface SearchViewModelInput {
    val description: String
    val errorEmptyKeyword: String
    val starredViewModelInput: StarredViewModelInput
}

class SearchViewModelInputImpl(
    context: Context,
    override val starredViewModelInput: StarredViewModelInput
) : SearchViewModelInput {

    override val description: String = context.getString(R.string.Search_Description)
    override val errorEmptyKeyword: String = context.getString(R.string.Error_Empty_Keyword)
}

interface MediaServerInterface {
    fun queryMedia(query: String, page: Int, size: Int): Single<ArrayList<MediaUrl>>
}

class MediaServer(private val server: AppServerInterface) : MediaServerInterface {
    override fun queryMedia(query: String, page: Int, size: Int): Single<ArrayList<MediaUrl>> {
        return Single.create { emitter ->
            val imageRequest = server.request(GetImageRequest(query, page, size))
            val videoRequest = server.request(GetVideoRequest(query, page, size))

            RxUtils.zip(imageRequest, videoRequest) { e1, e2 -> Pair(e1, e2) }
                .subscribeBy(
                    onNext = { (imageResponse, videoResponse) ->
                        if (!emitter.isDisposed) {
                            val list = arrayListOf<MediaUrl>()
                            val medias = arrayListOf<SharedMedia>()

                            imageResponse.response.get()?.medias?.also { medias.addAll(it) }
                            videoResponse.response.get()?.medias?.also { medias.addAll(it) }

                            medias.sortWith(Comparator { e1, e2 -> e2.dateTime.time.compareTo(e1.dateTime.time) })
                            medias.forEach { list.add(it.thumbnailUrl()) }

                            emitter.onSuccess(list)
                        }

                    },
                    onError = {
                        emitter.onError(it)
                    }
                )
        }
    }
}