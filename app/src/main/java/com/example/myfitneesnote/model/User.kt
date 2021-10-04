package com.example.myfitneesnote.model

import android.os.Parcel
import android.os.Parcelable
data class User(
    val user_id: String = "",
    val name: String="",
    val username: String="",
    val email: String="",
    val password: String="",
    val image: String="") : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    )
    override fun writeToParcel(parcel: Parcel, flags: Int) = with(parcel) {
        writeString(user_id)
        writeString(name)
        writeString(username)
        writeString(email)
        writeString(password)
        writeString(image)
    }
    override fun describeContents(): Int {
        return 0
    }
    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }
}
