package com.app.func.coroutine_demo.retrofit.single

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.func.base_content.BaseFragment
import com.app.func.coroutine_demo.data.model.Result
import com.app.func.databinding.FragmentSingleCallNetworkBinding
import com.app.func.utils.Logger

class SingleCallNetworkFragment : BaseFragment() {

    private var mBinding: FragmentSingleCallNetworkBinding? = null
    private var quoteAdapter: QuotesAdapter = QuotesAdapter()
    private var data: List<Result>? = null
    private val mViewModel: SingleCallNetworkViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentSingleCallNetworkBinding.inflate(inflater, container, false)

        initViewModel()
        initObserver()
        initRecyclerView()
        return mBinding?.root
    }

    private fun initObserver() {
        mViewModel.quoteList.observe(viewLifecycleOwner) {
            Logger.logD("aaaa", "aaaaa ${it.body()}")
            data = it.body()?.results
            data?.let { it1 ->
                quoteAdapter.setData(it1)
            }
        }

        mViewModel.errorMessage.observe(viewLifecycleOwner) {
            Toast.makeText(requireActivity(), it, Toast.LENGTH_SHORT).show()
        }

        mViewModel.loading.observe(viewLifecycleOwner) {
            if (it) {
                mBinding?.progressBar?.visibility = View.VISIBLE
            } else {
                mBinding?.progressBar?.visibility = View.GONE
            }
        }

        mViewModel.getQuotes()
    }

    private fun initViewModel() {
        mViewModel.getQuotes()
    }

    private fun initRecyclerView() {
        mBinding?.recyclerView?.layoutManager = LinearLayoutManager(activity)
//        mBinding?.recyclerView?.addItemDecoration(DividerItemDecoration(activity, LinearLayoutManager.VERTICAL))
        mBinding?.recyclerView?.addItemDecoration(
            DividerItemDecoration(
                activity,
                (mBinding?.recyclerView?.layoutManager as LinearLayoutManager).orientation
            )
        )
        mBinding?.recyclerView?.adapter = quoteAdapter
        mViewModel.getQuotes()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        mBinding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() = SingleCallNetworkFragment()
    }
}