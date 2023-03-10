package com.app.func.login_demo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.func.R
import com.app.func.base_content.BaseFragment
import com.app.func.databinding.FragmentSignInBinding
import com.app.func.utils.Constants
import com.app.func.view.animation_view.WaveHelper

class SignInFragment : BaseFragment(), View.OnClickListener {

    private var binding: FragmentSignInBinding? = null
    private var mWaveHelper: WaveHelper? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?

    ): View? {
        binding = FragmentSignInBinding.inflate(inflater, container, false)
        binding?.btnSignIn?.setOnClickListener(this)
        binding?.btnSignUp?.setOnClickListener(this)

        mWaveHelper = WaveHelper(binding?.contentWater)
        binding?.contentWater?.setContent("120" ?: Constants.EMPTY_STRING)

        //binding?.contentWater?.changeToLostConnection()

        binding?.contentWater?.onAnimationUp = {
            //startAnimationUp()
        }
        binding?.contentWater?.onAnimationDown = {
            //startAnimationDown()
        }
        mWaveHelper?.start()
        return binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onClick(view: View?) {
        when (view) {
            binding?.btnSignIn -> {
                getNavController()?.navigate(R.id.homeFragment)
            }
            binding?.btnSignUp -> {
                getNavController()?.navigate(R.id.homeFragment)
            }
        }
    }
}