package net.bi119ate5hxk.a2ipns

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.budiyev.android.codescanner.*
import org.json.JSONException
import org.json.JSONObject

class QRCodeActivity : AppCompatActivity() {
    private lateinit var codeScanner: CodeScanner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qrcode)

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(arrayOf(Manifest.permission.CAMERA), 0)
        }

        val scannerView = findViewById<CodeScannerView>(R.id.scanner_view)

        codeScanner = CodeScanner(this, scannerView)

        // Parameters (default values)
        codeScanner.camera = CodeScanner.CAMERA_BACK // or CAMERA_FRONT or specific camera id
        codeScanner.formats = CodeScanner.ALL_FORMATS // list of type BarcodeFormat,
        // ex. listOf(BarcodeFormat.QR_CODE)
        codeScanner.autoFocusMode = AutoFocusMode.SAFE // or CONTINUOUS
        codeScanner.scanMode = ScanMode.SINGLE // or CONTINUOUS or PREVIEW
        codeScanner.isAutoFocusEnabled = true // Whether to enable auto focus or not
        codeScanner.isFlashEnabled = false // Whether to enable flash or not

        // Callbacks
        codeScanner.decodeCallback = DecodeCallback {
            runOnUiThread {
                val token = getDeviceToken(it.text)

                if (token != null) {
                    val prefEditor = AppHelper.Settings.edit()

                    prefEditor.putString(getString(R.string.pref_key_device_token), token)
                        .apply()

                    Toast.makeText(
                        this,
                        getString(R.string.toast_device_token_pair_success_message),
                        Toast.LENGTH_SHORT
                    ).show()

                    finish()
                } else {
                    codeScanner.startPreview()
                }
            }
        }
        codeScanner.errorCallback = ErrorCallback {
            // or ErrorCallback.SUPPRESS
            runOnUiThread {
                Toast.makeText(
                    this, getString(R.string.toast_device_token_camera_init_error) + it.message,
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        scannerView.setOnClickListener {
            codeScanner.startPreview()
        }
    }

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }

    private fun getDeviceToken(json: String): String? {
        try {
            val jsonObject = JSONObject(json)

            when (jsonObject.getString("id") == getString(R.string.app_name)) {
                true -> return jsonObject.getString("token")
                false -> return null
            }
        } catch (_: JSONException) {
            return null
        }
    }
}
