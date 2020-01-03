package com.lk.counter.models

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class CounterGetApi(@SerializedName("id") val counterId:Int,
                         @SerializedName("serialNumber") val serialNumber:String,
                         @SerializedName("data") val data:Int,
                         @SerializedName("open") val open:Int,
                         @SerializedName("batt") val battery:Int,
                         @SerializedName("timestamp") val timestamp: String,
                         @SerializedName("roomName") val roomName:String,
                         @SerializedName("counterType") val counterTypeName: String):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {}

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(counterId)
        parcel.writeString(serialNumber)
        parcel.writeInt(data)
        parcel.writeInt(open)
        parcel.writeInt(battery)
        parcel.writeString(timestamp)
        parcel.writeString(roomName)
        parcel.writeString(counterTypeName)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CounterGetApi> {
        override fun createFromParcel(parcel: Parcel): CounterGetApi {
            return CounterGetApi(parcel)
        }

        override fun newArray(size: Int): Array<CounterGetApi?> {
            return arrayOfNulls(size)
        }
    }

}