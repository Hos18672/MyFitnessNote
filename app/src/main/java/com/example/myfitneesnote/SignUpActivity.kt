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
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_sign_up)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setupActionBar()

        signUp_loginText.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
        sigUp_button.setOnClickListener {
            when{
                TextUtils.isEmpty(signUpUsernameInput.text.toString().trim{ it <= ' '}) ->{
                    Toast.makeText(this@SignUpActivity,"Please enter your Username",Toast.LENGTH_SHORT).show()
                }
                TextUtils.isEmpty(signUp_email_input.text.toString().trim{ it <= ' '}) ->{
                    Toast.makeText(this@SignUpActivity,"Please enter your E-Mail",Toast.LENGTH_SHORT).show()
                }
                TextUtils.isEmpty(signUp_password_input.text.toString().trim{ it <= ' '}) ->{
                    Toast.makeText(this@SignUpActivity,"Please enter your password",Toast.LENGTH_SHORT).show()
                }
                TextUtils.isEmpty(signUp_password_input2.text.toString().trim{ it <= ' '}) ->{
                    Toast.makeText(this@SignUpActivity,"Please enter your password",Toast.LENGTH_SHORT).show()
                }
                else ->{
                    val email : String = signUp_email_input.text.toString().trim{ it <= ' '}
                    val password : String = signUp_password_input.text.toString().trim{ it <= ' '}

                    // create an instance and create a register a user with email and password
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val firebaseUser: FirebaseUser = task.result!!.user!!
                            Toast.makeText(
                                this@SignUpActivity,
                                "Your are Signed Up successfully.",
                                Toast.LENGTH_SHORT
                            ).show()

                            val intent = Intent(this@SignUpActivity, MainActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            intent.putExtra("user_id", firebaseUser.uid)
                            intent.putExtra("email_id", email)
                            intent.putExtra("userName", signUpUsernameInput.text.toString())
                            startActivity(intent)
                            finish()
                        }else{
                            Toast.makeText(this@SignUpActivity,task.exception!!.message.toString(),Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    private fun setupActionBar()
    {
        setSupportActionBar(toolBar_sign_up_activity)
        var actionBar = supportActionBar
        if(actionBar!=null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_navigate_before_black_24dp)
        }
        toolBar_sign_up_activity.setNavigationOnClickListener{onBackPressed()}
    }
}
