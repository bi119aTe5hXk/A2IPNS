package org.xlfdll.a2pns

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        setSupportActionBar(findViewById(R.id.actionToolbar))

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        this.title = getString(R.string.action_settings)

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.settings, SettingsFragment())
            .commit()
    }

    class SettingsFragment : PreferenceFragmentCompat(),
        SharedPreferences.OnSharedPreferenceChangeListener {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.preferences, rootKey)

            initializePreferenceButtons()
        }

        override fun onSharedPreferenceChanged(
            sharedPreferences: SharedPreferences?,
            key: String?
        ) {
            val customAuthTokenURLKey = getString(R.string.pref_key_custom_auth_token_url)
            val customAuthTokenSecretKey = getString(R.string.pref_key_custom_auth_token_secret)

            when (key) {
                customAuthTokenURLKey -> {
                    if (sharedPreferences?.getString(key, null) == "") {
                        sharedPreferences.edit()
                            .putString(key, null)
                            .putString(customAuthTokenSecretKey, null)
                            .commit()
                    }
                }
            }
        }

        override fun onResume() {
            preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)

            super.onResume()
        }

        override fun onPause() {
            preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)

            super.onPause()
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