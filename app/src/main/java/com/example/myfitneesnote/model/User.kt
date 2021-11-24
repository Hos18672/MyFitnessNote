package com.example.myfitneesnote.model

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable
data class User(
    val user_id: String = "",
    val name: String="",
    val username: String="",
    val email: String="",
    val password: String="",
    val image: String="",
    val age: String="",
    val height: String="",
    val weight: String="",
    val gender: String="",
    val firstWorkoutIsCreated: Boolean = false) : Parcelable {
    @SuppressLint("NewApi")
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readBoolean()!!
    )
    @SuppressLint("NewApi")
    override fun writeToParcel(parcel: Parcel, flags: Int) = with(parcel) {
        writeString(user_id)
        writeString(name)
        writeString(username)
        writeString(email)
        writeString(password)
        writeString(image)
        writeString(age)
        writeString(height)
        writeString(weight)
        writeString(gender)
        writeBoolean(firstWorkoutIsCreated)
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
