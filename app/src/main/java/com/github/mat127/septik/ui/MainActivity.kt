package com.github.mat127.septik.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.github.mat127.septik.R
import com.github.mat127.septik.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_addEmptyDate -> {
            addEmptyTimestamp()
            true
        }
        R.id.action_addState -> {
            addState()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    private fun addState() {
        val dialog = AddStateDialog()
        dialog.show(supportFragmentManager, "add-state")
    }

    private fun addEmptyTimestamp() {
        val dialog = AddEmptyDialog()
        dialog.show(supportFragmentManager, "add-empty")
    }
}