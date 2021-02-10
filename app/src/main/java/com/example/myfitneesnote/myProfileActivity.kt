package com.example.myfitneesnote

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_my_profile.*

class myProfileActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_profile)
        fullscreen()
        setupActionBar()
    }
    private fun setupActionBar() {
        setSupportActionBar(toolBar_my_profile_activity)
        var actionBar = supportActionBar
        if(actionBar!=null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_navigate_before_black_24dp)
        }
        toolBar_my_profile_activity.setNavigationOnClickListener{
            onBackPressed()
        }
    }

}
