package com.app.func.login_demo.tab_viewpager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.func.base_content.BaseFragment
import com.app.func.databinding.FragmentTabViewpagerBinding

class TabFirstFragment : BaseFragment() {

    private var binding: FragmentTabViewpagerBinding? = null

    fun setInfo(value: String) {
        binding?.textInfo?.text = value
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTabViewpagerBinding.inflate(inflater, container, false)

        return binding?.root
    }

    companion object {
        fun newFragment() = TabFirstFragment()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}