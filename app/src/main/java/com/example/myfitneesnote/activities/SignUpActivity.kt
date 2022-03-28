package com.example.myfitneesnote.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityOptionsCompat
import com.example.myfitneesnote.R
import com.example.myfitneesnote.firebase.FirebaseService
import com.example.myfitneesnote.firebase.FirestoreClass
import com.example.myfitneesnote.utils.showCustomToast
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_sign_up.*
import java.util.*

@Suppress("DEPRECATION")
class SignUpActivity : BaseActivity() {

    var userToken = ""
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        setupActionBar()
        login_signUpText.setOnClickListener {
            val intent =  Intent(this, LoginActivity::class.java)
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, login_signUpText, "tsigIn")
            startActivity(intent, options.toBundle())

        }
        sigUp_button.setOnClickListener {
            getFirebaseMessagingToken()
            signUpUser()
        }
    }

    /**
     * A function to register the user  and save data on  firestore database.
     */
    private  fun signUpUser(){
        val name     : String = signUpNameInput.text.toString().trim{ it <= ' '}
        val username : String = signUpUsernameInput.text.toString().trim{ it <= ' '}
        val email    : String = signUp_email_input.text.toString().trim{ it <= ' '}
        val password : String = signUp_password_input.text.toString().trim{ it <= ' '}
        val password2: String = signUp_password_input2.text.toString().trim{ it <= ' '}
        val pb = findViewById<ProgressBar>(R.id.progressBar_signUp)
        if (password.length < 8 && password2.length < 8){
            showErrorSnackBar("Password's length must be equal to 8 or greater!")
        }else{
            if(validateForm(name, username, email, password, password2) ) {
                if (password == password2) {
                    pb.visibility = View.VISIBLE
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val firebaseUser: FirebaseUser = task.result!!.user!!
                                firebaseUser.email!!
                                val user = com.example.myfitneesnote.model.User(
                                    firebaseUser.uid,
                                    name,
                                    username,
                                    email,
                                    userToken
                                )
                                FirestoreClass().registerUser(this, user)
                                val uid = FirebaseAuth.getInstance().uid ?: ""
                                val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
                                ref.setValue(user)
                                    .addOnSuccessListener {
                                        Log.d("User", "Finally we saved the user to Firebase Database")
                                    }
                                    .addOnFailureListener {
                                        Log.d("User", "Failed to set value to database: ${it.message}")
                                    }

                                    FirebaseAuth.getInstance().currentUser!!.sendEmailVerification().addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        Toast(this).showCustomToast("Please check your email to verify", this)
                                    }else{
                                        showErrorSnackBar("Email verfication email failed!")
                                    }
                                }
                                //  FirebaseAuth.getInstance().signOut()
                                val intent =
                                    Intent(this@SignUpActivity, BodyInfoActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                intent.putExtra("user_id", firebaseUser.uid)
                                intent.putExtra("name", signUpNameInput.text.toString())
                                intent.putExtra("userName", signUpUsernameInput.text.toString())
                                intent.putExtra("email_id", email)
                                startActivity(intent)
                                finish()

                            } else {
                                pb.visibility = View.GONE
                                showErrorSnackBar(task.exception!!.message.toString())

                            }
                        }
                }else{
                    pb.visibility = View.GONE
                    showErrorSnackBar("Passwords not match")
                }
            }
        }

    }

    fun getFirebaseMessagingToken() {
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task: Task<String?> ->
                if (!task.isSuccessful) {
                    //Could not get FirebaseMessagingToken
                    return@addOnCompleteListener
                }
                if (null != task.result) {
                    //Got FirebaseMessagingToken
                    val firebaseMessagingToken = Objects.requireNonNull(task.result)
                    FirebaseService.token = firebaseMessagingToken
                    userToken = firebaseMessagingToken.toString()
                    //Use firebaseMessagingToken further
                }
            }
    }

    private  fun validateForm(name: String, username: String, email: String, password1 : String, password2 : String) : Boolean{
        return when{
            TextUtils.isEmpty(name)->{ showErrorSnackBar("Please enter a  name")
                false
            }
            TextUtils.isEmpty(username)->{
                showErrorSnackBar("Please enter a  username")
                false
            }
            TextUtils.isEmpty(email)->{
                showErrorSnackBar("Please enter a  email address")
                false
            }
            TextUtils.isEmpty(password1)->{
                showErrorSnackBar("Please enter a  password")
                false
            }
            TextUtils.isEmpty(password2)->{
                showErrorSnackBar("Please retype your password")
                false
            }
            else ->{
                true
            }
        }
    }
    private fun setupActionBar() {
        setSupportActionBar(toolBar_sign_up_activity)
        val actionBar = supportActionBar
        if(actionBar!=null) {
            supportActionBar?.setDisplayShowTitleEnabled(false)
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_navigate_before_black_24dp)
        }
        toolBar_sign_up_activity.setNavigationOnClickListener{
            onBackPressed()
        }
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
        // hideProgressDialog()
        /**
         * Here the new user registered is automatically signed-in so we just sign-out the user from firebase
         * and send him to Intro Screen for Sign-In
         */
        FirebaseAuth.getInstance().signOut()
        // Finish the Sign-Up Screen
        finish()
    }
    override fun onBackPressed() {
        startActivity(Intent(this, IntroActivity::class.java))
        finish() }

}