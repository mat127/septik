package com.github.mat127.septik.ui

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.preference.PreferenceManager
import com.github.mat127.septik.R
import com.github.mat127.septik.SeptikApplication
import com.github.mat127.septik.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.bottomNavigationView

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        val navController = navHostFragment.navController
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(setOf(
            R.id.navigation_state, R.id.navigation_costs, R.id.navigation_settings))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        this.setupPreferences(preferences)
    }

    fun addState() {
        val dialog = AddStateDialog()
        dialog.show(supportFragmentManager, "add-state")
    }

    fun addEmptyTimestamp() {
        val dialog = AddEmptyDialog()
        dialog.show(supportFragmentManager, "add-empty")
    }

    private val preferences get() =
        PreferenceManager.getDefaultSharedPreferences(this /* Activity context */)

    private fun setupPreferences(preferences: SharedPreferences) {
        listOf(
            getString(R.string.preference_key_volume),
            getString(R.string.preference_key_water_price),
            getString(R.string.preference_key_empting_price)
        ).forEach {
            preferencesListener.onSharedPreferenceChanged(preferences, it)
        }
        preferences.registerOnSharedPreferenceChangeListener(preferencesListener)
    }

    private val preferencesListener = SharedPreferences.OnSharedPreferenceChangeListener {
            preferences: SharedPreferences, key: String? ->
        when (key) {
            getString(R.string.preference_key_volume) -> {
                preferences.getString(key, "")?.toDoubleOrNull().let {
                    septik?.volume = it ?: Double.NaN
                }
            }
            getString(R.string.preference_key_empting_price) -> {
                preferences.getString(key, "")?.toDoubleOrNull().let {
                    septik?.emptingPrice = it ?: Double.NaN
                }
            }
            getString(R.string.preference_key_water_price) -> {
                preferences.getString(key, "")?.toDoubleOrNull().let {
                    septik?.waterPrice = it ?: Double.NaN
                }
            }
            else -> {}
        }
    }

    val septik get() = (application as? SeptikApplication)?.septik

    override fun onResume() {
        super.onResume()
        preferences.registerOnSharedPreferenceChangeListener(preferencesListener)
    }

    override fun onPause() {
        super.onPause()
        preferences.registerOnSharedPreferenceChangeListener(preferencesListener)
    }
}