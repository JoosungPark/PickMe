package com.joosung.pickme.util

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.joosung.pickme.common.BaseFragment

class KeyboardUtils {
    companion object {
        fun hideKeyboard(view: View?) {
            if (view == null) {
                return
            }

            view.clearFocus()

            val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }

        fun hideKeyboard(activity: Activity?) {
            if (activity == null) {
                return
            }

            hideKeyboard(activity.currentFocus)
        }

        fun hideKeyboard(fragment: BaseFragment?) {
            if (fragment == null) {
                return
            }

            val activity = fragment.activity

            hideKeyboard(activity)
        }

        fun showKeyboard(view: View?) {
            if (view == null) {
                return
            }

            view.requestFocus()
            val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
        }
    }
}