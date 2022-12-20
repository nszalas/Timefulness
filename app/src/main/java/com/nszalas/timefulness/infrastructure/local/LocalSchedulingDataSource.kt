package com.nszalas.timefulness.infrastructure.local

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.nszalas.timefulness.domain.model.Task
import com.nszalas.timefulness.extensions.asLocalDateTime
import com.nszalas.timefulness.utils.TimeFormatter
import java.time.ZoneId
import javax.inject.Inject

const val TASK_REMINDER_ACTION = "TASK_REMINDER_ACTION"
const val NOTIFICATION_EXTRA_TITLE = "NOTIFICATION_EXTRA_TITLE"
const val NOTIFICATION_EXTRA_TEXT = "NOTIFICATION_EXTRA_TEXT"
const val NOTIFICATION_EXTRA_ID = "NOTIFICATION_EXTRA_ID"

class LocalSchedulingDataSource @Inject constructor(
    private val context: Context,
    private val alarmManager: AlarmManager,
    private val timeFormatter: TimeFormatter,
) {
    fun setTaskReminder(triggerAtMillis: Long, task: Task) = try {
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            triggerAtMillis,
            getTaskReminderPendingIntent(task)
        )
    } catch (e: Exception) {
        // todo
    }

    fun cancelTaskReminder(task: Task) = try {
        alarmManager.cancel(getTaskReminderPendingIntent(task))
    } catch (e: Exception) {
        // todo
    }

    private fun getTaskReminderPendingIntent(task: Task): PendingIntent {
        val startTime = task.startTimestamp
            ?.asLocalDateTime(task.timezoneId ?: ZoneId.systemDefault().id)
            ?.toLocalTime()
        val endTime = task.endTimestamp
            ?.asLocalDateTime(task.timezoneId ?: ZoneId.systemDefault().id)
            ?.toLocalTime()
        val notificationText = when {
            startTime != null && endTime != null -> {
                StringBuilder().apply {
                    append(timeFormatter.formatTime(startTime))
                    append(" - ")
                    append(timeFormatter.formatTime(endTime))
                }.toString()
            }
            else -> "Nadchodzące wydarzenie"
        }

        val intent = Intent(context, NotificationTaskReminderReceiver::class.java)
        intent.action = TASK_REMINDER_ACTION
        intent.putExtra(NOTIFICATION_EXTRA_TITLE, task.title)
        intent.putExtra(NOTIFICATION_EXTRA_TEXT, notificationText)
        intent.putExtra(NOTIFICATION_EXTRA_ID, task.id)

        return PendingIntent.getBroadcast(
            context,
            task.id,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_CANCEL_CURRENT
        )
    }
}