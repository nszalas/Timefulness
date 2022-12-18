package com.nszalas.timefulness.infrastructure.local

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.nszalas.timefulness.R
import com.nszalas.timefulness.domain.model.NotificationData
import com.nszalas.timefulness.domain.usecase.SendNotificationUseCase
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class NotificationTaskReminderReceiver: BroadcastReceiver() {

    @Inject
    lateinit var sendNotification: SendNotificationUseCase

    override fun onReceive(context: Context?, intent: Intent?) {

        if(intent?.action == STATUS_REMINDER_ACTION) {
            sendNotification(
                NotificationData(
                    title = "title",
                    body = "body",
                    alternativeContent = "alt content",
                    smallIconRes = R.drawable.ic_launcher_foreground
                )
            )
        }
    }

}