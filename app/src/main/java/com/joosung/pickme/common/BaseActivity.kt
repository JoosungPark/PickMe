package com.joosung.pickme.common

import androidx.appcompat.app.AppCompatActivity
import com.joosung.pickme.R
import io.reactivex.disposables.CompositeDisposable
import java.util.ArrayList

open class BaseActivity : AppCompatActivity(), CompositeDisposablePresentable {
    protected val DEBUG_TAG = this.javaClass.simpleName
    override val disposables = CompositeDisposable()

    enum class ActivityType {
        ImagePager
    }

    fun loadFragment(fragment: BaseFragment, animations: ArrayList<Int>? = null, replace: Boolean = false, containerId: Int = R.id.container, allowStateLoss: Boolean = false) {
        val fm = supportFragmentManager
        val ft = fm.beginTransaction()
        if (animations != null) {
            ft.setCustomAnimations(animations[0], animations[1], animations[2], animations[3])
        }
        if (replace) {
            ft.replace(containerId, fragment)
            ft.addToBackStack(null)
        } else {
            ft.add(containerId, fragment)
        }


        if (allowStateLoss) {
            ft.commitAllowingStateLoss()
        } else {
            ft.commit()
        }
    }

    fun replaceFragment(fragment: BaseFragment, containerId: Int = R.id.container) {
        val fm = supportFragmentManager
        val ft = fm.beginTransaction()
        ft.replace(containerId, fragment)
        ft.commit()
    }

    fun loadFragment(fragment: BaseFragment, replace: Boolean = false, containerId: Int = R.id.container) {
        val fm = supportFragmentManager
        val ft = fm.beginTransaction()
        if (replace) {
            ft.replace(containerId, fragment)
            ft.addToBackStack(null)
        } else {
            ft.add(containerId, fragment)
        }
        ft.commit()
    }

    fun loadFragment(type: FragmentBundle, replace: Boolean = false, containerId: Int = R.id.container) {
        loadFragment(FragmentFactory.createFragment(type), replace, containerId)
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.clear()
    }

    override fun onBackPressed() {
        val fm = supportFragmentManager

        if (fm.backStackEntryCount == 0) {
            super.onBackPressed()
        } else {
            fm.popBackStack()
            fm.beginTransaction().commit()
        }
    }

}