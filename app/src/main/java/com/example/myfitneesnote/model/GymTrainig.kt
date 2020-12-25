package com.example.myfitneesnote.model

class GymTrainig(id: Int,
                           name: String?,
                           type: String?,
                           descript: String?) : Training(id, name, type, descript) {
     override  fun compareTo(other: Training): Int {
         TODO("Not yet implemented")
     }

      override fun toString(): String {
          return super.toString()
      }
  }
