package com.example.myfitneesnote

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import android.view.animation.AnimationUtils
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityOptionsCompat
import kotlinx.android.synthetic.main.activity_workout.*

@Suppress("DEPRECATION")
class WorkoutsChoiceActivity : BaseActivity(){
    @SuppressLint("ResourceAsColor")
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_workout)
        window.navigationBarColor = android.R.color.white
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        //fullscreen()
        setupActionBar()
        onClick()
        animat()
    }

    private fun animat(){
        val btt = AnimationUtils.loadAnimation(this, R.anim.btt)
        btn_home_workout.startAnimation(btt)
        val ttb = AnimationUtils.loadAnimation(this, R.anim.ttb)
        btn_gym_workout.startAnimation(ttb)
        val ttb1 = AnimationUtils.loadAnimation(this, R.anim.ttb)
        tv_gymType.startAnimation(ttb1)
    }
    private  fun onClick(){
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
            val intent = Intent( this,  MuskelGroupActivity::class.java)
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, btn_gym_workout, "cvWorkouts")
            intent.putExtra("WorkoutType",txt_gym_workout.text)
            startActivity(intent, options.toBundle())
            //finish()
        }
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
            val intent = Intent( this,  MuskelGroupActivity::class.java)
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, btn_home_workout, "cvWorkouts")
            intent.putExtra("WorkoutType",txt_home_workout.text)
            startActivity(intent, options.toBundle())
            //finish()
        }


        btnBack1.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, btnBack1, "addBtn")
            startActivity(intent, options.toBundle())
            //finish()
        }
    }
    private fun setupActionBar() {
        setSupportActionBar(toolBar_workout_activity)
        val actionBar = supportActionBar
        if(actionBar!=null) {
            supportActionBar?.setDisplayShowTitleEnabled(false)
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_navigate_before_black_24dp)
        }
        toolBar_workout_activity.setNavigationOnClickListener{
            onBackPressed()
            finish()
        }
    }
}


