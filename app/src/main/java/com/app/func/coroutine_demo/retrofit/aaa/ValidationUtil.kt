package com.app.func.coroutine_demo.retrofit.aaa

object ValidationUtil {

    fun isValidateMovies(movie: Movie) : Boolean {
        if (movie.name.isNotEmpty() && movie.category.isNotEmpty()) {
            return true
        }
        return false
    }
}