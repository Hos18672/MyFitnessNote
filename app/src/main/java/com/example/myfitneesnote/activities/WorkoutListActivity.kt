package com.example.myfitneesnote.activities

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.example.myfitneesnote.R
import com.example.myfitneesnote.fragments.WorkoutListFragmentAll
import com.example.myfitneesnote.fragments.WorkoutListFragmentToday
import com.example.myfitneesnote.fragments.WorkoutListFragmentTomorrow
import kotlinx.android.synthetic.main.activity_workouts_list.*

class WorkoutListActivity : BaseActivity() {
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_workouts_list)
        toolBar_workouts_activity.elevation = 0f
        setupActionBar()
        btn1.setCardBackgroundColor(Color.parseColor("#00AEFF"))
        val trainingsFragment = WorkoutListFragmentToday()
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.root_container, trainingsFragment).commit()
        }
        tip()
    }
    @SuppressLint("Range")
    private fun tip() {
        val nightModeFlags: Int = applicationContext.resources.configuration.uiMode and
                Configuration.UI_MODE_NIGHT_MASK
        when (nightModeFlags) {
            Configuration.UI_MODE_NIGHT_YES -> {
                btn2.setCardBackgroundColor(Color.parseColor("#26282C"))
                btn3.setCardBackgroundColor(Color.parseColor("#26282C"))
                btn1.setOnClickListener {
                    val trainingsFragment = WorkoutListFragmentToday()
                    supportFragmentManager.beginTransaction().apply {
                        replace(R.id.root_container, trainingsFragment).commit()
                        btn2.setCardBackgroundColor(Color.parseColor("#26282C"))
                        btn3.setCardBackgroundColor(Color.parseColor("#26282C"))
                        btn1.setCardBackgroundColor(Color.parseColor("#00AEFF"))
                    }
                }

                btn2.setOnClickListener {
                    val trainingsFragment2 = WorkoutListFragmentTomorrow()
                    supportFragmentManager.beginTransaction().apply {
                        replace(R.id.root_container, trainingsFragment2).commit()
                        btn2.setCardBackgroundColor(Color.parseColor("#00AEFF"))
                        btn1.setCardBackgroundColor(Color.parseColor("#26282C"))
                        btn3.setCardBackgroundColor(Color.parseColor("#26282C"))
                    }
                }

                btn3.setOnClickListener {
                    val trainingsFragment2 = WorkoutListFragmentAll()
                    supportFragmentManager.beginTransaction().apply {
                        replace(R.id.root_container, trainingsFragment2).commit()
                        btn3.setCardBackgroundColor(Color.parseColor("#00AEFF"))
                        btn1.setCardBackgroundColor(Color.parseColor("#26282C"))
                        btn2.setCardBackgroundColor(Color.parseColor("#26282C"))
                    }
                }
            }
            Configuration.UI_MODE_NIGHT_NO -> {

                btn1.setOnClickListener {
                    val trainingsFragment = WorkoutListFragmentToday()
                    supportFragmentManager.beginTransaction().apply {
                        replace(R.id.root_container, trainingsFragment).commit()
                        btn3.setCardBackgroundColor(Color.WHITE)
                        btn2.setCardBackgroundColor(Color.WHITE)
                        btn1.setCardBackgroundColor(Color.parseColor("#00AEFF"))
                    }
                }

                btn2.setOnClickListener {
                    val trainingsFragment2 = WorkoutListFragmentTomorrow()
                    supportFragmentManager.beginTransaction().apply {
                        replace(R.id.root_container, trainingsFragment2).commit()

                        btn2.setCardBackgroundColor(Color.parseColor("#00AEFF"))
                        btn1.setCardBackgroundColor(Color.WHITE)
                        btn3.setCardBackgroundColor(Color.WHITE)
                    }
                }
                btn3.setOnClickListener {
                    val trainingsFragment2 = WorkoutListFragmentAll()
                    supportFragmentManager.beginTransaction().apply {
                        replace(R.id.root_container, trainingsFragment2).commit()

                        btn3.setCardBackgroundColor(Color.parseColor("#00AEFF"))
                        btn1.setCardBackgroundColor(Color.WHITE)
                        btn2.setCardBackgroundColor(Color.WHITE)
                    }
                }
            }
            Configuration.UI_MODE_NIGHT_UNDEFINED -> {
            }
        }
    }

    private fun setupActionBar() {
        setSupportActionBar(toolBar_workouts_activity)
        val actionBar = supportActionBar
        if(actionBar!=null) {
            supportActionBar?.setDisplayShowTitleEnabled(false)
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_navigate_before_black_24dp)
        }
        toolBar_workouts_activity.setNavigationOnClickListener{
            onBackPressed()
        }
    }
    override fun onBackPressed() {
        finish()
    }

}