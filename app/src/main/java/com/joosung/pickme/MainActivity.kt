package com.joosung.pickme

import android.databinding.DataBindingUtil
import android.os.Bundle
import com.joosung.pickme.common.BaseActivity
import com.joosung.pickme.common.FragmentBundle
import com.joosung.pickme.databinding.ActivityMainBinding

class MainActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        loadFragment(FragmentBundle.Home)
    }
}
