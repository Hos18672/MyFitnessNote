package com.example.myfitneesnote.activities



import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.myfitneesnote.R.*
import com.example.myfitneesnote.firebase.FirestoreClass
import com.example.myfitneesnote.fragments.AddWorkoutFragment
import com.example.myfitneesnote.model.Workout
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.vivekkaushik.datepicker.OnDateSelectedListener
import kotlinx.android.synthetic.main.activity_add_workout.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ThreadLocalRandom

class AddWorkoutActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var currentDate: String
    private var trainingsName: String? = ""
    private var gymType = ""
    var muskelName: String = ""
    var workoutName: String = ""
    var workout_num = ""
    private  var listWeightlessWorkout : ArrayList<String> = ArrayList()
    @SuppressLint("ResourceAsColor")
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_add_workout)
        onClick()
        setupActionBar()
        gymType = intent.getStringExtra("GymName").toString()
        muskelName = intent.getStringExtra("MuskelName").toString()
        workoutName = intent.getStringExtra("WorkoutName").toString()
        //  trainingsName = intent.getStringExtra("MuskelName")
        tv_muscle_name.text = muskelName
        tv_workout_name.text = workoutName
/*
        listWeightlessWorkout.add("Dumbbel fly")
        listWeightlessWorkout.add("Flat bench lying leg lift")
        listWeightlessWorkout.add("Side bridge")
        listWeightlessWorkout.add("Superman")
        listWeightlessWorkout.add("Leg lift")
        listWeightlessWorkout.add("Push Up")
        listWeightlessWorkout.add("rotating hip lift")*/

        listWeightlessWorkout.addAll(arrayListOf(
            "Dumbbel fly","Flat bench lying leg lift","Side bridge","Superman","Leg lift","Push Up","rotating hip lift",
            "Decline push-ups", "Incline push-ups","Plyometric push-ups", "slightly easier push-ups", "Walking plank",
            "Chin ups", "plank taps", "Pull ups","Reverse hand push-ups",
            "Bridge","High blank", "Low blank", "Quadruped limb raises", "Superman back extension",
            "Diamond push-ups", "Power triceps extension","Triceps bow", "triceps dips",
            "Flat bench lying leg lift", "Side bridge","Superman","Leg lift", "rotating hip lift",
            "Band pull aparts", "Decline push-ups", "Front schoulder raise with band", "Handstand push-ups", "Plank raise tap crunch",
            "Lunges", "Pistol squats", "squats","Squats jumps"))
        for (i in listWeightlessWorkout){
            if (workoutName == i){
                weight_ll.visibility = View.GONE
            }
        }

        //Set a Start date (Default, 1 Jan 1970)
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        datePickerTimeline.setInitialDate(year, month, day)
        currentDate = "${year}-${month + 1}-${day}"

        val fragment = AddWorkoutFragment.newInstance(muskelName, workoutName)
        supportFragmentManager.beginTransaction().apply {
            replace(id.root_container, fragment).commit()
        }
        datePickerTimeline.setOnDateSelectedListener(object : OnDateSelectedListener {
            override fun onDateSelected(year: Int, month: Int, day: Int, dayOfWeek: Int) {
                // Do Something
                currentDate = "${year}-${month+1}-${day}"
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

    private fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)
    private fun onClick() {
        btn_back_home.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finishAffinity()
        }
        save_btn.setOnClickListener {
            if (checkForInternet(this)) {
                createTraining()
                val fragment = AddWorkoutFragment.newInstance(muskelName, workoutName)
                supportFragmentManager.beginTransaction().apply {
                    replace(id.root_container, fragment).commit()
                }
            } else {
                Toast.makeText(this, "No internet connection!", Toast.LENGTH_SHORT).show()
            }
        }
        btn_set_minus.setOnClickListener { minusButton(SetNum) }
        btn_set_plus.setOnClickListener { plusButton(SetNum) }
        btn_weight_minus.setOnClickListener { minusButton(weightNum) }
        btn_weight_plus.setOnClickListener { plusButton(weightNum) }
        btn_break_minus.setOnClickListener { minusButton(breakNum) }
        btn_break_plus.setOnClickListener { plusButton(breakNum) }
        btn_repeat_minus.setOnClickListener { minusButton(repeatNum) }
        btn_repeat_plus.setOnClickListener { plusButton(repeatNum) }
    }

    private fun minusButton(et: EditText) {
        if (et.text.toString() != " " && et.text.toString() != "" ){
            var num = et.text.toString().toInt()
            if (num > 0) {
                num--
                et.text = "$num".toEditable()
            }
        }else
        {
            showErrorSnackBar("Fill the field!")
        }
    }

    private fun plusButton(et: EditText) {
        if (et.text.toString() != " " && et.text.toString() != ""){
            var num = et.text.toString().toInt()
            num++
            et.text = "$num".toEditable()
        }
        else
        {
            showErrorSnackBar("Fill the field!")
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun dateFormatter(date: String): Date {
        val inputFormatter: DateFormat = SimpleDateFormat("yyyy-M-d")
        return inputFormatter.parse(date)
    }

    private fun createTraining() {
        val mFirestore = FirebaseFirestore.getInstance()
        mFirestore.collection("users").document(getCurrentUserId()).get()
            .addOnSuccessListener { documentSnapshot ->
                var weight = documentSnapshot.getString("weight")
                if (weight == "0"){
                    weight = "0.01"
                }
                val workout: Workout
                val weight2 = weightNum.text.toString()
                val breakNum = breakNum.text.toString()
                val set = SetNum.text.toString()
                val rep = repeatNum.text.toString()
                val note = workoutNote.text.toString()
                if (validateForm(set, weight2, breakNum, rep)) {
                    val _random = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        ThreadLocalRandom.current().nextDouble(0.0, 0.9)
                    } else { TODO("VERSION.SDK_INT < LOLLIPOP") }
                    val random = 0.0 + _random
                    val s = 20 * set.toInt() * rep.toInt() * (2 * 3.0 + random * weight.toString().toInt())
                    val calories = s / 200
                    workout = Workout(getCurrentUserId(),gymType,muskelName,workoutName, set, weight2, breakNum,
                                        rep, currentDate, calories, dateFormatter(currentDate), note)
                    FirestoreClass().createNewTraining(this@AddWorkoutActivity, workout)
                    val recyclerView = findViewById<RecyclerView>(id.recyclerView_add)
                    recyclerView.scrollToPosition(0)
                    val fragment = AddWorkoutFragment.newInstance(muskelName, workoutName)
                    supportFragmentManager.beginTransaction().apply {
                        replace(id.root_container, fragment).commit()
                    }
                }
            }
    }

    private fun validateForm(set: String, weight: String, breakNum: String, repeat: String): Boolean {
        return when {
            TextUtils.isEmpty(set)  -> {
                showErrorSnackBar("Set is empty")
                false
            }
            TextUtils.isEmpty(weight) -> {
                showErrorSnackBar("Weight is empty")
                false
            }
            TextUtils.isEmpty(breakNum) -> {
                showErrorSnackBar("BreakTime is empty")
                false
            }
            TextUtils.isEmpty(repeat) -> {
                showErrorSnackBar("Repeat is empty")
                false
            }
            else -> {
                true
            }
        }
    }
    private fun getCurrentUserId(): String {
        val currentUser = FirebaseAuth.getInstance().currentUser
        var currentUserID = ""
        if (currentUser != null) {
            currentUserID = currentUser.uid
        }
        return currentUserID
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun setupActionBar() {
        setSupportActionBar(toolBar_add_workout_activity)
        val actionBar = supportActionBar
        if (actionBar != null) {
            supportActionBar?.setDisplayShowTitleEnabled(false)
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(drawable.ic_navigate_before_black_24dp)
        }
        toolBar_add_workout_activity.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        TODO("Not yet implemented")
    }



}
