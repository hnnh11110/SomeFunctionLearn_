package com.app.func.thread_demo.snowy

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.app.func.thread_demo.snowy.model.Tutorial

class TutorialPager2Adapter2(private val tutorialList: List<Tutorial>,
    private val itemsCount: Int,
    activity: AppCompatActivity
) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = tutorialList.size

    override fun createFragment(position: Int): Fragment {
        return SnowyMainFragment.newInstance()
    }


}