package com.example.myfitneesnote


import android.os.Bundle
import kotlinx.android.synthetic.main.activity_add_workout.*

class AddWorkoutActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_workout)

        btnBackNewWorkout.setOnClickListener { onBackPressed() }
        var Muskel: String? = intent.getStringExtra("MuskelName")
        WorkoutName.text = Muskel

    }
}
