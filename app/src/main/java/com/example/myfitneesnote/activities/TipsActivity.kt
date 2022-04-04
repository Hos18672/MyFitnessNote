package com.example.myfitneesnote.activities

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.example.myfitneesnote.R
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
        sv_tipp.viewTreeObserver
            .addOnScrollChangedListener {
                if (!sv_tipp.canScrollVertically(-1)) {
                    toolBar_Tipps.elevation = 0f
                }
                else{
                    toolBar_Tipps.elevation = 50f
                }
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


