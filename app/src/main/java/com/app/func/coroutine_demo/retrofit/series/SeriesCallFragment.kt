package com.app.func.coroutine_demo.retrofit.series

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.func.base_content.BaseFragment
import com.app.func.coroutine_demo.data.model.QuoteListResponse
import com.app.func.coroutine_demo.data.model.Result
import com.app.func.coroutine_demo.retrofit.base.ApiConstants
import com.app.func.coroutine_demo.retrofit.base.RetrofitObject
import com.app.func.coroutine_demo.retrofit.base.RetrofitService
import com.app.func.coroutine_demo.retrofit.single.QuotesAdapter
import com.app.func.databinding.FragmentSingleCallNetworkBinding
import com.app.func.utils.Logger
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SeriesCallFragment : BaseFragment() {

    private var mBinding: FragmentSingleCallNetworkBinding? = null
    private var quoteAdapter: QuotesAdapter = QuotesAdapter()
    private var data: List<Result>? = null

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
        RetrofitObject.getRetrofit(ApiConstants.BASE_URL_QUOTE).create(RetrofitService::class.java)
            .getQuoteNormal().enqueue(object : Callback<QuoteListResponse> {
                override fun onResponse(
                    call: Call<QuoteListResponse>,
                    response: Response<QuoteListResponse>
                ) {
                    val valueGet: QuoteListResponse? = response.body()
                    Logger.logD("series_aaa", "Okie, thanh cong   --- ${valueGet?.results}")
                    valueGet?.results?.let { quoteAdapter.setData(it) }
                }

                override fun onFailure(call: Call<QuoteListResponse>, t: Throwable) {
                    Logger.logD("series_aaa", "Co loi xay ra   --- ")
                }
            })
    }

    private fun initViewModel() {
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
//        mViewModel.getQuotes()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        mBinding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() = SeriesCallFragment()
    }
}