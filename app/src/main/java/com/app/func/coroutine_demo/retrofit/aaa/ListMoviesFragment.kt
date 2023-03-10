package com.app.func.coroutine_demo.retrofit.aaa

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.func.base_content.BaseFragment
import com.app.func.coroutine_demo.retrofit.base.ApiConstants
import com.app.func.databinding.FragmentSingleCallNetworkBinding

class ListMoviesFragment : BaseFragment() {

    private var mBinding: FragmentSingleCallNetworkBinding? = null
    private val movieAdapter = MovieAdapter()

    //    private var mViewModel : ListMovieViewModel by viewModels()
    private lateinit var mViewModel: ListMovieViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentSingleCallNetworkBinding.inflate(inflater, container, false)
        val layoutManager = LinearLayoutManager(requireActivity())
        mBinding?.recyclerView?.layoutManager = layoutManager
        mBinding?.recyclerView?.adapter = movieAdapter

        initViewModel()

        mViewModel.movieList.observe(viewLifecycleOwner) {
            movieAdapter.setMovies(it)
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

        mViewModel.getAllMovies()

        initObserver()
        initRecyclerView()
        return mBinding?.root
    }

    private fun initObserver() {

    }

    private fun initViewModel() {
        val mainRepository = getRepositoryRetrofit(ApiConstants.BASE_URL_ANDROID)
        mViewModel = ViewModelProvider(
            this,
            MyViewModelFactory(mainRepository)
        )[ListMovieViewModel::class.java]
    }

    private fun initRecyclerView() {


    }

    override fun onDestroyView() {
        super.onDestroyView()
        mBinding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() = ListMoviesFragment()
    }
}