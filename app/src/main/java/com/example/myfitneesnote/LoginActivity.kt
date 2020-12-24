package com.example.myfitneesnote

import activities.R
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.WindowManager
import android.widget.Toast

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.toolBar_login_activity

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setupActionBar()

        login_signUpTv.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }

            login_button.setOnClickListener {
                when{
                    TextUtils.isEmpty(login_email_input.text.toString().trim{ it <= ' '}) ->{
                        Toast.makeText(this@LoginActivity,"Please enter your E-Mail", Toast.LENGTH_SHORT).show()
                    }
                    TextUtils.isEmpty(login_password_input.text.toString().trim{ it <= ' '}) ->{
                        Toast.makeText(this@LoginActivity,"Please enter your password", Toast.LENGTH_SHORT).show()
                    }

                    else ->{
                        val email : String = login_email_input.text.toString().trim{ it <= ' '}
                        val password : String = login_password_input.text.toString().trim{ it <= ' '}

                        // create an instance and create a register a user with email and password
                        FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password).addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val firebaseUser: FirebaseUser = task.result!!.user!!
                                Toast.makeText(
                                    this@LoginActivity,
                                    "Your are logged in Up successfully.",
                                    Toast.LENGTH_SHORT
                                ).show()

                                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                intent.putExtra("user_id", FirebaseAuth.getInstance().currentUser!!.uid)
                                intent.putExtra("email_id", email)
                                startActivity(intent)
                                finish()
                            }else{
                                Toast.makeText(this@LoginActivity,task.exception!!.message.toString(),
                                    Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }
    }

    private fun setupActionBar()
    {
        setSupportActionBar(toolBar_login_activity)
        var actionBar = supportActionBar
        if(actionBar!=null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_navigate_before_black_24dp)
        }
        toolBar_login_activity.setNavigationOnClickListener{onBackPressed()}
    }
}
