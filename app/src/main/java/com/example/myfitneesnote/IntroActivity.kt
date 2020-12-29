package com.example.myfitneesnote


import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import kotlinx.android.synthetic.main.activity_intro.*
import kotlin.system.exitProcess

class IntroActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)
        fullscreen()
        animat()
        intro_login_button.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
        intro_sign_up_button.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
            finish()
        }
    }

    fun animat(){
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
        finish()
    }

}
