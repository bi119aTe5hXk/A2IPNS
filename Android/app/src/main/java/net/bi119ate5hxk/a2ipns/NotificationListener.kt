package net.bi119ate5hxk.a2ipns

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.service.notification.NotificationListenerService

class NotificationListener : NotificationListenerService() {
    private val nlServiceReceiver: NLServiceReceiver = NLServiceReceiver()

    override fun onCreate() {
        super.onCreate()

    }

    inner class NLServiceReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {

        }
    }
}