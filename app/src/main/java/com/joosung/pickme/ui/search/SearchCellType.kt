package com.joosung.pickme.ui.search

import com.joosung.pickme.common.MediaRepository
import com.joosung.pickme.http.model.MediaUrl
import com.joosung.rxrecycleradapter.RxRecyclerAdapterData

sealed class SearchCellType : RxRecyclerAdapterData {
    data class Description(val description: String) : SearchCellType()
    data class Media(val url: MediaUrl, val repo: MediaRepository) : SearchCellType()
    object Next : SearchCellType()
}