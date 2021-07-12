package com.example.myfitneesnote

import android.content.Intent
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_muskel_group.*

class MuskelGroupActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_muskel_group)
        var GymType: String? = intent.getStringExtra("WorkoutType")
        muscle_txt.text = GymType
        fullscreen()
        setupActionBar()
        btnBack.setOnClickListener{onBackPressed()}

        brust_btn.setOnClickListener  {
           var intent = Intent (this, AddWorkoutActivity::class.java)
            intent.putExtra("MuskelName", brust_btn.text)
            intent.putExtra("GymName",GymType)
            startActivity(intent)
        }
        biceps_btn.setOnClickListener  {
            var intent = Intent (this, AddWorkoutActivity::class.java)
            intent.putExtra("MuskelName", biceps_btn.text)
            intent.putExtra("GymName", biceps_btn.text)
                startActivity(intent)
        }
    }
    private fun setupActionBar() {
        setSupportActionBar(toolBar_muscle_gruppe_activity)
        var actionBar = supportActionBar
        if(actionBar!=null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_navigate_before_black_24dp)
        }
        toolBar_muscle_gruppe_activity.setNavigationOnClickListener{
            onBackPressed()
        }
    }
}
