package com.app.func.base_content

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import com.app.func.R
import com.app.func.coroutine_demo.retrofit.aaa.DataRepository
import com.app.func.coroutine_demo.retrofit.base.RetrofitObject
import com.app.func.coroutine_demo.retrofit.base.RetrofitService
import com.app.func.utils.Logger

open class BaseFragment : Fragment() {

    open fun setTitleActionBar() {
        (activity as AppCompatActivity).supportActionBar?.title = this::class.java.simpleName
    }

    fun getNavController(): NavController? {
        return (activity as? BaseActivity)?.getNavController()
    }

    fun getRepositoryRetrofit(url: String): DataRepository {
        val retrofitService = RetrofitObject.getRetrofit(url).create(RetrofitService::class.java)
        return DataRepository(retrofitService)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        Logger.logD(this::class.java.simpleName, "onCreateView is called...")
        return inflater.inflate(R.layout.fragment_base, container, false)
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        Logger.logD(this::class.java.simpleName, "onAttach is called...")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Logger.logD(this::class.java.simpleName, "onCreate is called...")
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Logger.logD(this::class.java.simpleName, "onActivityCreated is called...")
    }

    override fun onStart() {
        super.onStart()
        Logger.logD(this::class.java.simpleName, "onStart is called...")
    }

    override fun onResume() {
        super.onResume()
        Logger.logD(this::class.java.simpleName, "onResume is called...")
        setTitleActionBar()
    }

    override fun onPause() {
        super.onPause()
        Logger.logD(this::class.java.simpleName, "onPause is called...")
    }

    override fun onStop() {
        super.onStop()
        Logger.logD(this::class.java.simpleName, "onStop is called...")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Logger.logD(this::class.java.simpleName, "onStop is called...")
    }

    override fun onDestroy() {
        super.onDestroy()
        Logger.logD(this::class.java.simpleName, "onDestroy is called...")
    }

    override fun onDetach() {
        super.onDetach()
        Logger.logD(this::class.java.simpleName, "onDetach is called...")
    }

    /*
    onAttach()
    onCreate()
    onCreateView()
    onActivityCreated()
    onStart()
    onResume()
    onPause()
    onStop()
    onDestroyView()
    onDestroy()
    onDetach()
     */

    companion object {
        @JvmStatic
        fun newInstance() = BaseFragment()
    }
}