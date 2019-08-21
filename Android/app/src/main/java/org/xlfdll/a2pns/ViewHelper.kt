package org.xlfdll.a2pns

import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest

internal object ViewHelper {
    fun updateAPNSAuthToken(context: Context) {
        val url = AppHelper.Settings.getString(
            context.getString(R.string.pref_key_custom_auth_token_url),
            ExternalData.APNSAuthTokenURL
        )
        val secret = AppHelper.Settings.getString(
            context.getString(R.string.pref_key_custom_auth_token_secret),
            ExternalData.DecryptionSecret
        )

        val request = JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->
                if (response.getString("time") != AppHelper.Settings.getString(
                        context.getString(R.string.pref_key_auth_token_update_date),
                        null
                    )
                ) {
                    val certData = response.getString("cert").split(":")
                    val token =
                        CryptoHelper.decrypt(
                            certData[0],
                            certData[1],
                            certData[2],
                            ExternalData.DecryptionSecret
                        )

                    val prefEditor = AppHelper.Settings.edit()

                    prefEditor.putString(context.getString(R.string.pref_key_auth_token), token)
                        .putString(
                            context.getString(R.string.pref_key_auth_token_update_date),
                            response.getString("time")
                        )
                        .commit()

                    Toast.makeText(
                        context,
                        context.getString(R.string.toast_token_updated_message),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                } else {
                    Toast.makeText(
                        context,
                        context.getString(R.string.toast_token_no_need_to_update_message),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            },
            Response.ErrorListener { error ->
                AlertDialog.Builder(context)
                    .setMessage(R.string.alert_token_download_error_message)
                    .setPositiveButton("OK", null)
                    .show()
            })

        AppHelper.HttpRequestQueue.add(request)
    }
}