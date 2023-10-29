package com.example.thebasementpart3

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.children
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject


class DataBase (private var mainActivity: Activity, private var BMObj:BasementObject){
    private val db = FirebaseFirestore.getInstance()
    private var basementId = "TestBasement"
    private var documentRef = db.collection("Basement").document(basementId)
    var returnedDoc = BasementObject.BasementSection("", "")

    val getBasementFromDatabase: () -> BasementObject.BasementSection = {
        receiveData()
         returnedDoc
    }

    private fun receiveData(){
        documentRef.get().addOnSuccessListener { documentSnapshot ->
            val basement = documentSnapshot.toObject <BasementClass>()
            if (basement != null) {
                if(basement.mHeaders != null &&  basement.mText != null) {
                    returnedDoc = BasementObject.BasementSection(basement.mHeaders.toString(), basement.mText.toString())
                }
            }
            BMObj.setTextBox(BMObj.setBasementText(returnedDoc))
        }
    }

    fun addBasementToDatabase(BMObj:BasementObject){
        val basementClassObject = BasementClass(basementId, BMObj.mHeaders, BMObj.mText)

        documentRef.set(basementClassObject)
            .addOnSuccessListener { Toast.makeText(mainActivity, "Successful", Toast.LENGTH_SHORT).show() }
            .addOnFailureListener { e -> Toast.makeText(mainActivity, "Failed$e", Toast.LENGTH_SHORT).show() }
    }

    data class BasementClass( //this will need to be expanded to hold the preferences
        var basementId: String? = null,
        var mHeaders: String? = null,
        var mText: String? = null
    )

    fun clearBasementDialog(MainText:View){
        val alertBuilder = AlertDialog.Builder(mainActivity)
        val dialogView: View = LayoutInflater.from(mainActivity).inflate(R.layout.dialog_delete, null)

        val clearBasementBtn = dialogView.findViewById<Button>(R.id.ClearBasement)
        clearBasementBtn.setOnClickListener{
            addBasementToDatabase(BMObj.eraseBasement())
            val layout = mainActivity.findViewById<LinearLayout>(R.id.BasementScrollLinearLayout)
            val count = layout.children
            for (a in count) { if (a != MainText) { layout.removeView(a) } }
        }

        alertBuilder.setView(dialogView)
        val dialogAlert = alertBuilder.create()
        dialogAlert.show()
    }

}



