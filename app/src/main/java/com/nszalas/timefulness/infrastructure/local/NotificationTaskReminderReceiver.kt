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

        if(intent?.action == TASK_REMINDER_ACTION) {
            val title = intent.getStringExtra(NOTIFICATION_EXTRA_TITLE) ?: "Timefulness"
            val id = intent.getIntExtra(NOTIFICATION_EXTRA_ID, 0)
            val altContent = "Nadchodzące wydarzenie"
            val body = intent.getStringExtra(NOTIFICATION_EXTRA_TEXT) ?: altContent
            sendNotification(
                NotificationData(
                    id = id,
                    title = title,
                    body = body,
                    alternativeContent = altContent,
                    smallIconRes = R.drawable.ic_launcher_foreground
                )
            )
        }
    }

}