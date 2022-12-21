package com.nszalas.timefulness.infrastructure.local

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.nszalas.timefulness.MainActivity
import com.nszalas.timefulness.R
import com.nszalas.timefulness.domain.model.NotificationData
import com.nszalas.timefulness.extensions.millis
import com.nszalas.timefulness.utils.DateTimeProvider
import javax.inject.Inject

private const val NOTIFICATION_CHANNEL_ID = "NOTIFICATION_CHANNEL_TASK"
private const val NOTIFICATION_ACTION_REQUEST_CODE = 1001
private const val NOTIFICATION_CHANNEL_NAME = "Task"

class LocalNotificationDataSource @Inject constructor(
    private val context: Context,
    private val dateTimeProvider: DateTimeProvider,
) {
    private val notificationManager by lazy {
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    private val notificationAction by lazy {
        PendingIntent.getActivity(
            context,
            NOTIFICATION_ACTION_REQUEST_CODE,
            Intent(context, MainActivity::class.java),
            PendingIntent.FLAG_IMMUTABLE
        )
    }

    fun sendNotification(notificationData: NotificationData) {
        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            NOTIFICATION_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        )
        val notification = context.getNotification(
            channel,
            notificationData
        )
        with(notificationManager) {
            createNotificationChannel(channel)
            notify(notificationData.id, notification)
        }
    }

    private fun Context.getNotification(
        channel: NotificationChannel,
        notificationData: NotificationData
    ): Notification = with(notificationData) {
        Notification.Builder(context, channel.id).apply {
            setContentTitle(title)
            setContentText(body ?: alternativeContent)
            style = Notification.BigTextStyle().bigText(body)
            setSmallIcon(R.drawable.ic_launcher_foreground)
            setShowWhen(true)
            setWhen(dateTimeProvider.currentDateTime().millis())
            setColor(applicationContext.getColor(R.color.greenMain))
            setColorized(true)
            setContentIntent(notificationAction)
            setAutoCancel(true)
        }.build()
    }
}