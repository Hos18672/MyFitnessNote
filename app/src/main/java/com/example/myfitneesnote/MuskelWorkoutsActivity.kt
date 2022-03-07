package com.example.myfitneesnote

import android.content.Intent
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_muskel_workouts.*


class MuskelWorkoutsActivity : BaseActivity() {
    private var gymType: String? = ""
    private var muskleName: String? = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_muskel_workouts)
        setupActionBar()
        gymType = intent.getStringExtra("GymName").toString()
        muskleName = intent.getStringExtra("MuskelName").toString()
    }

     fun  onClick(view : View){
         val intent = Intent( this,  AddWorkoutActivity::class.java)
         intent.putExtra("WorkoutName", view.contentDescription)
         intent.putExtra("MuskelName", muskleName)
         intent.putExtra("GymName", gymType)
         startActivity(intent)
    }

    private fun setupActionBar() {
        setSupportActionBar(toolBar_muskelWorkouts)
        val actionBar = supportActionBar
        if (actionBar != null) {
            supportActionBar?.setDisplayShowTitleEnabled(false)
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_navigate_before_black_24dp)
        }
        toolBar_muskelWorkouts.setNavigationOnClickListener {
            onBackPressed()
        }
    }
}