package com.example.myfitneesnote.activities

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
import com.example.myfitneesnote.R
import kotlinx.android.synthetic.main.activity_workout_choice.*


@Suppress("DEPRECATION")
class WorkoutsChoiceActivity : BaseActivity(){

    private var gymType: String? = ""
    private  var listOfWorkouts : HashMap<String, ArrayList<String>> = hashMapOf()
    private  var listOfChestWorkouts : ArrayList<String> = arrayListOf()
    private  var listOfBicepsWorkouts : ArrayList<String> = arrayListOf()
    private  var listOfBackWorkouts: ArrayList<String> = arrayListOf()
    private  var listOfForarmsWorkouts : ArrayList<String> = arrayListOf()
    private  var listOfAbsWorkouts: ArrayList<String> = arrayListOf()
    private  var listOfSchoulderWorkouts : ArrayList<String> = arrayListOf()
    private  var listOfTricepsWorkouts : ArrayList<String> = arrayListOf()
    private  var listOfLegWorkouts : ArrayList<String> = arrayListOf()

    private  var list : ArrayList<String> = arrayListOf()
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

    private fun setlist(name : String){
        if (name == "GYM"){
            listOfChestWorkouts = arrayListOf("Lang Barbell exercises","Bench press", "Inclined Bench Press", "Parallel bar flexion", "Standing Cable Fly", "Dumbbell fly", "Push Up","Decline push-ups", "Incline push-ups","Plyometric push-ups", "slightly easier push-ups", "Walking plank")
            listOfBicepsWorkouts = arrayListOf("Hammer exercise","Short dumbbell", "Cable bicep exercise","Chin ups", "plank taps", "Reverse hand push-ups")
            listOfBackWorkouts = arrayListOf("Pull ups","Cross lift", "Lat pull", "Barbell shrug","Dumbbell shrug", "Bridge","High blank", "Low blank", "Quadruped limb raises", "Superman back extension")
            listOfForarmsWorkouts = arrayListOf("Wrist exercise", "Wrist exercise", "Wrist exercise standing", "Dumbbell wrist exercise")
            listOfTricepsWorkouts = arrayListOf("Bench press with close grip one", "Dumbbel Overhead", "tricep extensions", "one-arm triceps extensions","Plank Up-Down","Diamond push-ups", "Power triceps extension","Triceps bow", "triceps dips")
            listOfAbsWorkouts = arrayListOf("Dumbbell side tilt", "Flat bench lying leg lift", "Side bridge","Superman","Leg lift", "rotating hip lift" )
            listOfSchoulderWorkouts = arrayListOf("Back Press", "Seated single column press", "Side lift","Front lift", "Barbell front lift",
                "Mitlitärpress behind neck","Band pull aparts", "Decline push-ups", "Front schoulder raise with band", "Handstand push-ups", "Plank raise tap crunch")
            listOfLegWorkouts = arrayListOf("Dumbbell exercises","squats", "Dumbbell drop steps", "Dumbbell squats","Hack squats", "Barbell step", "Good morning","Lunges", "Pistol squats","Squats jumps")
            listOfWorkouts= hashMapOf()
            listOfWorkouts["Chest"] = listOfChestWorkouts
            listOfWorkouts["Biceps"] = listOfBicepsWorkouts
            listOfWorkouts["Back"] = listOfBackWorkouts
            listOfWorkouts["Forearms"] = listOfForarmsWorkouts
            listOfWorkouts["Abs"] = listOfAbsWorkouts
            listOfWorkouts["Shoulder"] = listOfSchoulderWorkouts
            listOfWorkouts["Triceps"] = listOfTricepsWorkouts
            listOfWorkouts["Leg"] = listOfLegWorkouts
        }else if (name == "HOME"){
            listOfChestWorkouts = arrayListOf("Decline push-ups", "Incline push-ups","Plyometric push-ups", "slightly easier push-ups", "Walking plank")
            listOfBicepsWorkouts = arrayListOf("Chin ups", "plank taps", "Pull ups","Reverse hand push-ups")
            listOfBackWorkouts = arrayListOf("Bridge","High blank", "Low blank", "Quadruped limb raises", "Superman back extension")
            listOfTricepsWorkouts = arrayListOf("Diamond push-ups", "Power triceps extension","Triceps bow", "triceps dips")
            listOfAbsWorkouts = arrayListOf("Flat bench lying leg lift", "Side bridge","Superman","Leg lift", "rotating hip lift" )
            listOfSchoulderWorkouts = arrayListOf("Band pull aparts", "Decline push-ups", "Front schoulder raise with band", "Handstand push-ups", "Plank raise tap crunch")
            listOfLegWorkouts = arrayListOf("Lunges", "Pistol squats", "squats","Squats jumps")
            listOfWorkouts= hashMapOf()
            listOfWorkouts["Chest"] = listOfChestWorkouts
            listOfWorkouts["Biceps"] = listOfBicepsWorkouts
            listOfWorkouts["Back"] = listOfBackWorkouts
            listOfWorkouts["Abs"] = listOfAbsWorkouts
            listOfWorkouts["Shoulder"] = listOfSchoulderWorkouts
            listOfWorkouts["Triceps"] = listOfTricepsWorkouts
            listOfWorkouts["Leg"] = listOfLegWorkouts
        }
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
                gymType = "GYM"
             setlist("GYM")

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
            gymType = "HOME"
            setlist( "HOME")
        }
    }

    fun onclick(view: View){
        val intent = Intent( this,  MuskelWorkoutsActivity::class.java)
        intent.putExtra("MuskelName", view.contentDescription)
        intent.putExtra("GymName", gymType)

        for (i in listOfWorkouts.keys){
            if (i == "Chest" && view.contentDescription == "CHEST" ){
                list = listOfChestWorkouts
            }
            if (i == "Biceps" && view.contentDescription == "BICEPS" ){
                list = listOfBicepsWorkouts
            }
            if (i == "Triceps" && view.contentDescription == "TRICEPS" ){
                list = listOfTricepsWorkouts
            }
            if (i == "Forearms" && view.contentDescription == "FOREARMS" ){
                list = listOfForarmsWorkouts
            }
            if (i == "Back" && view.contentDescription == "BACK" ){
                list = listOfBackWorkouts
            }
            if (i == "Abs" && view.contentDescription == "ABS" ){
                list = listOfAbsWorkouts
            }
            if (i == "Shoulder" && view.contentDescription == "SHOULDER" ){
                list = listOfSchoulderWorkouts
            }
            if (i == "Leg" && view.contentDescription == "LEG" ){
                list = listOfLegWorkouts
            }
        }
        intent.putExtra("listOfWorkouts",list)

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


