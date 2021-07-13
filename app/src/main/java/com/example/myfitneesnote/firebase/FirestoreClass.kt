package com.example.myfitneesnote.firebase

import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.myfitneesnote.*
import com.example.myfitneesnote.model.User
import com.example.myfitneesnote.model.Workout
import com.example.myfitneesnote.utils.Constant
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.handleCoroutineException

class FirestoreClass {

    private val mFireStore = FirebaseFirestore.getInstance()

    fun registerUser(activity: SignUpActivity, userInfo: User) {
        mFireStore.collection(Constant.USERS)
            .document(getCurrentUserId())
            .set(userInfo, SetOptions.merge()).addOnSuccessListener {
                activity.userRegisteredSuccess()
            }
    }


    fun loginUser(activity: Activity) {
        mFireStore.collection(Constant.USERS)
            .document(getCurrentUserId())
            .get()
            .addOnSuccessListener { document ->
                val loggedInUser = document.toObject(User::class.java)
                when (activity) {
                    is LoginActivity -> {
                        if (loggedInUser != null) {
                            activity.logInSuccess(loggedInUser)
                        }
                    }
                    is MainActivity -> {
                        if (loggedInUser != null) {
                           // activity.updateNavigationUserDetails(loggedInUser)
                        }
                    }
                }
            }.addOnFailureListener { e ->
                when (activity) {
                    is LoginActivity -> {
                        activity.hideProgressDialog1()
                    }
                    is MainActivity -> {
                        activity.hideProgressDialog1()
                    }
                }
                Log.e("FireStoreLogInUser", "Error writing document")
            }
    }


    fun getCurrentUserId(): String {
        var currentUser = FirebaseAuth.getInstance().currentUser
        var currentUserID = ""
        if (currentUser != null) {
            currentUserID = currentUser.uid
        }
        return currentUserID
    }

    fun createNewTraining(activity: AddWorkoutActivity, workout: Workout){

        mFireStore.collection(Constant.TRAININGS)
            .document()
            .set(workout, SetOptions.merge())
            .addOnSuccessListener {
                Log.e(activity.javaClass.simpleName, "Created Successfully")
                Toast.makeText(activity, "Training created Successfully", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener{
                exception ->
                Log.e(activity.javaClass.simpleName, "Creation failed",exception)
                Toast.makeText(activity, "Training's creation failed", Toast.LENGTH_SHORT).show()
            }
    }

    fun getWorkout(activity: TrainingActivity){
        mFireStore.collection(Constant.TRAININGS)
            .whereArrayContains(Constant.USER_ID, getCurrentUserId())
            .get()
            .addOnSuccessListener {
                document ->
                Log.i(activity.javaClass.simpleName, document.documents.toString())
                val workoutList : ArrayList<Workout> = ArrayList()
                for(i in document.documents){
                    val workout= i.toObject(Workout::class.java)!!
                    workout.documentId= i.id
                    workoutList.add(workout)
                }
                activity.populateWorkoutListToUi(workoutList)
            }.addOnFailureListener{
                    exception ->
                Log.e(activity.javaClass.simpleName, "get Workouts failed",exception)

            }
    }
}