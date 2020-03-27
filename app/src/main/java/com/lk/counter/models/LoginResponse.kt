package com.lk.counter.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class LoginResponse (@SerializedName("token") @Expose val token:String,
                          @SerializedName("role") val role:String = "user")