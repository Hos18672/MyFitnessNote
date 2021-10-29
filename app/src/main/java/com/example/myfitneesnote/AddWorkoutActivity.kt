package com.example.myfitneesnote


import android.content.Intent
import android.graphics.Color

import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.MenuItem
import android.widget.EditText
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.myfitneesnote.R.*
import com.example.myfitneesnote.firebase.FirestoreClass
import com.example.myfitneesnote.model.Workout
import com.example.myfitneesnote.utils.Constant
import com.google.android.material.navigation.NavigationView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.vivekkaushik.datepicker.OnDateSelectedListener
import kotlinx.android.synthetic.main.activity_add_workout.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ThreadLocalRandom


class AddWorkoutActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {
    var uuid: UUID = UUID.randomUUID()
    private lateinit var currentDate: String
    private var trainingsName: String? = ""
    private val db = FirebaseFirestore.getInstance()

    var createdWorkout = false
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_add_workout)
        onClick()
        setupActionBar()
        trainingsName = intent.getStringExtra("MuskelName")
        TrainingName.text = trainingsName
        //Set a Start date (Default, 1 Jan 1970)
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        datePickerTimeline.setInitialDate(year, month, day)
        currentDate = "${year}-${month + 1}-${day}"
        val trainingsFragment = trainings_fragment()
        supportFragmentManager.beginTransaction().apply {
            replace(id.root_container, trainingsFragment).commit()
        }
        datePickerTimeline.setOnDateSelectedListener(object : OnDateSelectedListener {
            override fun onDateSelected(year: Int, month: Int, day: Int, dayOfWeek: Int) {
                // Do Something
                currentDate = "${year}-${month + 1}-${day}"
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
        val trainingsFragment = trainings_fragment()

        btnBackNewWorkout.setOnClickListener {
            val intent = Intent(this, MuskelGroupActivity::class.java)
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this,
                btnBackNewWorkout,
                "addTrainingBtn"
            )
            startActivity(intent, options.toBundle())
        }
        btn_back_home.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        save_btn.setOnClickListener {
            createTraining()
  /*          supportFragmentManager.beginTransaction().apply {
                replace(id.root_container, trainingsFragment).commit()
            }*/
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
        var num = et.text.toString().toInt()
        if (num > 0) {
            num--
            et.text = "$num".toEditable()
        }
    }

    private fun plusButton(et: EditText) {
        var num = et.text.toString().toInt()
        num++
        et.text = "$num".toEditable()
    }

    private fun dateFormatter(date: String): Date {
        val inputFormatter: DateFormat = SimpleDateFormat("yyyy-MM-dd")
        return inputFormatter.parse(date)
    }

    private fun createTraining() {
        var list= arrayListOf<String>()
        var getId = 1
        var getDate = ""
        val userWeight = 70
        var workout: Workout
        val gymType: String = intent.getStringExtra("GymName").toString()
        val muskelName: String = intent.getStringExtra("MuskelName").toString()
        var set = SetNum.text.toString()
        var rep = repeatNum.text.toString()
        val rnds = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ThreadLocalRandom.current().nextDouble(0.0 , 0.9)
        } else {
            TODO("VERSION.SDK_INT < LOLLIPOP")
        }
        val randnum = 0.0 + rnds
        var Calorie = set.toInt() * rep.toInt() * (2 * 3.0+randnum * userWeight) / 200.0005

        db.collection(Constant.USERS).document(getCurrentUserID()).collection(Constant.TRAININGS)
                .orderBy("date", Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener { task ->
                    if (task.result?.size()!! > 0) {
                        val any = if (task.isSuccessful) {
                            for (document in task.result!!) {
                                getDate = document.get("currentDateTime").toString()
                                list.add(document.get("workout_id").toString())
                            }
                            getId = list.get(list.size - 1).toInt()
                            if (currentDate!= getDate  || currentDate  > getDate) {
                                getId += 1
                            } else {
                                getId = getId
                            }
                            if (Calorie == 0.0) {
                                Calorie = 1.0
                            }
                            workout = Workout(
                                getId.toString(),
                                gymType,
                                muskelName,
                                set,
                                weightNum.text.toString(),
                                breakNum.text.toString(),
                                rep,
                                currentDate,
                                Calorie,
                                dateFormatter(currentDate)
                            )
                            FirestoreClass().createNewTraining(this, workout)
                            var recyclerView =  findViewById<RecyclerView>(id.recyclerView_add)
                            recyclerView.scrollToPosition(0)
                        } else{
                            Log.d(ChatLogActivity.TAG, "Error getting documents: ", task.exception)
                        }
                    } else {

                        if (!createdWorkout) {
                            workout = Workout(
                                getId.toString(),
                                gymType,
                                muskelName,
                                set,
                                weightNum.text.toString(),
                                breakNum.text.toString(),
                                rep,
                                currentDate,
                                Calorie,
                                dateFormatter(currentDate)
                            )
                            createdWorkout = true
                            FirestoreClass().createNewTraining(this, workout)
                            var recyclerView =  findViewById<RecyclerView>(id.recyclerView_add)
                            recyclerView.scrollToPosition(0)
                        }
                    }
                }

    }
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


