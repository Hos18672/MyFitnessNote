package com.example.myfitneesnote

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_my_profile.*
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.activity_workout.*

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
