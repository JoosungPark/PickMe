package com.joosung.pickme.ui.search

import android.content.Context
import android.databinding.ObservableField
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import com.joosung.library.rx.RxViewModel
import com.joosung.library.vm.SingleLiveEvent
import com.joosung.pickme.R

class FinderViewModel(private val input: FinderViewModelInput) : RxViewModel() {
    private val findEvent = SingleLiveEvent<String>()
    fun getFindEvent() = findEvent

    private val errorEvent = SingleLiveEvent<String>()
    fun getErrorEvent() = errorEvent

    private val clearEvent = SingleLiveEvent<Unit>()
    fun getClearEvent() = clearEvent

    val isVisibleDelete = ObservableField<Boolean>()

    val onEditorActionListener = TextView.OnEditorActionListener { textView, actionId, _ ->
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            val keyword = textView.text.toString()
            if (keyword.isEmpty()) {
                errorEvent.value = input.errorEmptyKeyword
            } else {
                findEvent.value = keyword
            }

            keyword.isNotEmpty()
        } else {
            false
        }
    }

    fun find(actionId: Int) {

    }

    fun onTextChanged(s: CharSequence) {
        isVisibleDelete.set(s.toString().isNotEmpty())
    }

    fun tapDelete() {
        clearEvent.call()
    }
}

interface FinderViewModelInput {
    val errorEmptyKeyword: String
}

class FinderViewModelImpl(context: Context) : FinderViewModelInput {
    override val errorEmptyKeyword: String = context.getString(R.string.Error_Empty_Keyword)
}