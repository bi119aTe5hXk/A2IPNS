package net.bi119ate5hxk.a2ipns

import android.app.Notification
import android.content.Intent
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import androidx.localbroadcastmanager.content.LocalBroadcastManager

class NotificationListener : NotificationListenerService() {
    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        val item = NotificationItem(
            sbn?.notification?.extras?.getString("android.title") ?: "",
            sbn?.notification?.extras?.getString("android.text") ?: "",
            sbn?.packageName ?: "<Unknown>"
        )
        val intent = Intent("net.bi119ate5hxk.a2ipns.NOTIFICATION_SERVICE")

        intent.putExtra("notification_item", item)

        sendBroadcast(intent)
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification?) {
        super.onNotificationRemoved(sbn)
    }
}