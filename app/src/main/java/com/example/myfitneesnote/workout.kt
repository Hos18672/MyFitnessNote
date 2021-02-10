package com.example.myfitneesnote

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_workout.*

class workout : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_workout)
        fullscreen()
        setupActionBar()
    }
    private fun setupActionBar() {
        setSupportActionBar(toolBar_workout_activity)
        var actionBar = supportActionBar
        if(actionBar!=null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_navigate_before_black_24dp)
        }
        toolBar_workout_activity.setNavigationOnClickListener{
            onBackPressed()
        }
    }
}
