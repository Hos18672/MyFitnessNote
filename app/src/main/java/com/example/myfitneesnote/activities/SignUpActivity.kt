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
import com.example.myfitneesnote.firebase.FirestoreClass
import com.example.myfitneesnote.utils.showCustomToast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_sign_up.*
import java.util.regex.Matcher
import java.util.regex.Pattern

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
            signUpUser()
        }
    }
    private  fun signUpUser(){
        val name     : String = signUpNameInput.text.toString().trim{ it <= ' '}
        val username : String = signUpUsernameInput.text.toString().trim{ it <= ' '}
        val email    : String = signUp_email_input.text.toString().trim{ it <= ' '}
        val password : String = signUp_password_input.text.toString().trim{ it <= ' '}
        val password2: String = signUp_password_input2.text.toString().trim{ it <= ' '}

        val pb = findViewById<ProgressBar>(R.id.progressBar_signUp)

        if(checkEmail(email)){
            if (password.length < 8 && password2.length < 8){
                showErrorSnackBar("The length of the password must be at least 8!")
            }else{
                if(validateForm(name, username, email, password, password2) ) {
                    if (isValidPassword(password)){
                        if (password == password2) {
                            pb.visibility = View.VISIBLE
                            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        val firebaseUser: FirebaseUser = task.result!!.user!!
                                        firebaseUser.email!!
                                        FirebaseMessaging.getInstance().token.addOnCompleteListener(
                                            OnCompleteListener { task ->
                                                if (!task.isSuccessful) {
                                                    Log.w("Tag", "Fetching FCM registration token failed", task.exception)
                                                    return@OnCompleteListener
                                                }
                                                // Get new FCM registration token
                                                val token = task.result
                                                userToken = token.toString()
                                                val user = com.example.myfitneesnote.model.User(
                                                    firebaseUser.uid,
                                                    name,
                                                    username,
                                                    email,
                                                    "password",
                                                    token.toString(),
                                                    ""
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
                                                        Toast(this).showCustomToast("Please check your email for verification", this)
                                                    }else{
                                                        showErrorSnackBar("Email verification failed!")
                                                    }
                                                }

                                                val intent = Intent(this@SignUpActivity, BodyInfoActivity::class.java)
                                                startActivity(intent)
                                            })

                                    }else{
                                        showErrorSnackBar("This email has already been registered")
                                        pb.visibility = View.GONE
                                    }

                                }
                        }
                    }else{
                        pb.visibility = View.GONE
                        showErrorSnackBar("Your password must contain at least one uppercase, one lowercase letter, and one special character!")
                    }

                }else{
                    pb.visibility = View.GONE
                    showErrorSnackBar("Passwords not match")
                }
            }
        }else{
            showErrorSnackBar("Invalid email!")
        }

    }

    fun isValidPassword(password: String?): Boolean {
        val pattern: Pattern
        val PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$"
        pattern = Pattern.compile(PASSWORD_PATTERN)
        val matcher: Matcher = pattern.matcher(password)
        return matcher.matches()
    }


    val EMAIL_ADDRESS_PATTERN: Pattern = Pattern.compile(
        "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                "\\@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                ")+"
    )

    private fun checkEmail(email: String): Boolean {
        return EMAIL_ADDRESS_PATTERN.matcher(email).matches()
    }

    private  fun validateForm(name: String, username: String, email: String, password1 : String, password2 : String) : Boolean{
        return when{
            TextUtils.isEmpty(name)  ->{ showErrorSnackBar("Please enter a  name")
                false
            }
            TextUtils.isEmpty(username)->{
                showErrorSnackBar("Please enter a  username")
                false
            }
            TextUtils.isEmpty(email) ->{
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
    fun userRegisteredSuccess() {
        Toast.makeText(
            this@SignUpActivity,
            "You have successfully registered.",
            Toast.LENGTH_SHORT
        ).show()

        FirebaseAuth.getInstance().signOut()
        // Finish the Sign-Up Screen
        finish()
    }
    override fun onBackPressed() {
        startActivity(Intent(this, IntroActivity::class.java))
        finish() }

}