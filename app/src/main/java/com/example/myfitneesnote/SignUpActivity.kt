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

class SignUpActivity : BaseActivity() {

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
                 signUpUser()
        }
    }
    /**
     * A function to register the user  and save data on  firestore database.
     */
    private  fun signUpUser(){
        val name : String = signUpUsernameInput.text.toString().trim{ it <= ' '}
        val email : String = signUp_email_input.text.toString().trim{ it <= ' '}
        val password : String = signUp_password_input.text.toString().trim{ it <= ' '}
        if(validateForm(name,email,password)){
            // Toast.makeText(this, "Now we register User",Toast.LENGTH_SHORT).show()
            showProgressDialog(resources.getString(R.string.please_wait))
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password).addOnCompleteListener {
                    task ->
                    hideProgressDialog()
                    if (task.isSuccessful) {
                        val firebaseUser: FirebaseUser = task.result!!.user!!
                        Toast.makeText(
                            this@SignUpActivity,
                            "Your are Signed Up successfully.",
                            Toast.LENGTH_SHORT
                        ).show()
                      //  FirebaseAuth.getInstance().signOut()

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
    private  fun validateForm(name :String, email: String, password: String) : Boolean{
        return when{
            TextUtils.isEmpty(name)->{
                showErrorSnackBar("Please enter a  name")
                false
            }
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

    /**
     * A function to be called the user is registered successfully and entry is made in the firestore database.
     */
    fun userRegisteredSuccess() {
        Toast.makeText(
            this@SignUpActivity,
            "You have successfully registered.",
            Toast.LENGTH_SHORT
        ).show()
        // Hide the progress dialog
        hideProgressDialog()
        /**
         * Here the new user registered is automatically signed-in so we just sign-out the user from firebase
         * and send him to Intro Screen for Sign-In
         */
        FirebaseAuth.getInstance().signOut()
        // Finish the Sign-Up Screen
        finish()
    }
}
