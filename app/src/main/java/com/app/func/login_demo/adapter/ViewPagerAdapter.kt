package com.app.func.login_demo.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.app.func.login_demo.tab_viewpager.TabFirstFragment
import com.app.func.login_demo.tab_viewpager.TabSecondFragment

class ViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    var listFragment = ArrayList<Fragment>()

    fun setData(
        tabFirst: TabFirstFragment?,
        tabSecond: TabSecondFragment?,
        tabThird: TabSecondFragment?
    ) {
        listFragment.clear()
        tabFirst?.let { listFragment.add(it) }
        tabSecond?.let { listFragment.add(it) }
        tabThird?.let { listFragment.add(it) }
        notifyDataSetChanged()
    }

    fun addFragment(fragment: Fragment) {
        listFragment.add(fragment)
        notifyDataSetChanged()
    }

    fun removeFragment() {
        if (listFragment.size > 1) {
            listFragment.removeAt(listFragment.size - 1)
            notifyDataSetChanged()
        } else {
            //No thing
        }
    }

    override fun getItemCount(): Int = listFragment.size

    override fun createFragment(position: Int): Fragment = listFragment[position]

}