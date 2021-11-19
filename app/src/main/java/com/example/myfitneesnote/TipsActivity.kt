package com.example.myfitneesnote

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_tips.*

class TipsActivity : BaseActivity() {
    var t :String? = ""
    var d :String? = ""
    var p :String? = ""
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tips)
         setupActionBar()
        window.statusBarColor = ContextCompat.getColor(this, R.color.colorOfStutusBar)
        val topic = findViewById<TextView>(R.id.Topic)
        val pic = findViewById<ImageView>(R.id.Picture) as ImageView
        val disc = findViewById<TextView>(R.id.Description)

         t =  intent.getStringExtra("TopicName")
         d =  intent.getStringExtra("DescriptionName")
         p = intent.getStringExtra("ImageSrc")

        topic.text = t
        disc.text  = d
        if (t == "EZ Bar Curl"){
            pic.setImageResource(R.drawable.ez_bar_curl)
        }
        if (t == "Dumbbell Curl"){
            pic.setImageResource(R.drawable.curl_dumbell)
        }
        if (t == "One arm dumbbell Curl"){
            pic.setImageResource(R.drawable.one_arm_dumbell)
        }
        if (t == "Seated biceps curls"){
            pic.setImageResource(R.drawable.biceps_curl)
        }


    }



    private fun setupActionBar() {
        setSupportActionBar(toolBar_Tipps)
        val actionBar = supportActionBar
        if (actionBar != null) {
            supportActionBar?.setDisplayShowTitleEnabled(false)
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_navigate_before_black_24dp)
        }
        toolBar_Tipps.setNavigationOnClickListener {
            onBackPressed()
        }
    }
}


