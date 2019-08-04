package net.bi119ate5hxk.a2ipns

import android.content.Intent
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import org.json.JSONObject

class NotificationListener : NotificationListenerService() {
    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        val item = NotificationItem(
            sbn?.notification?.extras?.getString("android.title") ?: "",
            sbn?.notification?.extras?.getString("android.text") ?: "",
            sbn?.packageName ?: "<Unknown>"
        )

        val json = generateAppleJson(item)

        val intent = Intent("net.bi119ate5hxk.a2ipns.NOTIFICATION_SERVICE")

        intent.putExtra("notification_item", item)

        sendBroadcast(intent)
    }

    private fun generateAppleJson(item: NotificationItem): String {
        val rootJsonObject = JSONObject()

        rootJsonObject.put("aps", JSONObject())

        val apsJsonObject = rootJsonObject.getJSONObject("aps")

        apsJsonObject.put("alert", JSONObject())

        val alertJsonObject = apsJsonObject.getJSONObject("alert")

        alertJsonObject.put("title", item.Title)
        alertJsonObject.put("subtitle", item.Source)
        alertJsonObject.put("body", item.Text)

        apsJsonObject.put("sound", "default")

        return rootJsonObject.toString()
    }
}