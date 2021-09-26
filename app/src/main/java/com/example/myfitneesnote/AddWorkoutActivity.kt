package com.example.myfitneesnote


import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.view.animation.AnimationUtils
import android.widget.EditText
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityOptionsCompat
import com.example.myfitneesnote.R.*
import com.example.myfitneesnote.firebase.FirestoreClass
import com.example.myfitneesnote.model.Workout
import com.google.firebase.Timestamp
import com.vivekkaushik.datepicker.OnDateSelectedListener
import kotlinx.android.synthetic.main.activity_add_workout.*
import kotlinx.android.synthetic.main.activity_main_layout.*
import java.util.*

class AddWorkoutActivity : BaseActivity() {
    var uuid : UUID= UUID.randomUUID()
    private lateinit var currentDate :String
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_add_workout)
        onClick()
        animat()
        fullscreen()
        setupActionBar()
        val trainingsName: String? = intent.getStringExtra("MuskelName")
        TrainingName.text = trainingsName
        // Set a Start date (Default, 1 Jan 1970)
        val c= Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        datePickerTimeline.setInitialDate(year,month,day)
        currentDate = "${year}/${month+1}/${day}"
        // datePickerTimeline.setActiveDate(Calendar.getInstance())
        // Set a date Selected Listener
        datePickerTimeline.setOnDateSelectedListener(object : OnDateSelectedListener {
            override fun onDateSelected(year: Int, month: Int, day: Int, dayOfWeek: Int) {
                // Do Something
                 currentDate = "${year}/${month+1}/${day}"
            }
            override fun onDisabledDateSelected(
                year: Int,
                month: Int,
                day: Int,
                dayOfWeek: Int,
                isDisabled: Boolean
            ) {
               // datePickerTimeline.setInitialDate(Calendar.YEAR, Calendar.MONTH,Calendar.DATE)
              //  currentDate = "${year}/${month+1}/${day}"
            }
        })
        // Disable date
        datePickerTimeline.setDisabledDateColor(Color.BLUE)
        val dates = arrayOf(Calendar.getInstance().time)
        datePickerTimeline.deactivateDates(dates)
    }
    private fun String.toEditable(): Editable =  Editable.Factory.getInstance().newEditable(this)


    fun animat(){
        val ttb = AnimationUtils.loadAnimation(this, R.anim.ttb)
        TrainingName.startAnimation(ttb)

    }
    private fun onClick(){
        btnBackNewWorkout.setOnClickListener {
            val intent = Intent( this,  MuskelGroupActivity::class.java)
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, btnBackNewWorkout, "addTrainingBtn")
            startActivity(intent, options.toBundle())
           // finish()

        }
        btn_set_minus.setOnClickListener{minusButton(SetNum)}
        btn_set_plus.setOnClickListener{plusButton(SetNum)}
        btn_weight_minus.setOnClickListener{minusButton(weightNum)}
        btn_weight_plus.setOnClickListener{plusButton(weightNum)}
        btn_break_minus.setOnClickListener{minusButton(breakNum)}
        btn_break_plus.setOnClickListener{plusButton(breakNum)}
        btn_repeat_minus.setOnClickListener{minusButton(repeatNum)}
        btn_repeat_plus.setOnClickListener{plusButton(repeatNum)}
        save_btn.setOnClickListener {

            createTraining()
            val intent = Intent(this, TrainingActivity::class.java)
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, save_btn, "trainingsBtn")
            startActivity(intent, options.toBundle())
            finish()}
    }
    private fun minusButton(et: EditText){
        var num = et.text.toString().toInt()
        if(num  > 0 ) {
            num--
            et.text = "$num".toEditable()
        }
    }
    private fun plusButton(et: EditText){
        var num = et.text.toString().toInt()
        num++
        et.text = "$num".toEditable()
    }
    private fun createTraining(){
        val currentDateAndTime = Timestamp.now()
        val workout : Workout
        val gymType    : String = intent.getStringExtra("GymName").toString()
        val muskelName : String = intent.getStringExtra("MuskelName").toString()
        workout= Workout(
            uuid.toString(),
            gymType,
            muskelName,
            SetNum.text.toString(),
            weightNum.text.toString(),
            breakNum.text.toString(),
            repeatNum.text.toString(),
            currentDate,
            currentDateAndTime)
        FirestoreClass().createNewTraining(this, workout)
    }
    private fun setupActionBar() {
        setSupportActionBar(toolBar_add_workout_activity)
        val actionBar = supportActionBar
        if(actionBar!=null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(drawable.ic_navigate_before_black_24dp)
        }
        toolBar_add_workout_activity.setNavigationOnClickListener{
            onBackPressed()
        }
    }
}
