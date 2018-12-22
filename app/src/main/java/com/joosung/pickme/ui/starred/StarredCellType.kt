package com.joosung.pickme.ui.starred

import com.joosung.pickme.common.MediaRepository
import com.joosung.pickme.http.model.MediaUrl
import com.joosung.rxrecycleradapter.RxRecyclerAdapterData

sealed class StarredCellType : RxRecyclerAdapterData {
    data class Media(val url: MediaUrl, val repo: MediaRepository) : StarredCellType()
}