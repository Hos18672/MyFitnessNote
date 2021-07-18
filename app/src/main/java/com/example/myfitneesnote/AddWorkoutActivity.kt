package com.example.myfitneesnote


import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.myfitneesnote.firebase.FirestoreClass
import com.example.myfitneesnote.model.Workout
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_add_workout.*
import kotlinx.android.synthetic.main.item_training.*
import java.sql.Timestamp
import java.time.Instant
import java.time.Instant.now
import java.time.LocalDate
import java.time.LocalDateTime.now
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*

class AddWorkoutActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_workout)
        onClick()
        fullscreen()
        setupActionBar()
        var trainingsName: String? = intent.getStringExtra("MuskelName")
        TrainingName.text = trainingsName

    }
    private fun String.toEditable(): Editable =  Editable.Factory.getInstance().newEditable(this)
    private fun onClick(){
        btnBackNewWorkout.setOnClickListener {onBackPressed()}

        btn_set_minus.setOnClickListener{minusButton(SetNum)}
        btn_set_plus.setOnClickListener{plusButton(SetNum)}

        btn_weight_minus.setOnClickListener{minusButton(weightNum)}
        btn_weight_plus.setOnClickListener{plusButton(weightNum)}

        btn_break_minus.setOnClickListener{minusButton(breakNum)}
        btn_break_plus.setOnClickListener{plusButton(breakNum)}

        btn_repeat_minus.setOnClickListener{minusButton(repeatNum)}
        btn_repeat_plus.setOnClickListener{plusButton(repeatNum)}

        save_btn.setOnClickListener { createTraining() }
    }
    private fun minusButton(et: EditText){
        var num = et.text.toString().toInt()
        num--
        et.text= "${num}".toEditable()
    }
    private fun plusButton(et: EditText){
        var num = et.text.toString().toInt()
        num++
        et.text= "${num}".toEditable()
    }
    private fun createTraining(){
        val c= Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)
        var currentDate : String = "${year}/${month}/${day} - ${hour}:${minute}"

        var workout : Workout
        val user   = FirebaseAuth.getInstance().currentUser
        var userId = user?.uid
        var gymType    : String = intent.getStringExtra("GymName").toString()
        var muskelName : String = intent.getStringExtra("MuskelName").toString()
        workout= Workout(
            userId.toString(),
            gymType,
            muskelName,
            SetNum.text.toString(),
            weightNum.text.toString(),
            breakNum.text.toString(),
            repeatNum.text.toString(),
            currentDate)
        FirestoreClass().createNewTraining(this, workout)
       Toast.makeText(
            this,
               "User:   ${userId}\n"+
                    "Gym:    ${gymType}\n" +
                    "Set:    ${SetNum.text}\n" +
                    "Weight: ${weightNum.text}\n" +
                    "Break:  ${breakNum.text}\n" +
                    "Repeat: ${repeatNum.text}",
            Toast.LENGTH_LONG
        ).show()

        startActivity(Intent(this, TrainingActivity::class.java))
    }
    private fun setupActionBar() {
        setSupportActionBar(toolBar_add_workout_activity)
        var actionBar = supportActionBar
        if(actionBar!=null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_navigate_before_black_24dp)
        }
        toolBar_add_workout_activity.setNavigationOnClickListener{
            onBackPressed()
        }
    }
}
