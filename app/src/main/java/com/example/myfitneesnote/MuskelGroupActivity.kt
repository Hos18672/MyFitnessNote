package com.example.myfitneesnote

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import kotlinx.android.synthetic.main.activity_muskel_group.*

class MuskelGroupActivity : BaseActivity() {
    var GymType: String? = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_muskel_group)
        GymType= intent.getStringExtra("WorkoutType")
        muscle_txt.text = GymType
        fullscreen()
        setupActionBar()
        btnBack.setOnClickListener{onBackPressed()}
        //Burst muscle
        brust_btn.setOnClickListener   { setOnclickMuscle(brust_btn) }
        //Biceps muscle
        biceps_btn.setOnClickListener  { setOnclickMuscle(biceps_btn) }
        //Triceps muscle
        triceps_btn.setOnClickListener { setOnclickMuscle(triceps_btn) }
        //Forarms muscle
        forearms_btn.setOnClickListener{ setOnclickMuscle(forearms_btn) }
        //Traps muscle
        traps_btn.setOnClickListener   { setOnclickMuscle(traps_btn) }
        //Upper-abs muscle
        upperabs_btn.setOnClickListener{ setOnclickMuscle(upperabs_btn) }
        //Lower-abs muscle
        lowerabs_btn.setOnClickListener{ setOnclickMuscle(lowerabs_btn) }
        //shoulder muscle
        shoulder_btn.setOnClickListener{ setOnclickMuscle(shoulder_btn) }
    }

    private  fun setOnclickMuscle(btn : Button){
        val intent = Intent (this, AddWorkoutActivity::class.java)
        intent.putExtra("MuskelName", btn.text)
        intent.putExtra("GymName", GymType)
        startActivity(intent)
    }
    private fun setupActionBar() {
        setSupportActionBar(toolBar_muscle_gruppe_activity)
        val actionBar = supportActionBar
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
