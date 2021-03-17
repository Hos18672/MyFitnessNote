package com.example.myfitneesnote

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import kotlinx.android.synthetic.main.activity_muskel_group.*

class MuskelGroupActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_muskel_group)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        fullscreen()
        setupActionBar()

        brust_btn.setOnClickListener {
            startActivity(Intent(this, AddWorkoutActivity::class.java))
        }
       biceps_btn.setOnClickListener {
            startActivity(Intent(this, AddWorkoutActivity::class.java))
        }
        triceps_btn.setOnClickListener {
            startActivity(Intent(this, AddWorkoutActivity::class.java))
        }
        forearms_btn7.setOnClickListener {
            startActivity(Intent(this, AddWorkoutActivity::class.java))
        }
        forearms_btn9.setOnClickListener {
            startActivity(Intent(this, AddWorkoutActivity::class.java))
        }

        forearms_btn10.setOnClickListener {
            startActivity(Intent(this, AddWorkoutActivity::class.java))
        }
        forearms_btn11.setOnClickListener {
            startActivity(Intent(this, AddWorkoutActivity::class.java))
        }
        BACK_btn12.setOnClickListener {
                onBackPressed()
        }

    }

    private fun setupActionBar() {
        setSupportActionBar(toolBar_muscle_gruppe_activity)
        var actionBar = supportActionBar
        if(actionBar!=null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_navigate_before_black_24dp)
        }
        toolBar_muscle_gruppe_activity.setNavigationOnClickListener{
            onBackPressed()
        }
    }

}
