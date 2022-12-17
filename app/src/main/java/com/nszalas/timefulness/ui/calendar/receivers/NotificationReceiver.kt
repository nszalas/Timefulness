package com.nszalas.timefulness.ui.calendar.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.PowerManager
import com.nszalas.timefulness.ui.calendar.utils.EVENT_ID
import com.nszalas.timefulness.ui.calendar.utils.eventsDB
import com.nszalas.timefulness.ui.calendar.utils.scheduleNextEventReminder
import com.simplemobiletools.commons.helpers.ensureBackgroundThread

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
        val wakelock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "simplecalendar:notificationreceiver")
        wakelock.acquire(3000)

        ensureBackgroundThread {
            handleIntent(context, intent)
        }
    }

    private fun handleIntent(context: Context, intent: Intent) {
        val id = intent.getLongExtra(EVENT_ID, -1L)
        if (id == -1L) {
            return
        }

        val event = context.eventsDB.getEventOrTaskWithId(id) ?: return

        context.scheduleNextEventReminder(event, false)
    }
}
