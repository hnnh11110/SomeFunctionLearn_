package com.app.func.login_demo

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.func.coroutine_demo.retrofit.aaa.DataRepository
import com.app.func.view.recycler_view_custom.ravi_recyclerview.ItemCart
import kotlinx.coroutines.*

class SignUpViewModel(private val repository: DataRepository) : ViewModel() {

    val errorMessage = MutableLiveData<String>()
    val menuFoodList = MutableLiveData<List<ItemCart>>()
    var job: Job? = null
    val loading = MutableLiveData<Boolean>()

    private val exceptionHandler =
        CoroutineExceptionHandler { _, throwable ->
            onError("Exception handled: ${throwable.localizedMessage}")
        }
    /*
    fun getAllMovies() {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            loading.postValue(true)
            val response = repository.getAllMovies()
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    movieList.postValue(response.body())
                    loading.value = false
                } else {
                    onError("Error : ${response.message()} ")
                }
            }
        }
    }
     */

    fun getListMenuFood() {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            loading.postValue(true)
            val response = repository.getMenuFood()
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    menuFoodList.postValue(response.body())
                    loading.value = false
                } else {
                    onError("Error -------- ${response.message()}")
                }
            }
        }
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