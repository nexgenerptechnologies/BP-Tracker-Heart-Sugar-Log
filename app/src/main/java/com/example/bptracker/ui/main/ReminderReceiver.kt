package com.example.bptracker.ui.main

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.example.bptracker.MainActivity
import com.example.bptracker.R
import com.example.bptracker.data.FileStorageRepository

class ReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val title = intent.getStringExtra("TITLE") ?: "Health Reminder"
        val notificationId = intent.getIntExtra("NOTIFICATION_ID", 1)
        val isSticky = intent.getBooleanExtra("IS_STICKY", false)

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Create Channel (Android O+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "bp_tracker_channel",
                "Health Reminders",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Reminders for blood pressure, sugar, and medications"
            }
            notificationManager.createNotificationChannel(channel)
        }
        
        // Fetch data
        val repo = FileStorageRepository(context)
        val vitals = repo.vitals.value
        
        val latestBp = vitals.firstOrNull { it.systolic > 0 }
        val latestHr = vitals.firstOrNull { it.heartRate > 0 }
        val latestSugar = vitals.firstOrNull { it.bloodSugar > 0 }
        
        val bpStr = if (latestBp != null) "${latestBp.systolic}/${latestBp.diastolic}" else "--/--"
        val hrStr = if (latestHr != null) "${latestHr.heartRate}" else "--"
        val sugarStr = if (latestSugar != null) "${latestSugar.bloodSugar}" else "--"

        val remoteViews = RemoteViews(context.packageName, R.layout.notification_rich_reminder)
        remoteViews.setTextViewText(R.id.tv_bp_value, bpStr)
        remoteViews.setTextViewText(R.id.tv_hr_value, hrStr)
        remoteViews.setTextViewText(R.id.tv_sugar_value, sugarStr)

        // Intents for buttons
        val bpIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("START_DESTINATION", "add_bp")
        }
        val hrIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("START_DESTINATION", "add_hr")
        }
        val sugarIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("START_DESTINATION", "add_sugar")
        }

        val pendingFlags = PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        
        val bpPending = PendingIntent.getActivity(context, notificationId * 10 + 1, bpIntent, pendingFlags)
        val hrPending = PendingIntent.getActivity(context, notificationId * 10 + 2, hrIntent, pendingFlags)
        val sugarPending = PendingIntent.getActivity(context, notificationId * 10 + 3, sugarIntent, pendingFlags)

        remoteViews.setOnClickPendingIntent(R.id.btn_record_bp, bpPending)
        remoteViews.setOnClickPendingIntent(R.id.panel_bp, bpPending)
        
        remoteViews.setOnClickPendingIntent(R.id.btn_measure_hr, hrPending)
        remoteViews.setOnClickPendingIntent(R.id.panel_hr, hrPending)
        
        remoteViews.setOnClickPendingIntent(R.id.btn_record_sugar, sugarPending)
        remoteViews.setOnClickPendingIntent(R.id.panel_sugar, sugarPending)
        
        // Meds Intent
        val medsIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("START_DESTINATION", "add_meds")
        }
        val medsPending = PendingIntent.getActivity(context, notificationId * 10 + 4, medsIntent, pendingFlags)
        remoteViews.setOnClickPendingIntent(R.id.btn_record_meds, medsPending)
        remoteViews.setOnClickPendingIntent(R.id.panel_meds, medsPending)

        // Show/Hide specific panels based on the reminder
        // 1: BP, 2: Sugar, 3: HR, 4: Meds. 999: Sticky (show all)
        if (!isSticky && notificationId in 1..4) {
            remoteViews.setViewVisibility(R.id.panel_bp, if (notificationId == 1) android.view.View.VISIBLE else android.view.View.GONE)
            remoteViews.setViewVisibility(R.id.panel_sugar, if (notificationId == 2) android.view.View.VISIBLE else android.view.View.GONE)
            remoteViews.setViewVisibility(R.id.panel_hr, if (notificationId == 3) android.view.View.VISIBLE else android.view.View.GONE)
            remoteViews.setViewVisibility(R.id.panel_meds, if (notificationId == 4) android.view.View.VISIBLE else android.view.View.GONE)
        } else {
            // Show all 4 for sticky by default
            remoteViews.setViewVisibility(R.id.panel_bp, android.view.View.VISIBLE)
            remoteViews.setViewVisibility(R.id.panel_sugar, android.view.View.VISIBLE)
            remoteViews.setViewVisibility(R.id.panel_hr, android.view.View.VISIBLE)
            remoteViews.setViewVisibility(R.id.panel_meds, android.view.View.VISIBLE)
        }

        // General tap intent
        val mainIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(context, notificationId, mainIntent, pendingFlags)

        // Build Notification
        val builder = NotificationCompat.Builder(context, "bp_tracker_channel")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setCustomContentView(remoteViews)
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(!isSticky)
            .setOngoing(isSticky)
            .setContentIntent(pendingIntent)

        notificationManager.notify(notificationId, builder.build())
    }
}
