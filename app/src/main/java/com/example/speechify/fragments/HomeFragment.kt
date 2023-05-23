package com.example.speechify.fragments

import android.content.Intent
import android.opengl.Visibility
import android.os.Bundle
import android.speech.RecognizerIntent
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.speechify.databinding.HomeFragmentBinding
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*


class HomeFragment : Fragment() {


    val db = Firebase.firestore
    val auth = FirebaseAuth.getInstance();
    private val requestCodeSpeechInput = 1
    private lateinit var binding: HomeFragmentBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = HomeFragmentBinding.inflate(inflater, container, false)
        return binding.root;

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().title = "Home"

        binding.startTalkingBtn.setOnClickListener {
            startRecording()
        }

        binding.cancelTV.setOnClickListener{
            binding.cardView.visibility = View.GONE
            binding.startTalkingBtn.visibility = View.VISIBLE

        }

        binding.saveBtn.setOnClickListener {

            val docData = hashMapOf(
                "content" to binding.speechTextId.text,
                "timeStamp" to Timestamp(Date()),
            )


            db.collection("users").document(auth.uid!!)
                .collection("notes").document().set(docData)
                .addOnSuccessListener {

                    binding.cardView.visibility = View.GONE
                    binding.startTalkingBtn.visibility = View.VISIBLE

                    Toast.makeText(requireActivity(), "Note Saved Successfully", Toast.LENGTH_SHORT).show()

                    Log.d("Success", "DocumentSnapshot successfully written!")
                }
                .addOnFailureListener { e ->
                    Toast.makeText(context, "Error saving note, try again", Toast.LENGTH_SHORT).show()
                    Log.w("Failure", "Error writing document", )
                }

        }

    }

    private fun startRecording() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to text")

        try {
            startActivityForResult(intent, requestCodeSpeechInput)
        } catch (e: Exception) {
            Toast.makeText(
                context,
                " " + e.message,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == requestCodeSpeechInput) {
            if (resultCode == AppCompatActivity.RESULT_OK && data != null) {
                val result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)

                binding.startTalkingBtn.visibility = View.GONE

                binding.cardView.visibility = View.VISIBLE
                binding.speechTextId.text = Objects.requireNonNull(result)?.get(0)
            }
        }
    }

    companion object {

        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }


}