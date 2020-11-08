package com.joosung.pickme.common.adapter

import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.joosung.pickme.common.FragmentBundle
import com.joosung.pickme.common.FragmentFactory

class BaseViewPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {
    protected val DEBUG_TAG = this.javaClass.simpleName

    protected var items = ArrayList<FragmentBundle>()
    protected val fragmentPosition = hashMapOf<Fragment, Int>()

    override fun getItem(position: Int): Fragment {
        val fragment = FragmentFactory.createFragment(items[position])
        fragmentPosition.put(fragment, position)
        return fragment
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        super.destroyItem(container, position, `object`)
        fragmentPosition.remove(`object`)
    }

    override fun getCount(): Int = items.count()

    fun rx(): (List<FragmentBundle>) -> Unit {
        return fun(list: List<FragmentBundle>) {
            items.clear()
            items.addAll(list)
            notifyDataSetChanged()
        }
    }
}

class ViewPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {
    private val items = arrayListOf<Fragment>()

    override fun getItem(position: Int): Fragment = items[position]

    override fun getCount(): Int = items.count()

    fun rx(): (List<Fragment>) -> Unit {
        return fun(list: List<Fragment>) {
            items.clear()
            items.addAll(list)

            notifyDataSetChanged()
        }
    }
}