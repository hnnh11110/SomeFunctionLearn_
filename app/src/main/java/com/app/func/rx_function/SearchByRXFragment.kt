package com.app.func.rx_function

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.func.base_content.BaseFragment
import com.app.func.databinding.FragmentSearchByRxBinding
import com.app.func.rx_function.utils.getQueryTextChangeObservable
import com.app.func.utils.Constants
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class SearchByRXFragment : BaseFragment() {

    private var binding: FragmentSearchByRxBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchByRxBinding.inflate(inflater, container, false)
        initViews()
        initActions()
        initObservers()
        return binding?.root
    }

    private fun initObservers() {
        /* binding!!.searchView.getQueryTextChangeObservable()
             .debounce(300, TimeUnit.MILLISECONDS)
             .filter { text ->
                 if (text.isEmpty()) {
                     binding!!.textViewResult.text = ""
                     return@filter false
                 } else {
                     return@filter true
                 }
             }
             .distinctUntilChanged()
             .switchMap { query ->
                 dataFromNetwork(query)
                     .doOnError {
                         // handle error
                     }
                     .onErrorReturn { "" }
             }
             .subscribeOn(Schedulers.io())
             .observeOn(AndroidSchedulers.mainThread())
             .subscribe { result ->
                 binding!!.textViewResult.text = result
             }*/

        binding?.searchView?.getQueryTextChangeObservable()
            ?.debounce(300, TimeUnit.MILLISECONDS)
            ?.filter {
                if (it.isEmpty()) {
                    binding?.textViewResult?.text = ""
                    return@filter false
                } else {
                    return@filter true
                }
            }?.distinctUntilChanged()?.switchMap {
                dataFromNetwork(it)
                    .doOnError {
                        //Handle Error
                    }.onErrorReturn { Constants.EMPTY_STRING }
            }
            ?.subscribeOn(Schedulers.io())?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe {
                binding?.textViewResult?.text = it
            }
    }

    /**
     * Simulation of network data
     */
    private fun dataFromNetwork(query: String): Observable<String> {
        return Observable.just(true).delay(2, TimeUnit.SECONDS).map { query }
    }

    private fun initActions() {

    }

    private fun initViews() {

    }


    companion object {
        val TAG: String = this::class.java.simpleName

        @JvmStatic
        fun newInstance() = SearchByRXFragment()
    }

}