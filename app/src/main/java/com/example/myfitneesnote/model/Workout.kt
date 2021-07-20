package com.example.myfitneesnote.model


import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.Timestamp


 data class Workout( var workout_id: String = "",
                         var user_id: String= "",
                         var GymType: String= "",
                         var MuskelName: String= "",
                         var set: String= "",
                         var weight:String= "",
                         var BreakTime: String= "",
                         var repeat: String= "",
                         var currentDateTime: String= "",
                         var date: Timestamp? = null ) : Parcelable {
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
         parcel.readParcelable(Timestamp::class.java.classLoader)
     ) {
     }

     override fun writeToParcel(parcel: Parcel, flags: Int) {
         parcel.writeString(workout_id)
         parcel.writeString(user_id)
         parcel.writeString(GymType)
         parcel.writeString(MuskelName)
         parcel.writeString(set)
         parcel.writeString(weight)
         parcel.writeString(BreakTime)
         parcel.writeString(repeat)
         parcel.writeString(currentDateTime)
         parcel.writeParcelable(date, flags)
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

