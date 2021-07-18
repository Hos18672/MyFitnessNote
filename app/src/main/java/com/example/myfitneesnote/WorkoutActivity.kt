package com.example.myfitneesnote

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.annotation.RequiresApi
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

            btn_gym_workout.animate().apply {
                duration =400
                scaleYBy(.2f)
                scaleXBy(.2f)
            }.withEndAction {
                btn_gym_workout.animate().apply {
                    duration = 400
                    scaleYBy(-.2f)
                    scaleXBy(-.2f)
                }
            }.start()
            var intent = Intent( this,  MuskelGroupActivity::class.java)
            intent.putExtra("WorkoutType",btn_gym_workout.text)
            startActivity(intent) }
        btn_home_workout.setOnClickListener{
            btn_home_workout.animate().apply {
                duration =400
                scaleYBy(.2f)
                scaleXBy(.2f)
            }.withEndAction {
                btn_home_workout.animate().apply {
                    duration = 400
                    scaleYBy(-.2f)
                    scaleXBy(-.2f)
                }
            }.start()
            var intent = Intent( this,  MuskelGroupActivity::class.java)
            intent.putExtra("WorkoutType",btn_home_workout.text)
            startActivity(intent)}
        btnBack1.setOnClickListener{ onBackPressed() }
    }
    private fun setupActionBar() {
        setSupportActionBar(toolBar_workout_activity)
        var actionBar = supportActionBar
        if(actionBar!=null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_navigate_before_black_24dp)
        }
        toolBar_workout_activity.setNavigationOnClickListener{
            onBackPressed()
        }
    }
}


