package net.bi119ate5hxk.a2ipns

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import androidx.core.app.NotificationCompat
import com.android.volley.Request
import com.android.volley.Response
import org.json.JSONObject
import org.xlfdll.android.network.JsonObjectRequestWithCustomHeaders

class NotificationListener : NotificationListenerService() {
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                AppHelper.NOTIFICATION_CHANNEL_ID,
                getString(R.string.notification_channel_name),
                NotificationManager.IMPORTANCE_DEFAULT
            )

            notificationChannel.description = getString(R.string.notification_channel_description)

            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            notificationManager.createNotificationChannel(notificationChannel)
        }

        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0)

        startForeground(
            AppHelper.NOTIFICATION_ID,
            NotificationCompat.Builder(this, AppHelper.NOTIFICATION_CHANNEL_ID)
                .setOngoing(true)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(getString(R.string.notification_running_text))
                .setContentIntent(pendingIntent)
                .build()
        )

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        if (AppHelper.Settings.getBoolean(getString(R.string.pref_key_enable_service), false)) {
            val item = NotificationItem(
                sbn?.notification?.extras?.getString("android.title") ?: "",
                sbn?.notification?.extras?.getString("android.text") ?: "",
                sbn?.packageName ?: "<Unknown>"
            )

            val authToken = AppHelper.Settings.getString(getString(R.string.pref_key_auth_token), null)

            if (authToken != null) {
                val authTokenPackage = JSONObject(authToken)
                val jwt = CryptoHelper.getAPNSBearerToken(authTokenPackage)
                val headers = HashMap<String, String>()

                headers["Authorization"] = "bearer $jwt"
                headers["apns-push-type"] = "alert"
                headers["apns-topic"] = authTokenPackage.getString("id")

                if (jwt != null) {
                    val jsonObject = generateAppleJSONObject(item)
                    val request = JsonObjectRequestWithCustomHeaders(Request.Method.POST,
                        AppHelper.APNSServerURL + "/3/device/${AppHelper.Settings.getString(
                            getString(R.string.pref_key_device_token),
                            ""
                        )}",
                        headers,
                        jsonObject,
                        Response.Listener { response ->
                        },
                        Response.ErrorListener { error ->
                        })

                    AppHelper.HttpRequestQueue.add(request)
                }
            }

            val intent = Intent("net.bi119ate5hxk.a2ipns.NOTIFICATION_SERVICE")

            intent.putExtra("notification_item", item)

            sendBroadcast(intent)
        }
    }

    private fun generateAppleJSONObject(item: NotificationItem): JSONObject {
        val rootJsonObject = JSONObject()

        rootJsonObject.put("aps", JSONObject())

        val apnsJsonObject = rootJsonObject.getJSONObject("aps")

        apnsJsonObject.put("alert", JSONObject())

        val alertJsonObject = apnsJsonObject.getJSONObject("alert")

        alertJsonObject.put("title", item.title)
        alertJsonObject.put("subtitle", item.source)
        alertJsonObject.put("body", item.text)

        apnsJsonObject.put("sound", "default")

        return rootJsonObject
    }
}