package org.xlfdll.a2pns

import android.content.Intent
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import org.json.JSONObject
import org.xlfdll.android.network.JsonObjectRequestWithCustomHeaders

class NotificationListener : NotificationListenerService() {
    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        if (AppHelper.Settings.getBoolean(getString(R.string.pref_key_enable_service), false)) {
            var source = sbn?.packageName

            if (source != null) {
                source = packageManager.getPackageInfo(source, 0).applicationInfo.loadLabel(
                    packageManager
                ).toString()
            }

            val item = NotificationItem(
                sbn?.notification?.extras?.getString("android.title") ?: "",
                sbn?.notification?.extras?.getString("android.text") ?: "",
                source ?: "<Unknown>",
                sbn?.packageName ?: ""
            )

            if (AppHelper.Settings.getStringSet(
                    getString(R.string.pref_key_selected_apps),
                    null
                )?.contains(item.packageName) == true
            ) {
                val authToken =
                    AppHelper.Settings.getString(getString(R.string.pref_key_auth_token), null)

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

                        if (!ExternalData.MockDebugMode) {
                            AppHelper.HttpRequestQueue.add(request)
                        }

                        Log.i(getString(R.string.app_name), "Message from ${item.source}")
                    }
                }

                val intent = Intent("org.xlfdll.a2pns.NOTIFICATION_SERVICE")

                intent.putExtra("notification_item", item)

                sendBroadcast(intent)
            }
        }

        super.onNotificationPosted(sbn)
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

        apnsJsonObject.put("content-available", 1)
        apnsJsonObject.put("mutable-content", 1)
        apnsJsonObject.put("sound", "default")

        rootJsonObject.put("package", item.packageName)

        return rootJsonObject
    }
}