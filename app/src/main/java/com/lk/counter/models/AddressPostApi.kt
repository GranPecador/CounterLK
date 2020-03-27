package com.lk.counter.models

import com.google.gson.annotations.SerializedName

data class AddressPostApi(@SerializedName("user_id") val userLogin:String,
                          @SerializedName("country") val country:String,
                          @SerializedName("city") val city:String,
                          @SerializedName("street") val street:String,
                          @SerializedName("homeNumber") val homeNumber:Int,
                          @SerializedName("flat") val flat:Int)