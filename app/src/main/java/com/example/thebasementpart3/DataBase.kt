package com.example.thebasementpart3

import android.content.Context
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore

class DataBase (private var mainContext: Context){
    private val db = FirebaseFirestore.getInstance()

    fun addBasementToDatabase(BMOBj: BasementObject ){
       val Basement = hashMapOf("ID" to BMOBj.mBasementId, "Headers" to BMOBj.mHeaders, "Text" to BMOBj.mText)
        db.collection("Basement")
            .add(Basement)
            .addOnSuccessListener { documentReference  -> Toast.makeText(mainContext, "Successful", Toast.LENGTH_SHORT).show() }
            .addOnFailureListener { e -> Toast.makeText(mainContext, "Failed" + e.toString(), Toast.LENGTH_SHORT).show()
            }
    }
}



