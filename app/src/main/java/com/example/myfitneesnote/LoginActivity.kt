package com.example.myfitneesnote

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.graphics.component1
import androidx.core.view.isVisible
import com.example.myfitneesnote.firebase.FirestoreClass
import com.example.myfitneesnote.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        fullscreen()
        setupActionBar()

        login_signUpTv.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
            finish()
        }
        val pb = findViewById(R.id.progressBar_login) as ProgressBar

        sing_in_button.setOnClickListener {
            Handler().postDelayed({
                loginUser()
                pb.visibility = View.VISIBLE
                sign_in_btn_text.setText("Please wait ...")
            }, 100)
        }



    }
    /**
     * A function to login the user.
     */
    private fun loginUser(){
        val email : String = login_email_input.text.toString().trim{ it <= ' '}
        val password : String = login_password_input.text.toString().trim{ it <= ' '}
        if(validateForm(email, password)) {
            //showProgressDialog(resources.getString(R.string.please_wait))
            // create an instance and create a register a user with email and password
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                    //hideProgressDialog()
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
                        Toast.makeText(
                            this@LoginActivity, task.exception!!.message.toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            }
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
            startActivity(Intent(this, IntroActivity::class.java))
            finish()
        }
    }
    fun logInSuccess(user: User){
            hideProgressDialog()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
    }
    private fun validateForm(email: String, password: String) : Boolean{
        return when{
            TextUtils.isEmpty(email)->{
                showErrorSnackBar("Please enter a  email address")
                false
            }
            TextUtils.isEmpty(password)->{
                showErrorSnackBar("Please enter a  password")
                false
            }else ->{
                true
            }
        }
    }
    override fun onBackPressed() {
        startActivity(Intent(this, IntroActivity::class.java))
        finish()
    }
}



