package com.app.func.features.animations_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.func.R
import com.app.func.base_content.BaseFragment
import com.app.func.databinding.MainAnimationFragmentBinding

class MainAnimationFragment : BaseFragment() {

    private var binding: MainAnimationFragmentBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = MainAnimationFragmentBinding.inflate(inflater, container, false)

        initActions()
        return binding?.root
    }

    private fun initActions() {
        binding?.btnBall1?.setOnClickListener {
            getNavController()?.navigate(R.id.bubbleAnimationFragment)
        }
        binding?.btnBall2?.setOnClickListener {
            getNavController()?.navigate(R.id.bubbleEmitterFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}