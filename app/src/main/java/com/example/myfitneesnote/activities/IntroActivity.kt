package com.example.myfitneesnote.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.view.WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS
import android.view.animation.AnimationUtils
import androidx.annotation.RequiresApi
import com.example.myfitneesnote.R
import kotlinx.android.synthetic.main.activity_intro.*


@Suppress("DEPRECATION")
class IntroActivity : BaseActivity() {
    lateinit var m : MainActivity

    @SuppressLint("ResourceAsColor")
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)
        val window: Window = this.window
        window.addFlags(FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        //fullscreen()
        m = MainActivity()
        animat()
        onClick()
    }
    private fun onClick(){
        intro_login_button.setOnClickListener {
            var intent =  Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
        intro_sign_up_button.setOnClickListener {
            var intent =  Intent(this, SignUpActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
    private fun animat(){
        val ttb = AnimationUtils.loadAnimation(this, R.anim.ttb1)
            intro_imageView.startAnimation(ttb)
        val ttb1 = AnimationUtils.loadAnimation(this, R.anim.ttb)
        intro_textView.startAnimation(ttb1)

        val btt = AnimationUtils.loadAnimation(this, R.anim.btt)
        intro_login_button.startAnimation(btt)
        val btt1 = AnimationUtils.loadAnimation(this, R.anim.btt1)
        intro_sign_up_button.startAnimation(btt1)
    }
    override fun onBackPressed() {
        m.finish()
        this.finishAffinity()
    }

}
