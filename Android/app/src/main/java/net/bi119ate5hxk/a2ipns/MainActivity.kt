package net.bi119ate5hxk.a2ipns

import android.content.*
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationManagerCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
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

        // Notification listener and receiver initialization
        if (!isNotificationListenerEnabled(applicationContext)) {
            openNotificationListenerSettings()
        }

        val filter = IntentFilter("net.bi119ate5hxk.a2ipns.NOTIFICATION_SERVICE")

        registerReceiver(receiver, filter)

        // App settings
        AppHelper.init(applicationContext)

        enableSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (!isNotificationListenerEnabled(applicationContext)) {
                openNotificationListenerSettings()
            } else if (isChecked && AppHelper.Settings.getString(
                    getString(R.string.pref_key_device_token),
                    null
                ) == null
            ) {
                enableSwitch.isChecked = false

                val builder = AlertDialog.Builder(this)

                builder.setTitle(R.string.alert_no_device_token_title)
                    .setMessage(R.string.alert_no_device_token_message)
                    .setPositiveButton(R.string.alert_button_yes, DialogInterface.OnClickListener { _, _ ->
                        startActivity(Intent(this, QRCodeActivity::class.java))
                    })
                    .setNegativeButton(R.string.alert_button_no, null)
                    .create()
                    .show()
            } else {
                val prefEditor = AppHelper.Settings.edit()

                prefEditor.putBoolean(getString(R.string.pref_key_enable_service), isChecked)
                    .commit()
            }
        }

        initNotificationList()

        // Update APNS authentication token
        ViewHelper.updateAPNSAuthToken(this)

        enableSwitch.isChecked = AppHelper.Settings.getBoolean(getString(R.string.pref_key_enable_service), false)
    }

    override fun onDestroy() {
        unregisterReceiver(receiver)

        super.onDestroy()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.action_bar, menu)

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
        return NotificationManagerCompat.getEnabledListenerPackages(this).contains(context.packageName)
    }

    private fun openNotificationListenerSettings() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP_MR1) {
            startActivity(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS))
        } else {
            startActivity(Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"))
        }
    }

    private fun initNotificationList() {
        notificationRecyclerView.apply {
            setHasFixedSize(true)

            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = notificationItemListAdapter
        }
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
