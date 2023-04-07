package com.github.mat127.septik.ui.settings

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.github.mat127.septik.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }
}