package com.example.myfitneesnote
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import kotlinx.android.synthetic.main.activity_muskel_group.*
import kotlinx.android.synthetic.main.activity_security.*
import kotlinx.android.synthetic.main.activity_workout.*

class WorkoutActivity : BaseActivity(){

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_workout)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        fullscreen()
        setupActionBar()
        btn_gym_workout.setOnClickListener{
            var intent = Intent( this,  MuskelGroupActivity::class.java)
            intent.putExtra("WorkoutType",btn_gym_workout.text)
            startActivity(intent) }

        btn_home_workout.setOnClickListener{
            var intent = Intent( this,  MuskelGroupActivity::class.java)
            intent.putExtra("WorkoutType",btn_home_workout.text)
            startActivity(intent)}
        btnBack1.setOnClickListener{ onBackPressed() }
    }


    private fun setupActionBar() {
        setSupportActionBar(toolBar_workout_activity)
        var actionBar = supportActionBar
        if(actionBar!=null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_navigate_before_black_24dp)
        }
        toolBar_workout_activity.setNavigationOnClickListener{
            onBackPressed()
        }
    }

}


