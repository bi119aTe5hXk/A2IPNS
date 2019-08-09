package net.bi119ate5hxk.a2ipns

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import org.xlfdll.android.network.OkHttpStack

internal object AppHelper {
    const val NOTIFICATION_CHANNEL_ID = "net.bi119ate5hxk.a2ipns.notification"
    const val NOTIFICATION_ID = 1

    // Development server: api.sandbox.push.apple.com:443
    // Production server: api.push.apple.com:443
    const val APNSServerURL = "https://api.sandbox.push.apple.com"

    lateinit var Settings: SharedPreferences
    lateinit var HttpRequestQueue: RequestQueue

    fun init(context: Context) {
        if (!(::Settings.isInitialized)) {
            Settings = PreferenceManager.getDefaultSharedPreferences(context)
        }

        if (!(::HttpRequestQueue.isInitialized)) {
            HttpRequestQueue = Volley.newRequestQueue(context, OkHttpStack())
        }
    }
}