package com.example.myfitneesnote

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Vibrator
import android.view.MotionEvent
import android.view.View
import android.widget.ScrollView
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import kotlinx.android.synthetic.main.activity_muskel_group.*
import kotlinx.android.synthetic.main.activity_training.*

class MuskelGroupActivity : BaseActivity() {
    private var GymType: String? = ""
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_muskel_group)
        window.navigationBarColor = android.R.color.white
        GymType= intent.getStringExtra("WorkoutType")
        muscle_txt.text = GymType
        //fullscreen()
        setupActionBar()
        btnBack.setOnClickListener{
            val intent = Intent( this,  WorkoutsChoiceActivity::class.java)
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, btnBack, "cvWorkouts")
            startActivity(intent, options.toBundle())
          //  finish()
            }
        //Burst muscle
        brust_btn.setOnClickListener {
            setOnclickMuscle(brust_btn as MaterialCardView)

            @Override
            fun onTouchEvent(v: View, event: MotionEvent): Boolean {
                var vb: Vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                vb.vibrate(100)
                return false
            }
        }

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
        intent.putExtra("GymName", GymType)

        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, btn, "addTrainingCv")
        startActivity(intent, options.toBundle())


       // finish()
    }
    private fun setupActionBar() {
        setSupportActionBar(toolBar_muscle_gruppe_activity)
        val actionBar = supportActionBar
        if(actionBar!=null)
        {
            supportActionBar?.setDisplayShowTitleEnabled(false)
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_navigate_before_black_24dp)
        }
        toolBar_muscle_gruppe_activity.setNavigationOnClickListener{
            onBackPressed()
        }
    }
}
