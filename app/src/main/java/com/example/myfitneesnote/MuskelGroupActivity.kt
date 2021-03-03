package com.example.myfitneesnote

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_muskel_group.*

class MuskelGroupActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_muskel_group)
        fullscreen()
        setupActionBar()
        brust_btn.setOnClickListener {
            var dialog = add_popup_fragment()
            dialog.show(supportFragmentManager, "customDialog")

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
