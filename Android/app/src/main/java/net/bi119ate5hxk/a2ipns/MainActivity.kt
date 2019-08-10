package net.bi119ate5hxk.a2ipns

import android.content.*
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationManagerCompat
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

        // Notification listener and receiver initialization
        if (!isNotificationListenerEnabled(applicationContext)) {
            openNotificationListenerSettings()
        }

        val filter = IntentFilter("net.bi119ate5hxk.a2ipns.NOTIFICATION_SERVICE")

        registerReceiver(receiver, filter)

        // App settings
        AppHelper.init(applicationContext)

        enableSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked && AppHelper.Settings.getString(getString(R.string.pref_key_device_token), null) == null) {
                enableSwitch.isChecked = false

                val builder = AlertDialog.Builder(this)

                builder.setTitle(R.string.alert_no_device_token_title)
                    .setMessage(R.string.alert_no_device_token_message)
                    .setPositiveButton(R.string.alert_button_yes, DialogInterface.OnClickListener { _, _ ->
                        pairiOSDeviceAction(enableSwitch)
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
        updateAPNSAuthToken()

        enableSwitch.isChecked = AppHelper.Settings.getBoolean(getString(R.string.pref_key_enable_service), false)
    }

    override fun onDestroy() {
        unregisterReceiver(receiver)

        super.onDestroy()
    }

    fun pairiOSDeviceAction(view: View) {
        val intent = Intent(this, QRCodeActivity::class.java)

        startActivity(intent)
    }

    fun updateAPNSAuthTokenAction(view: View) {
        updateAPNSAuthToken()
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

    private fun updateAPNSAuthToken() {
        mainLinearLayout.isEnabled = false

        val request = JsonObjectRequest(Request.Method.GET, ExternalData.APNSAuthTokenURL, null,
            Response.Listener { response ->
                if (response.getString("time") != AppHelper.Settings.getString(
                        getString(R.string.pref_key_auth_token_update_date),
                        null
                    )
                ) {
                    val certData = response.getString("cert").split(":")
                    val token =
                        CryptoHelper.decrypt(certData[0], certData[1], certData[2], ExternalData.DecryptionSecret)

                    val prefEditor = AppHelper.Settings.edit()

                    prefEditor.putString(getString(R.string.pref_key_auth_token), token)
                        .putString(
                            getString(R.string.pref_key_auth_token_update_date),
                            response.getString("time")
                        )
                        .commit()

                    Toast.makeText(this, getString(R.string.toast_token_updated_message), Toast.LENGTH_SHORT)
                        .show()
                } else {
                    Toast.makeText(
                        this,
                        getString(R.string.toast_token_no_need_to_update_message),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            },
            Response.ErrorListener { error ->
                AlertDialog.Builder(this)
                    .setMessage(R.string.alert_token_download_error_message)
                    .setPositiveButton("OK", null)
                    .show()
            })

        AppHelper.HttpRequestQueue.add(request)
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
