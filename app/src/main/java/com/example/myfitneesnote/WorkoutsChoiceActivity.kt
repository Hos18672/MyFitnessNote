package com.example.myfitneesnote

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
import kotlinx.android.synthetic.main.activity_muskel_group.*
import kotlinx.android.synthetic.main.activity_workout.*
import kotlinx.android.synthetic.main.activity_workout.biceps_btn
import kotlinx.android.synthetic.main.activity_workout.brust_btn
import kotlinx.android.synthetic.main.activity_workout.forearms_btn
import kotlinx.android.synthetic.main.activity_workout.shoulder_btn
import kotlinx.android.synthetic.main.activity_workout.traps_btn
import kotlinx.android.synthetic.main.activity_workout.triceps_btn
import kotlinx.android.synthetic.main.activity_workout.upperabs_btn

@Suppress("DEPRECATION")
class WorkoutsChoiceActivity : BaseActivity(){

    private var gymType: String? = ""
    @SuppressLint("ResourceAsColor")
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_workout)
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
        cv_gym2.startAnimation(ttb)
        val ttb1 = AnimationUtils.loadAnimation(this, R.anim.ttb)
        tv_gymType.startAnimation(ttb1)
    }
    private  fun onClick(){
        cv_gym2.setOnClickListener{
                if (hiddenLayout.getVisibility() === View.VISIBLE) {
                    TransitionManager.beginDelayedTransition(btn_gym_workout, AutoTransition())
                    hiddenLayout.setVisibility(View.GONE)
                } else {
                    TransitionManager.beginDelayedTransition(btn_gym_workout, AutoTransition())
                    hiddenLayout.setVisibility(View.VISIBLE)
                }
                gymType = txt_gym_workout.text.toString()
        }
        cv_home.setOnClickListener{
            if (hiddenLayout2.getVisibility() === View.VISIBLE) {
                TransitionManager.beginDelayedTransition(btn_home_workout, AutoTransition())
                hiddenLayout2.setVisibility(View.GONE)
            } else {
                TransitionManager.beginDelayedTransition(btn_home_workout, AutoTransition())
                hiddenLayout2.setVisibility(View.VISIBLE)
            }
            gymType = txt_home_workout.text.toString()
        }


        //Burst muscle
        brust_btn2.setOnClickListener { setOnclickMuscle(brust_btn as MaterialCardView) }
        //Biceps muscle
        biceps_btn2.setOnClickListener  { setOnclickMuscle(biceps_btn as MaterialCardView) }
        //Triceps muscle
        triceps_btn2.setOnClickListener { setOnclickMuscle(triceps_btn as MaterialCardView) }
        //Forarms muscle
        forearms_btn2.setOnClickListener{ setOnclickMuscle(forearms_btn as MaterialCardView) }
        //Traps muscle
        traps_btn2.setOnClickListener   { setOnclickMuscle(traps_btn as MaterialCardView) }
        //Upper-abs muscle
        upperabs_btn2.setOnClickListener{ setOnclickMuscle(upperabs_btn as MaterialCardView) }
        //shoulder muscle
        shoulder_btn2.setOnClickListener{ setOnclickMuscle(shoulder_btn as CardView) }

       // ======================= Home ============================================
        //Burst muscle
        brust_btn.setOnClickListener { setOnclickMuscle(brust_btn as MaterialCardView) }
        //Biceps muscle
        biceps_btn.setOnClickListener  { setOnclickMuscle(biceps_btn as MaterialCardView) }
        //Triceps muscle
        triceps_btn.setOnClickListener { setOnclickMuscle(triceps_btn as MaterialCardView) }
        //Forarms muscle
        forearms_btn.setOnClickListener{ setOnclickMuscle(forearms_btn as MaterialCardView) }
        //Traps muscle
        traps_btn.setOnClickListener   { setOnclickMuscle(traps_btn as MaterialCardView) }
        //Upper-abs muscle
        upperabs_btn.setOnClickListener{ setOnclickMuscle(upperabs_btn as MaterialCardView) }
        //shoulder muscle
        shoulder_btn.setOnClickListener{ setOnclickMuscle(shoulder_btn as CardView) }


    }


    private  fun setOnclickMuscle(btn: CardView){
        val intent = Intent( this,  AddWorkoutActivity::class.java)
        intent.putExtra("MuskelName", btn.contentDescription)
        intent.putExtra("GymName", gymType)

        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, btn, "addTrainingCv")
        startActivity(intent, options.toBundle())



        // finish()
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


