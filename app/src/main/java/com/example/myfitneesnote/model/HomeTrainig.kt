package com.example.myfitneesnote.model

import android.os.Parcel
import android.os.Parcelable

class HomeTrainig(id: Int,
                           name: String?,
                           type: String?,
                           descript: String?) : Training(id, name, type, descript), Parcelable {

    override fun compareTo(other: Training): Int {
        TODO("Not yet implemented")
    }

     override fun describeContents(): Int {
         TODO("Not yet implemented")
     }

     override fun writeToParcel(dest: Parcel?, flags: Int) {
         TODO("Not yet implemented")
     }

     override fun toString(): String {
         return super.toString()
     }
 }