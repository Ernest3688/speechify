package com.example.speechify

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.speechify.databinding.LoginPageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class LoginPage : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: LoginPageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        binding = LoginPageBinding.inflate(layoutInflater)
        setContentView(binding.root)


        if(auth.uid != null){
            val homePageIntent = Intent(this, MainActivity::class.java)
            startActivity(homePageIntent)
        }

        supportActionBar?.title = "Login Page"

        binding.signUpTxt.setOnClickListener {
            val registrationPageIntent = Intent(this, RegistrationPage::class.java)
            startActivity(registrationPageIntent);
        }

        binding.loginBtn.setOnClickListener{
           loginUser()
        }

    }

    private fun loginUser(){

        if(binding.etEmail.text.isEmpty()){
            binding.etEmail.error = "Email is required"
            return
        }
        if(binding.etPassword.text.isEmpty()){
            binding.etPassword.error = "Password is required"
            return
        }


        binding.loginBtn.visibility = View.GONE
        binding.loadingBar.visibility = View.VISIBLE

        auth.signInWithEmailAndPassword(binding.etEmail.text.toString(), binding.etPassword.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {

                    binding.loginBtn.visibility = View.VISIBLE
                    binding.loadingBar.visibility = View.GONE

                    Log.d(TAG, "signInWithEmail:success")

                        val homePageIntent = Intent(this, MainActivity::class.java)
                        startActivity(homePageIntent)

                } else {

                    Log.w(TAG, "signInWithEmail:failure", task.exception)

                    binding.loginBtn.visibility = View.VISIBLE
                    binding.loadingBar.visibility = View.GONE
                    Toast.makeText(baseContext, task.exception.toString().split(":")[1],
                        Toast.LENGTH_SHORT).show()
                }
            }
    }
}