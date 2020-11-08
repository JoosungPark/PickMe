package com.joosung.pickme.common

import android.app.Activity
import android.content.Intent
import androidx.fragment.app.Fragment
import com.joosung.pickme.util.KeyboardUtils
import com.joosung.pickme.R
import io.reactivex.disposables.CompositeDisposable

open class BaseFragment : Fragment(), CompositeDisposablePresentable {
    protected val DEBUG_TAG = this::class.java.simpleName
    override val disposables = CompositeDisposable()

    fun baseActivity(): BaseActivity? = (activity as? BaseActivity)

    val baseFragment: BaseFragment get() = this

    fun goToFragment(fragment: BaseFragment, containerId: Int = R.id.container) {
        goToFragment(fragment, null, containerId)
    }

    fun goToFragment(fragment: BaseFragment, animations: ArrayList<Int>? = null, containerId: Int = R.id.container) {
        activity?.let {
            val fm = it.supportFragmentManager
            val ft = fm.beginTransaction()
            animations?.let { ft.setCustomAnimations(it[0], it[1], it[2], it[3]) }
            ft.replace(containerId, fragment)
            ft.addToBackStack(null)

            ft.commitAllowingStateLoss()
        }
    }

    fun pushFragment(fragment: BaseFragment, animations: ArrayList<Int>? = null, containerId: Int = R.id.container, addBackStack: Boolean = true) {
        activity?.let {
            val fm = it.supportFragmentManager
            val ft = fm.beginTransaction()
            if (animations != null) {
                ft.setCustomAnimations(animations[0], animations[1], animations[2], animations[3])
            }
            ft.add(containerId, fragment)
            if (addBackStack) {
                ft.addToBackStack(null)
            }
            ft.commitAllowingStateLoss()
        }
    }

    fun pushFragment(fragment: BaseFragment, containerId: Int = R.id.container) {
        pushFragment(fragment, null, containerId)
    }

    fun goBackFragment() {
        KeyboardUtils.hideKeyboard(this)

        activity?.let {
            val fm = it.supportFragmentManager
            fm.popBackStack()
            fm.beginTransaction().commitAllowingStateLoss()
        }
    }

    fun popFragment() {
        goBackFragment()
    }

    fun setFragmentResult(data: Intent) {
        targetFragment?.onActivityResult(targetRequestCode, Activity.RESULT_OK, data)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        disposables.clear()
    }

}