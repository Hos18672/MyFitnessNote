package com.example.myfitneesnote.firebase

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_ONE_SHOT
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.myfitneesnote.R
import com.example.myfitneesnote.activities.UsersActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlin.random.Random

class FirebaseService : FirebaseMessagingService() {
    val CHANNEL_ID = "my_notification_channel"
    var firebaseAuth : FirebaseAuth = FirebaseAuth.getInstance()
    var active =""

    companion object{
        var sharedPref: SharedPreferences? = null

        var token:String?
            get(){
                return sharedPref?.getString("token","")
            }
            set(value){
                sharedPref?.edit()?.putString("token",value)?.apply()
            }
    }
    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
        token = p0
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onMessageReceived(p0: RemoteMessage) {
        //    if (userProfileData() == "0"){
        super.onMessageReceived(p0)
        if (FirebaseAuth.getInstance().currentUser != null){
            val fromId = p0.data["fromId"]
            val intent = Intent(this,UsersActivity::class.java)
            intent.putExtra("user_from_id", fromId)
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            val notificationId = Random.nextInt()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                createNotificationChannel(notificationManager)
            }
            val m = userProfileData()
            if (m == "0"){
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                val pendingIntent = PendingIntent.getActivity(this,0,intent,FLAG_ONE_SHOT)
                val notification = NotificationCompat.Builder(this,CHANNEL_ID)
                    .setContentTitle(p0.data["title"])
                    .setContentText(p0.data["message"])
                    .setSmallIcon(R.drawable.ic_baseline_email_24)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .build()

                notificationManager.notify(notificationId,notification)
            }
        }
    }
    private fun userProfileData() : String {
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                active = snapshot.child("inChat").getValue(String::class.java).toString()
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
        return if (active ==""){
            "0"
        }else{
            active
        }

    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager){
        val channelName = "ChannelFirebaseChat"
        val channel = NotificationChannel(CHANNEL_ID,channelName,IMPORTANCE_HIGH).apply {
            description="MY FIREBASE CHAT DESCRIPTION"
            enableLights(true)
            lightColor = Color.WHITE
        }
        notificationManager.createNotificationChannel(channel)

    }
    fun returnMeFCMtoken(): String? {
        val token = arrayOf("")
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isComplete) {
                token[0] = task.result
                Log.e("AppConstants", "onComplete: new Token got: " + token[0])
            }
        }
        return token[0]
    }
}