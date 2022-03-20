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
import java.util.*

import com.example.myfitneesnote.utils.showCustomToast
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

    private  var uploaded :Boolean = false


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
        profileImageView = findViewById(id.ProfileImage)
        btnSave.setOnClickListener {
            if (checkForInternet(this)) {
                onUpdateClicked()

            } else {
                Toast.makeText(this, "No internet connection!", Toast.LENGTH_SHORT).show()
            }
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
            uploadImage()
        }else{
        }
    }

    private fun uploadImage() {
        when (imageUri) {
            null -> Toast.makeText(
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

                        Toast(this).showCustomToast ("Your picture has been updated successfully.", this)
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
        if(name!=null  && name !=""  && name !=" "){
            if(username!=null && username.length > 0 ){
                if (age.toInt() in 16..120 ){
                    if(height.toInt() in 50 ..300){
                        if(weight.toInt() in 20 ..300) {
                            updateUser(this, username,age,height,weight,gender, name, uploaded)
                        }else{
                            showErrorSnackBar("Your weight must be between 20 kg and 300 kg!")
                        }
                    }else{
                        showErrorSnackBar("Your height must be between 50 cm and 300 cm!")
                    }
                }else{
                    showErrorSnackBar("Your age must be between 16 and 120 years Old!")
                }
            }else{
                showErrorSnackBar("Your username can not be empty or blank")
            }
        }else{
            showErrorSnackBar("Your name can not be empty or blank")
        }
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
                val uploadedImage = snapshot.child("image").getValue(String::class.java)

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
            myProfileActivity: MyProfileActivity,
            username: String,
            age: String,
            height: String,
            weight: String,
            gender: String,
            name: String,
            uploaded :Boolean
        ) {
            // updating the user via child nodes

            val fs  : FirebaseFirestore = FirebaseFirestore.getInstance()
            if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(name)) {
                myProfileActivity.mFirebaseDatabase!!.child("username").setValue(username.trim())
                myProfileActivity.mFirebaseDatabase!!.child("name").setValue(name.trim())
                myProfileActivity.mFirebaseDatabase!!.child("age").setValue(age.trim())
                myProfileActivity.mFirebaseDatabase!!.child("height").setValue(height.trim())
                myProfileActivity.mFirebaseDatabase!!.child("weight").setValue(weight.trim())
                myProfileActivity.mFirebaseDatabase!!.child("gender").setValue(gender.trim())
             //   Toast.makeText(myProfileActivity.applicationContext, "Successfully updated user", Toast.LENGTH_SHORT).show()
                Toast(myProfileActivity).showCustomToast ("Successfully updated", myProfileActivity)
                fs.collection("users").document(FirebaseAuth.getInstance().currentUser!!.uid).update(
                    "name",name.trim(),
                    "username",username.trim(),
                    "age", age.trim(),
                    "height", height.trim(),
                    "weight", weight.trim(),
                    "gender", gender.trim())
            }
            else
             //   Toast.makeText(myProfileActivity.applicationContext, "Unable to update user", Toast.LENGTH_SHORT).show()
                Toast(myProfileActivity).showCustomToast ("Unable to update", myProfileActivity)
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
