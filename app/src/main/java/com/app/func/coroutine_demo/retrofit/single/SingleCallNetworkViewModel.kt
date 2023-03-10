package com.app.func.coroutine_demo.retrofit.single

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.func.coroutine_demo.data.model.QuoteListResponse
import com.app.func.coroutine_demo.retrofit.base.ApiConstants
import com.app.func.coroutine_demo.retrofit.base.RetrofitService
import com.app.func.coroutine_demo.retrofit.base.RetrofitObject
import kotlinx.coroutines.*
import retrofit2.Response

class SingleCallNetworkViewModel : ViewModel() {

    private val api =
        RetrofitObject.getRetrofit(ApiConstants.BASE_URL_QUOTE).create(RetrofitService::class.java)
    private val quotes = MutableLiveData<Response<QuoteListResponse>>()
    val errorMessage = MutableLiveData<String>()
    var job: Job? = null
    val loading = MutableLiveData<Boolean>()
    private val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        onError("Exception handled: ${throwable.localizedMessage}")
    }

    val quoteList: MutableLiveData<Response<QuoteListResponse>> = quotes

    fun getQuotes() {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            loading.postValue(true)
            val response =  api.getQuotes()
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
//                    movieList.postValue(response.body())
//                    quotes.postValue(response)
                    quotes.value = api.getQuotes()
                    loading.value = false
                } else {
                    onError("Error : ${response.message()} ")
                }
            }
        }
//        viewModelScope.launch {
//            quotes.value = api.getQuotes()
//        }
    }

    private fun onError(message: String) {
        errorMessage.value = message
        loading.value = false
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }
}