package com.example.speechify

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.speechify.databinding.ActivityMainBinding
import com.example.speechify.fragments.HomeFragment
import com.example.speechify.fragments.SecondFragment
import com.example.speechify.fragments.SettingsFragment
import com.example.speechify.fragments.WeatherFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder

import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth


class MainActivity : AppCompatActivity() {
    private var mDrawer: DrawerLayout? = null
    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupDrawerContent(binding.nvView)


        supportActionBar?.apply {
            setHomeAsUpIndicator(R.drawable.ic_hamburger)
            setDisplayHomeAsUpEnabled(true)

        }

        val sharedPreferences = getPreferences(Context.MODE_PRIVATE)
        val isDarkTheme = sharedPreferences?.getBoolean("IS_DARK_THEME", false)
        if (isDarkTheme == true) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)


        } else {

            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
        mDrawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout

        val fragmentManager = supportFragmentManager
        fragmentManager.beginTransaction()
            .replace(R.id.flContent, HomeFragment::class.java.newInstance()).commit()

    }

    private fun setupDrawerContent(navigationView: NavigationView) {
        findViewById<NavigationView>(R.id.nvView).setNavigationItemSelectedListener { menuItem ->
            selectDrawerItem(menuItem)
            true
        }
    }

    private fun selectDrawerItem(menuItem: MenuItem) {

        if (menuItem.itemId == R.id.nav_logout) {
            MaterialAlertDialogBuilder(this)
                .setTitle("Are you sure you want to log out?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("Ok") { _, _ ->
                    FirebaseAuth.getInstance().signOut();
                    startActivity(
                        Intent(applicationContext, LoginPage::class.java)
                    );
                    finish();
                }
                .setNegativeButton("Cancel") { _, _ ->
                }
                .show()
            return
        }
        var fragment: Fragment? = null
        var fragmentClass: Class<out Fragment>? = null
        fragmentClass = when (menuItem.itemId) {
            R.id.nav_home_fragment -> HomeFragment::class.java
            R.id.nav_saved_note_fragment -> SecondFragment::class.java
            R.id.nav_settings_fragment -> SettingsFragment::class.java
            R.id.nav_weather_fragment -> WeatherFragment::class.java
            else -> HomeFragment::class.java
        }

        try {
            fragment = fragmentClass.newInstance()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val fragmentManager = supportFragmentManager
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment!!).commit()

        menuItem.isChecked = true

        mDrawer?.closeDrawers()

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                mDrawer!!.openDrawer(GravityCompat.START)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}