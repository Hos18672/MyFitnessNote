package com.example.myfitneesnote

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidx.core.app.ActivityOptionsCompat
import com.example.myfitneesnote.R.*
import com.example.myfitneesnote.R.drawable.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_my_profile.*

class MyProfileActivity : BaseActivity() {
    private var mFirebaseDatabase: DatabaseReference? = null
    private var mFirebaseInstance: FirebaseDatabase? = null
    private var userId: String? = null
    private  var getGender : Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_my_profile)
       // fullscreen()
        setupActionBar()
        userProfileData()
        btnback_profile.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, imageView8, "profileImage")
            startActivity(intent, options.toBundle())
            finish()}
        mFirebaseInstance = FirebaseDatabase.getInstance()
        // get reference to 'users' node
        mFirebaseDatabase = mFirebaseInstance!!.getReference("users")
        val user = FirebaseAuth.getInstance().currentUser
        // add it only if it is not saved to database
        userId = user?.uid
        btnSave.setOnClickListener {
            onUpdateClicked()
        }
    }
    private fun updateUser(username: String, age : String, height: String, weight: String,gender :String, name: String,fwc : Boolean) {
        // updating the user via child nodes
        if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(name)) {
            mFirebaseDatabase!!.child(userId!!).child("username").setValue(username)
            mFirebaseDatabase!!.child(userId!!).child("name").setValue(name)
            mFirebaseDatabase!!.child(userId!!).child("age").setValue(age)
            mFirebaseDatabase!!.child(userId!!).child("height").setValue(height)
            mFirebaseDatabase!!.child(userId!!).child("weight").setValue(weight)
            mFirebaseDatabase!!.child(userId!!).child("gender").setValue(gender)
            mFirebaseDatabase!!.child(userId!!).child("firstWorkoutIsCreated").setValue(fwc)
            Toast.makeText(applicationContext, "Successfully updated user", Toast.LENGTH_SHORT).show()
        }
        else
            Toast.makeText(applicationContext, "Unable to update user", Toast.LENGTH_SHORT).show()
    }
    private fun onUpdateClicked() {
        val username = login_username_input.text.toString()
        val name = profile_fullName_input.text.toString()
        val age = age_input_profile.text.toString()
        val height = height_input_profile.text.toString()
        val weight = weight_input_profile.text.toString()
        val gend : String
        val fwc = false
        if(getGender == true){
          gend = "Male"
        }else{
            gend = "Female"
        }
        //Calling updateUser function
        updateUser(username,age,height,weight,gend, name,fwc)
    }
    private fun setupActionBar() {
        setSupportActionBar(toolBar_my_profile_activity)
        val actionBar = supportActionBar
        if(actionBar!=null)
        {
            supportActionBar?.setDisplayShowTitleEnabled(false)
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(ic_navigate_before_black_24dp)
        }
        toolBar_my_profile_activity.setNavigationOnClickListener{
            onBackPressed()
        }
    }
    private fun userProfileData() {
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val username = snapshot.child("username").getValue(String::class.java)
                val fullname = snapshot.child("name").getValue(String::class.java)
                val age =    snapshot.child("age").getValue(String::class.java)
                val height = snapshot.child("height").getValue(String::class.java)
                val weight = snapshot.child("weight").getValue(String::class.java)
                val gender = snapshot.child("gender").getValue(String::class.java)

                login_username_input.setText(username)
                profile_fullName_input.setText(fullname)
                age_input_profile.setText(age)
                height_input_profile.setText("$height" )
                weight_input_profile.setText("$weight" )
                if(gender == "Male"){
                    radio_male_profile.isChecked = true
                }else{
                    radio_female_profile.isChecked = true
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
    fun onRadioButtonClicked(view: View) {
        if (view is RadioButton) {
            // Is the button now checked?
            val checked = view.isChecked
            // Check which radio button was clicked
            when (view.getId()) {
                R.id.radio_male_profile ->
                    if (checked) {
                        getGender = true
                    }
                R.id.radio_female_profile ->
                    if (checked) {
                        getGender = false
                    }
            }
        }
    }
}
/*
class profileImage(val user: User): Item<ViewHolder>(){
    override fun bind(viewHolder: ViewHolder, position: Int) {
        // load our user image into the picture
        val uri = user.image
        val targetImageView = viewHolder.itemView.imageView_to_row
        Picasso.get().load(uri).into(targetImageView)
    }
    override fun getLayout(): Int {
        return R.layout.chat_to_row
    }
}*/
