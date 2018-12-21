package com.joosung.pickme.di

import com.joosung.pickme.http.api.ImageId
import com.joosung.pickme.ui.home.HomeViewModel
import com.joosung.pickme.ui.image.ImagePagerViewModel
import com.joosung.pickme.ui.image.item.ImageViewModel
import com.joosung.library.rx.Variable
import com.joosung.pickme.common.*
import com.joosung.pickme.ui.search.*
import org.koin.android.ext.koin.androidApplication
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val appModule = module {
    val appShared = AppShared(AppConfig(AppConfig.Setting.Toy))

    single<AppSharedInterface>(createOnStart = true) { appShared }
    single<AppServerInterface>(createOnStart = true) { appShared }
    single<MediaRepository>(createOnStart = true) { appShared }
    single<MediaServerInterface>(createOnStart = true) { MediaServer(get()) }
    single<SearchViewModelInput>(createOnStart = true) { SearchViewModelInputImpl(androidApplication()) }
    single<FinderViewModelInput>(createOnStart = true) { FinderViewModelImpl(androidApplication()) }

    viewModel { HomeViewModel() }
    viewModel { SearchViewModel(get(), get(), get()) }
    viewModel { FinderViewModel(get()) }
    viewModel { (index: Int, imageIdList: Variable<ArrayList<ImageId>>) -> ImagePagerViewModel(index, imageIdList, get()) }
    viewModel { (id: ImageId) -> ImageViewModel(id, get()) }
}

val pickMeApp = listOf(appModule)