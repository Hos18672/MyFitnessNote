package com.example.myfitneesnote.model

import com.example.myfitneesnote.model.NotificationData

data class PushNotification(
    var data: NotificationData,
    var to:String
)