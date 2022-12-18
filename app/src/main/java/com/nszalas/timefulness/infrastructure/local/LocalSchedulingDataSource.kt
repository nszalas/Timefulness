package com.nszalas.timefulness.infrastructure.local

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.nszalas.timefulness.utils.DateTimeProvider
import javax.inject.Inject

const val STATUS_REMINDER_ACTION = "STATUS_REMINDER_ACTION"
const val STATUS_REMINDER_CODE = 0

class LocalSchedulingDataSource @Inject constructor(
    private val context: Context,
    private val alarmManager: AlarmManager,
    private val dateTimeProvider: DateTimeProvider
) {
    fun setTaskReminder(triggerAtMillis: Long) = try {
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            triggerAtMillis,
            getTaskReminderPendingIntent()
        )
    } catch (e: Exception) {
        // todo
    }

    fun cancelTaskReminder() = try {
        alarmManager.cancel(getTaskReminderPendingIntent())
    } catch (e: Exception) {
        // todo
    }

    private fun getTaskReminderPendingIntent(): PendingIntent {
        val intent = Intent(context, NotificationTaskReminderReceiver::class.java)
        intent.action = STATUS_REMINDER_ACTION

        return PendingIntent.getBroadcast(
            context,
            STATUS_REMINDER_CODE,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
    }
}