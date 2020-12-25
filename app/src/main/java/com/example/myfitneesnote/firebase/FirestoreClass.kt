package com.example.myfitneesnote.firebase

import android.util.Log
import com.example.myfitneesnote.LoginActivity
import com.example.myfitneesnote.SignUpActivity
import com.example.myfitneesnote.model.User
import com.example.myfitneesnote.utils.Constant
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions


class FirestoreClass {

    private val mFireStore = FirebaseFirestore.getInstance()

    fun registerUser(activity: SignUpActivity, userInfo: User){
        mFireStore.collection(Constant.USERS)
            .document(getCurrentUserId())
                .set(userInfo, SetOptions.merge()).addOnSuccessListener {
                activity.userRegisteredSuccess()
            }
    }

    fun loginUser(activity: LoginActivity){
        mFireStore.collection(Constant.USERS)
            .document(getCurrentUserId())
            .get()
            .addOnSuccessListener { document ->
                val loggedInUser = document.toObject(User::class.java)
                if (loggedInUser != null) {
                    activity.logInSuccess(loggedInUser)
                }
            }.addOnFailureListener{
                e ->
                Log.e("FireStoreLogInUser", "Error writing document")
            }
    }
    fun getCurrentUserId(): String {
        var currentUser= FirebaseAuth.getInstance().currentUser
        var currentUserID = ""
        if (currentUser!= null){
            currentUserID = currentUser.uid
        }
        return currentUserID
    }
}