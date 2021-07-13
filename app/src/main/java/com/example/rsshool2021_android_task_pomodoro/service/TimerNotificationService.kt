package com.example.rsshool2021_android_task_pomodoro.service

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.rsshool2021_android_task_pomodoro.PomodoroApp.Companion.CHANNEL_ID
import com.example.rsshool2021_android_task_pomodoro.R
import com.example.rsshool2021_android_task_pomodoro.model.Timer
import com.example.rsshool2021_android_task_pomodoro.model.dispatchers.TimerDispatcher
import com.example.rsshool2021_android_task_pomodoro.ui.MainActivity

class TimerNotificationService : Service(), Timer.OnTimeUpdate {

    private var notification: Notification? = null

    override fun onCreate() {
        super.onCreate()
        TimerDispatcher.getTimer().listener = this
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground(1, createNotification(TimerDispatcher.getTimer().updatableStringTimer))
        return START_NOT_STICKY
    }

    override fun onUpdate(time: String) {
        startForeground(1, createNotification(TimerDispatcher.getTimer().updatableStringTimer))
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun createNotification(input: String): Notification {
        val intentActivity = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, intentActivity, 0)
        notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Timer is running")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentText(input)
            .setContentIntent(pendingIntent)
            .build()
        return notification as Notification
    }

}