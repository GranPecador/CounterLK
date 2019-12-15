package com.lk.counter.api

import com.lk.counter.models.LoginResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface JSONPlaceHolderApi {
    @FormUrlEncoded
    @POST("/login")
    fun postLogin(@Field("login") login:String,
                  @Field("password") password:String): Call<LoginResponse>

    @FormUrlEncoded
    @POST("/register")
    fun postRegister(@Field("login") login:String,
                     @Field("password") password:String,
                     @Field("email") email:String,
                     @Field("telephone") telephone:String,
                     @Field("name") name:String,
                     @Field("surname") surname:String): Call<LoginResponse>
}