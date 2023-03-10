package com.app.func.coroutine_demo.retrofit.aaa

import com.app.func.coroutine_demo.data.model.QuoteListResponse
import com.app.func.coroutine_demo.retrofit.base.RetrofitService
import com.app.func.view.recycler_view_custom.ravi_recyclerview.ItemCart
import retrofit2.Response

class DataRepository constructor(private val retrofitService: RetrofitService) {

    suspend fun getAllMovies(): Response<List<Movie>> = retrofitService.getAllMovies()

    suspend fun getMenuFood(): Response<List<ItemCart>> = retrofitService.getMenuFood()

    suspend fun getAllQuotes(): Response<QuoteListResponse> = retrofitService.getQuotes()

}