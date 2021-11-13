package com.example.myfitneesnote.firebase

import android.app.Activity
import android.util.Log
import android.widget.Toast
import com.example.myfitneesnote.*
import com.example.myfitneesnote.model.User
import com.example.myfitneesnote.model.Workout
import com.example.myfitneesnote.utils.Constant
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions


class FirestoreClass {
    private val mFireStore = FirebaseFirestore.getInstance()
    private val db = FirebaseFirestore.getInstance()
    private val workout1 : Workout? = null
    fun registerUser(activity: SignUpActivity, userInfo: User) {
        mFireStore.collection(Constant.USERS)
            .document(getCurrentUserId())
            .set(userInfo, SetOptions.merge()).addOnSuccessListener {
                activity.userRegisteredSuccess()
            }
    }
    fun getCurrentUserId(): String {
        val currentUser = FirebaseAuth.getInstance().currentUser
        var currentUserID = ""
        if (currentUser != null) {
            currentUserID = currentUser.uid
        }
        return currentUserID
    }
    fun createNewTraining(activity: Activity, workout: Workout) {
       mFireStore.collection(Constant.USERS)
            .document(getCurrentUserId()).collection("Workouts").add(workout)
            .addOnSuccessListener {
                Log.e(activity.javaClass.simpleName, "Created Successfully")
                Toast.makeText(activity, "Training created Successfully", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener{
                    exception ->
                Log.e(activity.javaClass.simpleName, "Creation failed",exception)
                Toast.makeText(activity, "Training's creation failed", Toast.LENGTH_SHORT).show()
            }
    }
}