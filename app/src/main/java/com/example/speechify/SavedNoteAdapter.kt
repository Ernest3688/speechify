package com.example.speechify

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


data class SavedNote(

    val id: String = "",
    val content: String = "",
    val date: String = "",

    )

class SavedNoteAdapter(private val context: Context, private val itemList: List<SavedNote>) :
    RecyclerView.Adapter<SavedNoteAdapter.ViewHolder>() {


    // ViewHolder class
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val firestore = FirebaseFirestore.getInstance()
        val auth = FirebaseAuth.getInstance()
        val notesCollection = firestore.collection("users").document(auth.uid!!).collection("notes")

        private val nameTextView: TextView = itemView.findViewById(R.id.savedNoteTxt)
        private val dateTextView: TextView = itemView.findViewById(R.id.savedNoteDate)
        private val savedNoteDeleteBtn: TextView = itemView.findViewById(R.id.savedNoteDeleteBtn)
        private val savedNotedCardCV: CardView = itemView.findViewById(R.id.savedNotedCardCV)

        fun bind(item: SavedNote) {
            nameTextView.text = item.content
            dateTextView.text = item.date
            savedNotedCardCV.setOnClickListener {
                val intent = Intent(context, SavedNotesDetailsPage::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                intent.putExtra("note", item.content)

                Log.d("NOTES ___", intent.getStringExtra("note")!!)
                context.startActivity(intent)
            }

            savedNoteDeleteBtn.setOnClickListener {

                notesCollection.document(item.id).delete().addOnSuccessListener {
                    Toast.makeText(context, "DELETED", Toast.LENGTH_SHORT).show()

                }.addOnFailureListener {
                    Toast.makeText(context, "NOT DELETED", Toast.LENGTH_SHORT).show()
                }


            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.saved_notes_component, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}