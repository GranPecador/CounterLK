package com.lk.counter.models

import com.google.gson.annotations.SerializedName

class AddressApi(@SerializedName("id") val addressId:Int,
                 @SerializedName("userId") val userId:Int,
                 @SerializedName("country") val country:String,
                 @SerializedName("city") val city:String,
                 @SerializedName("street") val street:String,
                 @SerializedName("homeNumber") val homeNumber:Int,
                 @SerializedName("flat") val flat:Int
                 )