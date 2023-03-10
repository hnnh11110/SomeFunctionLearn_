package com.app.func.coroutine_demo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.func.R
import com.app.func.base_content.BaseFragment
import com.app.func.databinding.FragmentCoroutinesBinding

class CoroutinesFragment : BaseFragment(), View.OnClickListener {

    private var binding: FragmentCoroutinesBinding? = null

    private val mBinding get() = binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCoroutinesBinding.inflate(inflater)

        initActions()
        return binding?.root
    }

    private fun initActions() {
        binding?.btnSingleCallNetwork?.setOnClickListener(this)
        binding?.btnSeriesCallNetwork?.setOnClickListener(this)
        binding?.btnParallelCallNetwork?.setOnClickListener(this)
        binding?.btnRoomDatabase?.setOnClickListener(this)
        binding?.btnTimeOut?.setOnClickListener(this)
        binding?.btnTryCatchError?.setOnClickListener(this)
        binding?.btnExceptionHandler?.setOnClickListener(this)
        binding?.btnIgnoreAndContinue?.setOnClickListener(this)
        binding?.btnLongRunTask?.setOnClickListener(this)
        binding?.btnTwoLongRunTask?.setOnClickListener(this)
    }


    override fun onClick(view: View?) {
        when (view) {
            binding?.btnSingleCallNetwork -> {
                getNavController()?.navigate(R.id.singleCallNetworkFragment)
            }
            binding?.btnSeriesCallNetwork -> {
//                getNavController()?.navigate(R.id.listMoviesFragment)
                getNavController()?.navigate(R.id.seriesCallFragment)
            }
            binding?.btnParallelCallNetwork -> {

            }
            binding?.btnRoomDatabase -> {

            }
            binding?.btnTimeOut -> {

            }
            binding?.btnTryCatchError -> {

            }
            binding?.btnExceptionHandler -> {

            }
            binding?.btnIgnoreAndContinue -> {

            }
            binding?.btnLongRunTask -> {

            }
            binding?.btnTwoLongRunTask -> {

            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}