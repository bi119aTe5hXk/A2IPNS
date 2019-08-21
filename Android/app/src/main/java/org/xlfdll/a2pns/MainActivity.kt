package org.xlfdll.a2pns

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.*
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    val notificationItemList = ArrayList<NotificationItem>()
    val notificationItemListAdapter = NotificationListAdapter(notificationItemList)
    private val receiver = NotificationServiceReceiver()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(findViewById(R.id.actionToolbar))

        // Has to set title here, as manifest's label attributes do not work consistently
        this.title = getString(R.string.app_title)

        if (!AppHelper.isLaunched) {
            // App settings
            AppHelper.init(applicationContext)

            // Update APNS authentication token
            ViewHelper.updateAPNSAuthToken(this)

            AppHelper.isLaunched = true
        }

        // Notification listener and receiver initialization
        if (!isNotificationListenerEnabled(applicationContext)) {
            openNotificationListenerSettings()
        }

        showDeviceTokenPrompt()

        val filter = IntentFilter("org.xlfdll.a2pns.NOTIFICATION_SERVICE")

        registerReceiver(receiver, filter)

        initNotificationList()

        enableSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (!isNotificationListenerEnabled(applicationContext)) {
                openNotificationListenerSettings()
            } else if (isChecked && AppHelper.Settings.getString(
                    getString(R.string.pref_key_device_token),
                    null
                ) == null
            ) {
                enableSwitch.isChecked = false

                showDeviceTokenPrompt()
            } else {
                val prefEditor = AppHelper.Settings.edit()

                prefEditor.putBoolean(getString(R.string.pref_key_enable_service), isChecked)
                    .commit()

                if (isChecked) {
                    showNotificationIcon()
                } else {
                    hideNotificationIcon()
                }
            }
        }

        enableSwitch.isChecked =
            AppHelper.Settings.getBoolean(getString(R.string.pref_key_enable_service), false)
    }

    override fun onDestroy() {
        unregisterReceiver(receiver)

        super.onDestroy()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.action_bar_main, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_settings -> startActivity(Intent(this, SettingsActivity::class.java))
        }

        return super.onOptionsItemSelected(item)
    }

    fun clearHistoryAction(view: View) {
        notificationItemList.clear()
        notificationItemListAdapter.notifyDataSetChanged()
    }

    private fun isNotificationListenerEnabled(context: Context): Boolean {
        return NotificationManagerCompat.getEnabledListenerPackages(this)
            .contains(context.packageName)
    }

    private fun openNotificationListenerSettings() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP_MR1) {
            startActivity(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS))
        } else {
            startActivity(Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"))
        }
    }

    private fun showDeviceTokenPrompt() {
        if (AppHelper.Settings.getString(
                getString(R.string.pref_key_device_token),
                null
            ) == null
        ) {
            val builder = AlertDialog.Builder(this)

            builder.setTitle(R.string.alert_no_device_token_title)
                .setMessage(R.string.alert_no_device_token_message)
                .setPositiveButton(
                    R.string.alert_button_yes,
                    DialogInterface.OnClickListener { _, _ ->
                        startActivity(Intent(this, QRCodeActivity::class.java))
                    })
                .setNegativeButton(R.string.alert_button_no, null)
                .create()
                .show()
        }
    }

    private fun initNotificationList() {
        notificationRecyclerView.apply {
            setHasFixedSize(true)

            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = notificationItemListAdapter
        }
    }

    private fun showNotificationIcon() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                AppHelper.NOTIFICATION_CHANNEL_ID,
                getString(R.string.notification_channel_name),
                NotificationManager.IMPORTANCE_DEFAULT
            )

            notificationChannel.description = getString(R.string.notification_channel_description)

            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            notificationManager.createNotificationChannel(notificationChannel)
        }

        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0)

        val notification = NotificationCompat.Builder(this, AppHelper.NOTIFICATION_CHANNEL_ID)
            .setOngoing(true)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(getString(R.string.app_title))
            .setContentText(getString(R.string.notification_running_text))
            .setContentIntent(pendingIntent)
            .build()

        notification.flags =
            notification.flags or Notification.FLAG_NO_CLEAR or Notification.FLAG_ONGOING_EVENT

        val notifier = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notifier.notify(AppHelper.NOTIFICATION_ID, notification)
    }

    private fun hideNotificationIcon() {
        val notifier =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notifier.cancel(AppHelper.NOTIFICATION_ID)
    }

    inner class NotificationServiceReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val item = intent?.getParcelableExtra<NotificationItem>("notification_item")

            if (item != null) {
                notificationItemList.add(0, item)
                notificationItemListAdapter.notifyDataSetChanged()
            }
        }
    }
}
