package com.joosung.pickme.common

import com.joosung.pickme.ui.home.HomeFragment

sealed class FragmentBundle {
    object Home : FragmentBundle()
}

class FragmentFactory {

    companion object {
        fun createFragment(bundle: FragmentBundle): BaseFragment = when (bundle) {
                is FragmentBundle.Home -> HomeFragment.newInstance()
        }
    }
}