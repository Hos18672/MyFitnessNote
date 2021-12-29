package com.example.myfitneesnote.model

import java.util.*

data class Workout(
    var workout_id: String = "",
    var GymType: String= "",
    var MuskelName: String= "",
    var set: String= "",
    var weight:String= "",
    var BreakTime: String= "",
    var repeat: String= "",
    var currentDateTime: String= "",
    var calorie: Double = 0.0,
    var date: Date? = null,
    var note : String = "",
    var expandable : Boolean = false)


