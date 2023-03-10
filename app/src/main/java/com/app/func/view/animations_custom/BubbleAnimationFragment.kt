package com.app.func.view.animations_custom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.func.base_content.BaseFragment
import com.app.func.databinding.BubbleAnimationFragmentBinding

class BubbleAnimationFragment : BaseFragment() {

    private var binding: BubbleAnimationFragmentBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = BubbleAnimationFragmentBinding.inflate(inflater, container, false)

        initActions()
        return binding?.root
    }

    private fun initActions() {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}