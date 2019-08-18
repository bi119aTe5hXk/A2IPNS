package net.bi119ate5hxk.a2ipns

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import kotlinx.android.synthetic.main.activity_main.*

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        setSupportActionBar(findViewById(R.id.actionToolbar))

        this.title = getString(R.string.action_settings)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.settings, SettingsFragment())
            .commit()
    }

    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.preferences, rootKey)

            initializePreferenceButtons()
        }

        private fun initializePreferenceButtons() {
            findPreference<Preference>(getString(R.string.pref_ns_key_pair_device))?.onPreferenceClickListener =
                Preference.OnPreferenceClickListener {
                    startActivity(Intent(requireContext(), QRCodeActivity::class.java))

                    true
                }
            findPreference<Preference>(getString(R.string.pref_ns_key_sync_auth_token))?.onPreferenceClickListener =
                Preference.OnPreferenceClickListener {
                    ViewHelper.updateAPNSAuthToken(requireContext())

                    true
                }
            findPreference<Preference>(getString(R.string.pref_ns_key_select_apps))?.onPreferenceClickListener =
                Preference.OnPreferenceClickListener {
                    startActivity(Intent(requireContext(), AppListActivity::class.java))

                    true
                }
        }
    }
}