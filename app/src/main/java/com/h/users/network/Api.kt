package com.h.users.network

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query


interface Api {
    @GET("users")
    fun getUsers(@Query("page") page : Int) : Single<ApiResponse>
}