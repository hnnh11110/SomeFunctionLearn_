package com.app.func.common_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.func.R
import com.app.func.base_content.BaseFragment
import com.app.func.databinding.FragmentMultiSynsBinding

class MultiSynsFragment : BaseFragment(), View.OnClickListener {

    private var binding: FragmentMultiSynsBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMultiSynsBinding.inflate(inflater, container, false)
        binding?.btnRX?.setOnClickListener(this)
        binding?.btnCoroutines?.setOnClickListener(this)
        return binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    companion object {

        @JvmStatic
        fun newInstance() = MultiSynsFragment()
    }

    override fun onClick(view: View?) {
        when (view) {
            binding?.btnRX -> {
                getNavController()?.navigate(R.id.rxFunctionFragment)
            }
            binding?.btnCoroutines -> {
                getNavController()?.navigate(R.id.coroutinesFragment)

            }
        }
    }
}