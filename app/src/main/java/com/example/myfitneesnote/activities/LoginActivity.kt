package com.example.myfitneesnote.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityOptionsCompat
import com.example.myfitneesnote.R
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_login.*

@Suppress("DEPRECATION")
class LoginActivity : BaseActivity() {
    lateinit var GoogleSignInClient: GoogleSignInClient
    val RC_SIGN_IN: Int = 1
    lateinit var gso: GoogleSignInOptions
    lateinit var auth: FirebaseAuth
    private val regCode: Int = 123

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setupActionBar()

        auth = Firebase.auth


        login_signUpBtn.setOnClickListener {
            val intent =  Intent(this, SignUpActivity::class.java)
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, login_signUpBtn, "tSignUp")
            startActivity(intent, options.toBundle())
        }

        sing_in_button.setOnClickListener {
                loginUser()
        }

    }


    private fun setupActionBar() {
        setSupportActionBar(toolBar_login_activity)
        val actionBar = supportActionBar
        if(actionBar!=null)
        {
            supportActionBar?.setDisplayShowTitleEnabled(false)
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
        val pb = findViewById<ProgressBar>(R.id.progressBar_login)
        if (validateForm(email, password)) {
            //create an instance and create a register a user with email and password
                pb.visibility = View.VISIBLE
                login_signInText.text = "Please wait"
                FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
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
                            pb.visibility = View.GONE
                            login_signInText.text = "Sign in"
                            showErrorSnackBar("Login Failed: Your Email or password is wrong!")
                        }
                    }
        } else {
            pb.visibility = View.GONE
            login_signInText.text = "SIGN IN"
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
        startActivity(Intent(this, IntroActivity::class.java))
        finish() }
}



