package com.silviavaldez.sampleapp.helpers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.support.v4.app.NotificationCompat
import android.support.v4.app.TaskStackBuilder
import android.util.Log
import com.silviavaldez.sampleapp.R
import com.silviavaldez.sampleapp.views.activities.MainActivity

private const val SAMPLE_NOTIFICATION = 1
private const val SAMPLE_NOTIFICATION_CUSTOM = 2

private const val VIBRATION_DELAY = 0L
private const val VIBRATION_DURATION = 300L
private const val LED_MS = 3000

private const val CHANNEL_ID = "SampleApp channel"
private const val CHANNEL_NAME = "Default channel"
private const val CHANNEL_DESCRIPTION = "Default description"

class NotificationHelper(private val context: Context) {

    private val tag = NotificationHelper::class.java.simpleName

    private var notificationManager: NotificationManager? = null

    init {
        initChannels()
    }

    private fun getPendingIntent(): PendingIntent? {
        // Creates an explicit intent for an Activity in your app
        val activity = MainActivity::class.java
        val resultIntent = Intent(context, activity)

        // The stack builder object will contain an artificial back stack for the started Activity.
        // This ensures that navigating backward from the Activity leads out of your app
        // to the Home screen.
        val stackBuilder = TaskStackBuilder.create(context)

        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(activity)

        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent)

        return stackBuilder.getPendingIntent(
                0,
                PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    private fun getRingtoneUri(notificationId: Int): Uri {
        return when (notificationId) {
            SAMPLE_NOTIFICATION -> RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

//            SAMPLE_NOTIFICATION_CUSTOM -> Uri.parse(String.format(Locale.getDefault(),
//                    "android.resource://%s/%d",
//                    context.packageName,
//                    R.raw.police_siren_alert))

            else -> Settings.System.DEFAULT_NOTIFICATION_URI
        }
    }

    fun showDefaultNotification() {
        val title = context.getString(R.string.default_notification_title)
        val message = context.getString(R.string.message_i_do_nothing)

        showNotification(title, message, SAMPLE_NOTIFICATION)
    }

    fun cancelNotification(id: Int): Boolean {
        return try {
            notificationManager!!.cancel(id)
            true
        } catch (e: Exception) {
            Log.e(tag, "Attempting to cancel notification with ID $id", e)
            false
        }
    }

    private fun showNotification(title: String, message: String, id: Int) {
        val pendingIntent = getPendingIntent()
        val ringtoneUri = getRingtoneUri(id)
        val vibrationPattern = longArrayOf(VIBRATION_DELAY,
                VIBRATION_DURATION,
                VIBRATION_DURATION,
                VIBRATION_DURATION,
                VIBRATION_DURATION)

        val notificationBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
        notificationBuilder.setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent)
                .setContentTitle(title)
                .setContentText(message)
                .setColorized(true)
                .setSound(ringtoneUri)
                .setVibrate(vibrationPattern)
                .setLights(Color.RED, LED_MS, LED_MS)
                .setAutoCancel(true)

        notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE)
                as NotificationManager

        // id is a unique integer your app uses to identify the notification.
        // For example, to cancel the notification, you can pass its ID number
        // to NotificationManager.cancel().
        notificationManager!!.notify(id, notificationBuilder.build())
    }

    private fun initChannels() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return
        }

        val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH)
        channel.description = CHANNEL_DESCRIPTION

        notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE)
                as NotificationManager
        notificationManager?.createNotificationChannel(channel)
    }
}