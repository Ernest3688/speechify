package com.example.speechify.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.speechify.R
import com.example.speechify.SavedNote
import com.example.speechify.SavedNoteAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import java.text.SimpleDateFormat
import java.util.*


class SecondFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var noNotesTV: TextView
    private lateinit var adapter: SavedNoteAdapter
    private lateinit var itemList: MutableList<SavedNote>
    private lateinit var firestore: FirebaseFirestore
    private lateinit var listenerRegistration: ListenerRegistration


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val auth = FirebaseAuth.getInstance();

        val view = inflater.inflate(R.layout.saved_note_fragment, container, false)

        recyclerView = view.findViewById(R.id.savedNoteRecyclerView)
        noNotesTV = view.findViewById(R.id.noNotesTV)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        requireActivity().title = "Saved Notes"
        itemList = mutableListOf()



        firestore = FirebaseFirestore.getInstance()
        val collectionRef = firestore.collection("users").document(auth.uid!!).collection("notes")
        listenerRegistration = collectionRef.addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                return@addSnapshotListener
            }

            itemList.clear()
            snapshot?.forEach { documentSnapshot ->
                val date = documentSnapshot.getDate("timeStamp")
                val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                val formattedDate: String = simpleDateFormat.format(date!!)


                val savedNote = SavedNote(
                    id = documentSnapshot.id,
                    content = documentSnapshot.get("content").toString(),
                    date = formattedDate
                );

                itemList.add(savedNote)

                if (itemList.isNotEmpty()) {
                    noNotesTV.visibility = View.GONE
                } else {
                    Toast.makeText(requireActivity(), "'dfksifjisfj", Toast.LENGTH_SHORT).show()
                    noNotesTV.visibility = View.VISIBLE

                }
            }

            if (isAdded) {
                adapter =
                    SavedNoteAdapter(requireActivity().applicationContext, itemList.reversed())
                recyclerView.adapter = adapter
            }

        }

        return view;
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SecondFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}