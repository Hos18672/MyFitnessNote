package com.example.myfitneesnote

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.WindowManager
import android.view.WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
import android.widget.ProgressBar
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_main_layout.*
import java.util.*

class LoginActivity : BaseActivity() {
    val database = FirebaseDatabase.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE or SOFT_INPUT_ADJUST_RESIZE)
        fullscreen()
        setupActionBar()
        login_signUpTv.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
            finish()
        }
        sing_in_button.setOnClickListener {
                loginUser()
        }
    }
    private fun setupActionBar() {
        setSupportActionBar(toolBar_login_activity)
        var actionBar = supportActionBar
        if(actionBar!=null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_navigate_before_black_24dp)
        }
        toolBar_login_activity.setNavigationOnClickListener{
            onBackPressed()
        }
    }
    /**
     * A function to login the user.
     */
    @SuppressLint("SetTextI18n")
    private fun loginUser() {
        val email: String = login_email_input.text.toString().trim { it <= ' ' }
        val password: String = login_password_input.text.toString().trim { it <= ' ' }
       // val pb = findViewById<ProgressBar>(R.id.progressBar_login)

        if (validateForm(email, password)) {
                //showProgressDialog(resources.getString(R.string.please_wait))
                // create an instance and create a register a user with email and password
               // pb.visibility = View.VISIBLE
                //sign_in_btn_text.setText("Please wait ...")
                FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        //hideProgressDialog()
                        if (task.isSuccessful) {
                            Toast.makeText(
                                this@LoginActivity,
                                "Your are logged in successfully.",
                                Toast.LENGTH_SHORT
                            ).show()

                            val intent = Intent(this@LoginActivity, MainActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            intent.putExtra("user_id", FirebaseAuth.getInstance().currentUser!!.uid)
                            intent.putExtra("email_id", email)
                            startActivity(intent)
                            finish()

                        } else {
                            //pb.visibility = View.GONE
                            Toast.makeText(
                                this@LoginActivity, task.exception!!.message.toString(),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
        } else {
            //pb.visibility = View.GONE
            //sign_in_btn_text.text = "Sign in"
        }
    }

    private fun validateForm(email: String, password: String): Boolean {
        return when {
            TextUtils.isEmpty(email) -> {
                showErrorSnackBar("Please enter a  email address")
                false
            }
            TextUtils.isEmpty(password) -> {
                showErrorSnackBar("Please enter a  password")
                false
            }
            else -> {
                true
            }
        }
    }



    override fun onBackPressed() {
        finish()
    }
}



