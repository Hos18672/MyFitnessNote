package com.example.myfitneesnote

import activities.R
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        val userId= intent.getStringExtra("user_id")
        val emailId= intent.getStringExtra("email_id")
        val usernameId= intent.getStringExtra("userName")

        main_userIdTV.text= "User Id = $userId"
        main_emailTv.text= "Email = $emailId"
        main_usernameTV.text= "Username = $usernameId"
        main_logOut_btn.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()}
    }
}
