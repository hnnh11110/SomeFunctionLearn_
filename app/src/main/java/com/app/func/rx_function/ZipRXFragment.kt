package com.app.func.rx_function

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.func.base_content.BaseFragment
import com.app.func.databinding.FragmentSimpleRxBinding
import com.app.func.rx_function.model.User
import com.app.func.rx_function.utils.AppConstant
import com.app.func.rx_function.utils.Utils
import com.app.func.utils.Logger
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableOnSubscribe
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.functions.BiFunction
import io.reactivex.rxjava3.schedulers.Schedulers

class ZipRXFragment : BaseFragment(), View.OnClickListener {

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
     * Here we are getting two user list
     * One, the list of cricket fans
     * Another one, the list of football fans
     * Then we are finding the list of users who loves both
     */
    private fun doSomeWork() {
        Observable.zip(getCricketFansObservable(), getFootballFansObservable(),
            BiFunction { cricketFans, footballFans ->
                return@BiFunction Utils.filterUserWhoLovesBoth(cricketFans, footballFans)
            })
            // Run on a background thread
            .subscribeOn(Schedulers.io())
            // Be notified on the main thread
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(getObserver())
    }

    private fun getObserver(): Observer<List<User>> {
        return object : Observer<List<User>> {

            override fun onSubscribe(d: Disposable) {
                Logger.logD(TAG, " onSubscribe : " + d.isDisposed)
            }

            override fun onNext(userList: List<User>) {
                binding?.textView?.append(" onNext")
                binding?.textView?.append(AppConstant.LINE_SEPARATOR)
                for (user in userList) {
                    binding?.textView?.append(" firstname : ${user.firstname}")
                    binding?.textView?.append(AppConstant.LINE_SEPARATOR)
                }
                Logger.logD(TAG, " onNext : " + userList.size)
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

    private fun getFootballFansObservable(): Observable<List<User>> {
        return Observable.create(ObservableOnSubscribe<List<User>> {
            if (!it.isDisposed) {
                it.onNext(Utils.getUserListWhoLovesFootball())
                it.onComplete()
            }
        }).subscribeOn(Schedulers.io())
    }

    private fun getCricketFansObservable(): Observable<List<User>> {
        return Observable.create(ObservableOnSubscribe<List<User>> {
            if (!it.isDisposed) {
                it.onNext(Utils.getUserListWhoLovesCricket())
                it.onComplete()
            }
        }).subscribeOn(Schedulers.io())
    }

    companion object {
        val TAG: String = this::class.java.simpleName

        @JvmStatic
        fun newInstance() = ZipRXFragment()
    }

}