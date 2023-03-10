package com.app.func.rx_function

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.func.base_content.BaseFragment
import com.app.func.databinding.FragmentSimpleRxBinding
import com.app.func.rx_function.utils.AppConstant
import com.app.func.utils.Logger
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable

class FilterRXFragment : BaseFragment(), View.OnClickListener {

    private var binding: FragmentSimpleRxBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSimpleRxBinding.inflate(inflater, container, false)
        initViews()
        initActions()
        initObservers()
        return binding?.root
    }

    private fun initObservers() {

    }

    private fun initActions() {
        binding?.doSomeWork?.setOnClickListener(this)
    }

    private fun initViews() {

    }

    override fun onClick(view: View?) {
        if (view == binding?.doSomeWork) {
            binding?.loadingView?.visibility = View.VISIBLE
            Handler(Looper.getMainLooper()).postDelayed({
                doSomeWork()
            }, 1000L)

        }
    }

    /*
     * simple example by using filter operator to emit only even value
     *
     */
    private fun doSomeWork() {
        Observable.just(1, 2, 3, 4, 5, 6)
            .filter { value ->
                return@filter value % 2 == 0
            }
            .subscribe(getObserver())
    }


    private fun getObserver(): Observer<Int> {

        return object : Observer<Int> {

            override fun onSubscribe(d: Disposable) {
                Logger.logD(TAG, " onSubscribe : " + d.isDisposed)
            }

            override fun onNext(value: Int) {
                binding?.textView?.append(" onNext : ")
                binding?.textView?.append(AppConstant.LINE_SEPARATOR)
                binding?.textView?.append(" value : $value")
                binding?.textView?.append(AppConstant.LINE_SEPARATOR)
                Logger.logD(TAG, " onNext ")
                Logger.logD(TAG, " value : $value")
            }

            override fun onError(e: Throwable) {
                binding?.textView?.append(" onError : " + e.message)
                binding?.textView?.append(AppConstant.LINE_SEPARATOR)
                Logger.logD(TAG, " onError : " + e.message)
                binding?.loadingView?.visibility = View.GONE
            }

            override fun onComplete() {
                binding?.textView?.append(" onComplete")
                binding?.textView?.append(AppConstant.LINE_SEPARATOR)
                Logger.logD(TAG, " onComplete")
                binding?.loadingView?.visibility = View.GONE
            }
        }
    }


    companion object {
        val TAG: String = this::class.java.simpleName

        @JvmStatic
        fun newInstance() = FilterRXFragment()
    }

}