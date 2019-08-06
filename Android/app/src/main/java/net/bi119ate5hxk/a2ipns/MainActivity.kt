package net.bi119ate5hxk.a2ipns

import android.Manifest
import android.content.*
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.provider.Settings
import android.view.View
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    val notificationItemList = ArrayList<NotificationItem>()
    val notificationItemListAdapter = NotificationListAdapter(notificationItemList)
    val receiver = NotificationServiceReceiver()

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
        AppHelper.Settings = PreferenceManager.getDefaultSharedPreferences(applicationContext)

        findViewById<Switch>(R.id.enableSwitch).setOnCheckedChangeListener { _, isChecked ->
            if (isChecked && AppHelper.Settings!!.getString(getString(R.string.pref_key_device_token), null) == null) {
                val builder = AlertDialog.Builder(this)

                builder.setTitle(R.string.alert_no_device_token_title)
                    .setMessage(R.string.alert_no_device_token_message)
                    .setPositiveButton(R.string.alert_button_yes, DialogInterface.OnClickListener { _, _ ->

                    })
                    .setNegativeButton(R.string.alert_button_no, null)
                    .create()
            }

            if (isChecked && AppHelper.Settings!!.getString(getString(R.string.pref_key_device_token), null) != null) {
                val prefEditor = AppHelper.Settings!!.edit()

                prefEditor.putBoolean(getString(R.string.pref_key_enable_service), isChecked)
                    .commit()
            } else if (isChecked) {
                enableSwitch.isChecked = false
            }
        }

        initNotificationList()

        // Update APS authentication token
        updateAPSAuthToken()
    }

    fun pairiOSDeviceAction(view: View) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(arrayOf(Manifest.permission.CAMERA), 0)
        }

        val intent = Intent(this, QRCodeActivity::class.java)

        startActivity(intent)
    }

    fun updateAPSAuthTokenAction(view: View) {
        updateAPSAuthToken()
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
        findViewById<RecyclerView>(R.id.notificationRecyclerView).apply {
            setHasFixedSize(true)

            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = notificationItemListAdapter
        }
    }

    private fun updateAPSAuthToken() {
        mainLinearLayout.isEnabled = false

        val queue = Volley.newRequestQueue(this)
        val stringRequest =
            StringRequest(Request.Method.GET, ExternalData.APSAuthTokenURL, Response.Listener<String> { response ->
                val jsonObject = JSONObject(response)

                if (jsonObject.getString("time") != AppHelper.Settings!!.getString(
                        getString(R.string.pref_key_auth_token_update_date),
                        null
                    )
                ) {
                    val certData = jsonObject.getString("cert").split(":")
                    val token =
                        CryptoHelper.decrypt(certData[0], certData[1], certData[2], ExternalData.DecryptionSecret)

                    val prefEditor = AppHelper.Settings!!.edit()

                    prefEditor.putString(getString(R.string.pref_key_auth_token), token)
                        .putString(
                            getString(R.string.pref_key_auth_token_update_date),
                            jsonObject.getString("time")
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
                Response.ErrorListener {
                    AlertDialog.Builder(this)
                        .setMessage(R.string.alert_token_download_error_message)
                        .setPositiveButton("OK", null)
                        .show()
                })

        queue.add(stringRequest)
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
