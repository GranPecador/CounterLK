package com.lk.counter.models

import com.google.gson.annotations.SerializedName

data class CounterActivatePostApi (@SerializedName("userLogin") val login:String,
                                   @SerializedName("room") val idRoom:Int,
                                   @SerializedName("serialNumber") val serialNumber:String,
                                   @SerializedName("address") val idAddress:Int,
                                   @SerializedName("counterType") val idCounter:Int)