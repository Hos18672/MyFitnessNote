package com.example.myfitneesnote.model

import android.os.Parcel
import android.os.Parcelable

data class Workout(
    val user_id: String= "",
    var GymType: String= "",
    var MuskelName: String= "",
    var set: String= "",
    var weight:String= "",
    var Break: String= "",
    var repeat: String= "",)
    :Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    ) {
    }
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(user_id)
        parcel.writeString(GymType)
        parcel.writeString(MuskelName)
        parcel.writeString(set)
        parcel.writeString(weight)
        parcel.writeString(Break)
        parcel.writeString(repeat)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Workout> {
        override fun createFromParcel(parcel: Parcel): Workout {
            return Workout(parcel)
        }

        override fun newArray(size: Int): Array<Workout?> {
            return arrayOfNulls(size)
        }
    }
}


