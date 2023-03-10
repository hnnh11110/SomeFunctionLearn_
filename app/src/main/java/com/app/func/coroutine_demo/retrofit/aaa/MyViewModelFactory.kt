package com.app.func.coroutine_demo.retrofit.aaa

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.app.func.login_demo.SignUpViewModel

class MyViewModelFactory constructor(private val repository: DataRepository) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(ListMovieViewModel::class.java) -> {
                ListMovieViewModel(this.repository) as T
            }
            modelClass.isAssignableFrom(SignUpViewModel::class.java) -> {
                SignUpViewModel(this.repository) as T
            }
            else -> {
                throw IllegalArgumentException("ViewModel Not Found")
            }
        }
    }
}