package com.app.func.rx_function

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.func.base_content.BaseFragment
import com.app.func.databinding.FragmentSimpleRxBinding
import com.app.func.utils.Constants
import com.app.func.utils.Logger
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers

class SimpleRXFragment : BaseFragment(), View.OnClickListener {

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

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun initObservers() {

    }

    private fun initActions() {
        binding?.doSomeWork?.setOnClickListener(this)
    }

    private fun initViews() {

    }

    companion object {
        val TAG: String = SimpleRXFragment::class.java.simpleName

        @JvmStatic
        fun newInstance() = SimpleRXFragment()
    }

    override fun onClick(view: View?) {
        if (view == binding?.doSomeWork) {
            binding?.loadingView?.visibility = View.VISIBLE
            Handler(Looper.getMainLooper()).postDelayed({
                doSomeWork()
            }, 1000L)

        }
    }

    private fun getObservable(): Observable<String> {
        return Observable.just("Cricket", "Football")
    }

    private fun doSomeWork() {
        getObservable()
            // Run on a background thread
            .subscribeOn(Schedulers.io())
            // Be notified on the main thread
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(getObserver())
    }

    private fun getObserver(): Observer<String> {
        val observer = object : Observer<String> {
            override fun onSubscribe(d: Disposable) {
                Logger.logD(TAG, " onSubscribe : ${d.isDisposed}")
            }

            override fun onNext(value: String) {
                binding?.textView?.append(" onNext : value : $value")
                binding?.textView?.append(Constants.LINE_SEPARATOR)
                Logger.logD(TAG, " onNext : value : $value")
            }

            override fun onError(e: Throwable) {
                binding?.textView?.append(" onError : " + e.message)
                binding?.textView?.append(Constants.LINE_SEPARATOR)
                Logger.logD(TAG, " onError : " + e.message)
                binding?.loadingView?.visibility = View.GONE
            }

            override fun onComplete() {
                binding?.textView?.append(" onComplete")
                binding?.textView?.append(Constants.LINE_SEPARATOR)
                Logger.logD(TAG, " onComplete")
                binding?.loadingView?.visibility = View.GONE
            }
        }
        return observer
    }
}