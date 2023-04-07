package com.github.mat127.septik.ui.settings

import android.os.Bundle
import android.text.InputType
import androidx.preference.EditTextPreference
import androidx.preference.PreferenceFragmentCompat
import com.github.mat127.septik.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
        findPreference<EditTextPreference>("volume")?.let(::numberOnly)
        findPreference<EditTextPreference>("water_price")?.let(::numberOnly)
        findPreference<EditTextPreference>("empting_price")?.let(::numberOnly)
    }

    private fun numberOnly(preference: EditTextPreference) {
        preference.setOnBindEditTextListener {
            it.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
        }
    }
}