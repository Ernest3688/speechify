package com.example.speechify.fragments

import android.content.Context
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import androidx.appcompat.app.AppCompatDelegate
import com.example.speechify.R
import com.google.android.material.switchmaterial.SwitchMaterial


class SettingsFragment : Fragment() {

    private lateinit var switchBtn: SwitchMaterial

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var view = inflater.inflate(R.layout.settings_fragment, container, false)

        val sharedPreferences = activity?.getPreferences(Context.MODE_PRIVATE)
        var isDarkTheme = sharedPreferences?.getBoolean("IS_DARK_THEME", false)
        if (isDarkTheme == null) {
            isDarkTheme = false
        }

        switchBtn = view.findViewById(R.id.switchBtn)
        switchBtn.isChecked = isDarkTheme

        requireActivity().title = "Settings"

        switchBtn.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {

                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                saveThemePreference(true)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                saveThemePreference(false)
            }

        }
        return view
    }

    private fun saveThemePreference(isDarkTheme: Boolean) {
        val sharedPreferences = activity?.getPreferences(Context.MODE_PRIVATE)
        val editor = sharedPreferences?.edit()
        editor?.putBoolean("IS_DARK_THEME", isDarkTheme)
        editor?.apply()
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SettingsFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}