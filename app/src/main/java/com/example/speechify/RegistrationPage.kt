package com.example.speechify

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.speechify.data_classes.RegistrationModel
import com.example.speechify.databinding.LoginPageBinding
import com.example.speechify.databinding.RegistrationPageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RegistrationPage : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    val db = Firebase.firestore
    private lateinit var binding: RegistrationPageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        binding = RegistrationPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Registration Page"


        binding.loginTxt.setOnClickListener {
            val registrationPageIntent = Intent(this, LoginPage::class.java)
            startActivity(registrationPageIntent);
        }

        binding.registrationBtn.setOnClickListener {
            val model = RegistrationModel(
                fName = binding.etFname.text.toString(),
                lName = binding.etLname.text.toString(),
                emailAddress = binding.etEmail.text.toString(),
                phoneNum = binding.etPhoneNum.text.toString(),
                password = binding.etPassword.text.toString()
            )
            registerUser(model)
        }
    }

    private fun registerUser(model: RegistrationModel) {


        if (binding.etFname.text.trim().isEmpty()) {
            binding.etFname.error = "First name is required"
            return
        }

        if (binding.etLname.text.trim().isEmpty()) {
            binding.etLname.error = "Last name is required"
            return
        }

        if (binding.etEmail.text.trim().isEmpty()) {
            binding.etEmail.error = "Email is required"
            return
        }
        if (binding.etPassword.text.trim().length != 6) {
            binding.etPassword.error = "Password should be at least 6 characters"
            return
        }

        if (binding.etPassword.text.trim() != binding.etCnfmPassword.text.trim()) {
            binding.etCnfmPassword.error = "Passwords do not match"
            return
        }

        binding.registrationBtn.visibility = View.GONE
        binding.loadingBar.visibility = View.VISIBLE



        auth.createUserWithEmailAndPassword(model.emailAddress, model.password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "createUserWithEmail:success")
                    val user = auth.currentUser

                    val userData = hashMapOf(
                        "fName" to binding.etFname.text.toString(),
                        "lName" to binding.etLname.text.toString(),
                        "emailAddress" to binding.etEmail.text.toString(),
                        "phoneNum" to binding.etPhoneNum.text.toString(),
                    )

                    if (user != null) {
                        db.collection("users").document(user.uid)
                            .set(userData)
                            .addOnSuccessListener {

                                val homePageIntent = Intent(this, MainActivity::class.java)
                                startActivity(homePageIntent);
                            }
                            .addOnFailureListener {
                                Toast.makeText(
                                baseContext, "Something went wrong, please Go to Login",
                                Toast.LENGTH_SHORT
                            ).show() }
                    }

                    binding.registrationBtn.visibility = View.VISIBLE
                    binding.loadingBar.visibility = View.GONE



                } else {
                    binding.registrationBtn.visibility = View.VISIBLE
                    binding.loadingBar.visibility = View.GONE
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext, task.exception.toString().split(":")[1],
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

    }

}