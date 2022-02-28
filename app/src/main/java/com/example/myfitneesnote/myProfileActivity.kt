package com.example.myfitneesnote

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.myfitneesnote.R.*
import com.example.myfitneesnote.R.drawable.*
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Picasso
import com.theartofdev.edmodo.cropper.CropImage
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_my_profile.*
import kotlinx.android.synthetic.main.nav_header_main.*
import java.util.*
import android.R

import android.app.Activity
import android.content.Context

import android.widget.TextView
import com.theartofdev.edmodo.cropper.CropImageView


class MyProfileActivity : BaseActivity() {
    private var mFirebaseDatabase: DatabaseReference? = null
    private var mFirebaseInstance: FirebaseDatabase? = null
    private lateinit var storageReference : StorageReference

    private lateinit var mAuth : FirebaseAuth
    private var userId: String? = null
    private  var getGender : Boolean = true

    private lateinit var profileImageView : CircleImageView
    private lateinit var imageProfileMain: CircleImageView
    private lateinit var imageUri : Uri
    private  var myUri: String =""




    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_my_profile)
       // fullscreen()
        setupActionBar()
        userProfileData()
        mFirebaseInstance = FirebaseDatabase.getInstance()
        // get reference to 'users' node
        mFirebaseDatabase = mFirebaseInstance!!.getReference("users")
        val user = FirebaseAuth.getInstance().currentUser
        // add it only if it is not saved to database
        userId = user?.uid
        btnSave.setOnClickListener {
            if (checkForInternet(this)) {
                onUpdateClicked()
            } else {
                Toast.makeText(this, "No internet connection!", Toast.LENGTH_SHORT).show()
            }
            uploadImage()
        }

        mAuth = FirebaseAuth.getInstance()
        val uid = FirebaseAuth.getInstance().uid
        mFirebaseDatabase = FirebaseDatabase.getInstance().getReference("/users/$uid")
        storageReference = FirebaseStorage.getInstance().reference.child("Profile Pic")
        profileImageView = findViewById(id.ProfileImage)


        editBtn.setOnClickListener {
            CropImage.activity().setAspectRatio(1,1 ).start(this@MyProfileActivity)
        }
        profileImageView.setOnClickListener {
            CropImage.activity().setAspectRatio(1,1 ).start(this@MyProfileActivity)
        }
    }

    @Override
     override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE&& resultCode == RESULT_OK && data!= null){
            var result : CropImage.ActivityResult = CropImage.getActivityResult(data)
            imageUri = result.uri
            profileImageView.setImageURI(imageUri)
        }else{
        }
    }

    private fun uploadImage() {
        when {
            imageUri == null -> Toast.makeText(
                this,
                "Please select image first.",
                Toast.LENGTH_LONG
            ).show()


            else -> {
                val progressDialog = ProgressDialog(this)
                progressDialog.setTitle("Upload picture")
                progressDialog.setMessage("Please wait, we are uploading your picture ...")
                progressDialog.show()


                val fileRef = storageReference.child(mAuth.currentUser!!.uid+ ".jpg") //GetFile Extention Buiding Function Kotlin!
                var uploadTask: StorageTask<*>
                uploadTask = fileRef.putFile(imageUri!!)

                uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                    if (!task.isSuccessful) {
                        task.exception?.let {
                            throw it
                            progressDialog.dismiss()
                        }
                    }
                    return@Continuation fileRef.downloadUrl
                }).addOnCompleteListener(OnCompleteListener<Uri> { task ->
                    if (task.isSuccessful) {
                        val downloadUrl = task.result
                        myUri = downloadUrl.toString()

                        val ref = FirebaseDatabase.getInstance().reference.child("users")

                        val postMap = HashMap<String, Any>()
                        postMap["image"] = myUri

                        ref.child(mAuth.currentUser!!.uid).updateChildren(postMap)

                        Toast.makeText(
                            this,
                            "Your picture has been updated successfully.",
                            Toast.LENGTH_LONG
                        ).show()
                        progressDialog.dismiss()
                    } else {
                        progressDialog.dismiss()
                    }
                })
            }
        }
    }
    private fun onUpdateClicked() {
        val username = login_username_input.text.toString()
        val name = profile_fullName_input.text.toString()
        val age = age_input_profile.text.toString()
        val height = height_input_profile.text.toString()
        val weight = weight_input_profile.text.toString()
        val gender : String
        if(getGender){
            gender = "Male"
        }else{
            gender = "Female"
        }
        //Calling updateUser function
        updateUser(this, username,age,height,weight,gender, name)
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
                if(snapshot.hasChild("image")){
                    var image = snapshot.child("image").value.toString()
                    if (image.isNotEmpty()){
                        Picasso.get().load(image).into(profileImageView)

                    }

                }
                else{
                    System.out.println("empty")
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
                id.radio_male_profile ->
                    if (checked) {
                        getGender = true
                    }
                id.radio_female_profile ->
                    if (checked) {
                        getGender = false
                    }
            }
        }
    }

    companion object {
        private fun updateUser(
            myProfileActivity: MyProfileActivity, username: String,
            age: String,
            height: String,
            weight: String,
            gender: String,
            name: String,
        ) {
            // updating the user via child nodes

            val fs  : FirebaseFirestore = FirebaseFirestore.getInstance()
            if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(name)) {
                myProfileActivity.mFirebaseDatabase!!.child(myProfileActivity.userId!!).child("username").setValue(username.trim())
                myProfileActivity.mFirebaseDatabase!!.child(myProfileActivity.userId!!).child("name").setValue(name.trim())
                myProfileActivity.mFirebaseDatabase!!.child(myProfileActivity.userId!!).child("age").setValue(age.trim())
                myProfileActivity.mFirebaseDatabase!!.child(myProfileActivity.userId!!).child("height").setValue(height.trim())
                myProfileActivity.mFirebaseDatabase!!.child(myProfileActivity.userId!!).child("weight").setValue(weight.trim())
                myProfileActivity.mFirebaseDatabase!!.child(myProfileActivity.userId!!).child("gender").setValue(gender.trim())
                Toast.makeText(myProfileActivity.applicationContext, "Successfully updated user", Toast.LENGTH_SHORT).show()
                fs.collection("users").document(myProfileActivity.userId!!).update("age", age.trim(),
                    "height", height.trim(),
                    "weight", weight.trim(),
                    "gender", gender.trim())

            }
            else
                Toast.makeText(myProfileActivity.applicationContext, "Unable to update user", Toast.LENGTH_SHORT).show()
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
