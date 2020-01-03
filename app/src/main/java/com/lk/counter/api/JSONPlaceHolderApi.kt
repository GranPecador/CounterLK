package com.lk.counter.api

import com.lk.counter.models.*
import retrofit2.Call
import retrofit2.http.*

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

    @Headers("Content-Type: application/json; charset=UTF-8")
    @POST("/lk/addresses")
    fun postAddress(@Header("Authorization") token:String,
                    @Body address:AddressPostApi) :Call<MessageApi>

    @Headers("Content-Type: application/json; charset=UTF-8")
    @POST("/lk/counters")
    fun postActivateCounter(@Header("Authorization") token:String,
                            @Body counter:CounterActivatePostApi):Call<MessageApi>

    @GET("/lk/addresses")
    fun getAddresses(@Header("Authorization") token:String):Call<AddressesListApi>

    @GET("/lk/rooms")
    fun getRooms(@Header("Authorization") token:String):Call<RoomsListApi>

    @GET("/lk/addresses/{id}")
    fun getCounters(@Header("Authorization") token:String,
                    @Path("id") addressId:Int):Call<CountersListApi>

    @GET("/lk/addresses/{idAddress}/{idCounter}")
    fun getCounter(@Header("Authorization") token:String,
                   @Path("idAddress") addressId:Int,
                   @Path("idCounter") counterId:Int):Call<CounterGetApi>

    @GET("/lk/ctypes")
    fun getCounterTypes(@Header("Authorization") token:String):Call<CounterTypesListApi>
}