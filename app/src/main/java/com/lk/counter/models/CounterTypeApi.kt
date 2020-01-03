package com.lk.counter.models

import com.google.gson.annotations.SerializedName

data class CounterTypeApi(@SerializedName("id") val typeId: Int,
                          @SerializedName("name") val name:String)