package com.example.myfitneesnote

import android.animation.LayoutTransition
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.View
import android.view.WindowManager
import android.view.animation.AnimationUtils
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityOptionsCompat
import com.google.android.material.card.MaterialCardView
import kotlinx.android.synthetic.main.activity_workout_choice.*


@Suppress("DEPRECATION")
class WorkoutsChoiceActivity : BaseActivity(){

    private var gymType: String? = ""

    @SuppressLint("ResourceAsColor")
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_workout_choice)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        //fullscreen()
        animat()
        setupActionBar()
        onClick2()
    }

    private fun animat(){
        val btt = AnimationUtils.loadAnimation(this, R.anim.btt)
        btn_home_workout.startAnimation(btt)
        val ttb = AnimationUtils.loadAnimation(this, R.anim.ttb)
        cv_gym2.startAnimation(ttb)
        val ttb1 = AnimationUtils.loadAnimation(this, R.anim.ttb)
        tv_gymType.startAnimation(ttb1)

        llmain.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
    }
    private  fun onClick2(){
        cv_gym2.setOnClickListener{
                if (hiddenLayout.getVisibility() === View.VISIBLE) {
                    TransitionManager.beginDelayedTransition(cv_gym2, AutoTransition())
                    hiddenLayout.visibility = View.GONE
                    gym_arrow.setBackgroundResource(R.drawable.arrow_down)

                } else {
                    TransitionManager.beginDelayedTransition(cv_gym2, AutoTransition())
                    hiddenLayout.visibility = View.VISIBLE
                    hiddenLayout2.visibility = View.GONE
                    gym_arrow.setBackgroundResource(R.drawable.arrow_up)
                    home_arrow.setBackgroundResource(R.drawable.arrow_down)
                }
                gymType = txt_gym_workout.text.toString()

        }
        cv_home.setOnClickListener{
            if (hiddenLayout2.getVisibility() === View.VISIBLE) {
                TransitionManager.beginDelayedTransition(cv_home, AutoTransition())
                hiddenLayout2.visibility = View.GONE
                home_arrow.setBackgroundResource(R.drawable.arrow_down)

            } else {
                TransitionManager.beginDelayedTransition(cv_home, AutoTransition())
                hiddenLayout2.visibility = View.VISIBLE
                hiddenLayout.visibility = View.GONE
                home_arrow.setBackgroundResource(R.drawable.arrow_up)
                gym_arrow.setBackgroundResource(R.drawable.arrow_down)
            }
            gymType = txt_home_workout.text.toString()
        }
    }

    fun onclick(view: View){
        val intent = Intent( this,  MuskelWorkoutsActivity::class.java)
        intent.putExtra("MuskelName", view.contentDescription)
        intent.putExtra("GymName", gymType)
        startActivity(intent)
    }
    override  fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
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


