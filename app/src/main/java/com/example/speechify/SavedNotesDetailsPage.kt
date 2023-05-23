package com.example.speechify

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.speechify.databinding.ActivitySavedNotesDetailsPageBinding
import com.example.speechify.databinding.LoginPageBinding
import com.example.speechify.databinding.SavedNotesComponentBinding

class SavedNotesDetailsPage : AppCompatActivity() {

    private lateinit var binding: ActivitySavedNotesDetailsPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySavedNotesDetailsPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.apply {
            title = "Saved Note details"
            setDisplayHomeAsUpEnabled(true)
        }


        val bundle = intent.extras
        val savedNote = bundle?.getString("note")
        if(savedNote != null){
            binding.notesTV.text = savedNote

        }


    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed() //with this line

        return true
    }
}