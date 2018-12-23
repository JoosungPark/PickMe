package com.joosung.pickme.di

import android.databinding.ObservableField
import com.joosung.pickme.ui.home.HomeViewModel
import com.joosung.pickme.ui.image.ImagePagerViewModel
import com.joosung.pickme.ui.image.item.ImageViewModel
import com.joosung.library.rx.Variable
import com.joosung.pickme.common.*
import com.joosung.pickme.http.model.MediaUrl
import com.joosung.pickme.ui.search.*
import com.joosung.pickme.ui.starred.StarredViewModel
import com.joosung.pickme.ui.starred.StarredViewModelInput
import com.joosung.pickme.ui.starred.StarredViewModelInputImpl
import org.koin.android.ext.koin.androidApplication
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val appModule = module {
    val appShared = AppShared(AppConfig(AppConfig.Setting.Toy))
    val realm = "realm"

    single<RealmRepository>(createOnStart = true) { appShared }
    single<AppSharedInterface>(createOnStart = true) { appShared }
    single<AppServerInterface>(createOnStart = true) { appShared }
    single<MediaRepository>(createOnStart = true) { appShared }
    single<MediaServerInterface>(createOnStart = true) { MediaServer(get()) }
    single<StarredViewModelInput>(createOnStart = true) { StarredViewModelInputImpl(ObservableField(false), Variable(ArrayList(RealmQueryBuilder(appShared.realm).queryMediaUrlList()))) }
    single<SearchViewModelInput>(createOnStart = true) { SearchViewModelInputImpl(androidApplication(), get()) }
    single<FinderViewModelInput>(createOnStart = true) { FinderViewModelImpl(androidApplication()) }

    viewModel { HomeViewModel() }
    viewModel { SearchViewModel(get(), get(), get(), get()) }
    viewModel { StarredViewModel(get(), get(), get()) }
    viewModel { FinderViewModel(get()) }
    viewModel { (index: Int, imageIdList: Variable<ArrayList<MediaUrl>>) -> ImagePagerViewModel(index, imageIdList, get()) }
    viewModel { (id: MediaUrl) -> ImageViewModel(id, get()) }
}

val pickMeApp = listOf(appModule)