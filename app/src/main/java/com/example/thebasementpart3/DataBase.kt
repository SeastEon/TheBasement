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
import java.util.UUID


class DataBase (private var mainActivity: Activity, private var BMObj:BasementObject){
    var basementId = "TestBasement"
    var ShareCode:String? = null
    private val db = FirebaseFirestore.getInstance()
    private var documentRef = db.collection("Basement").document(basementId)
    var returnedDoc = BasementObject.BasementSection("", "")

    fun setDocumentRef(basementId: String){
        val newDocumentRef =db.collection("Basement").document(basementId)
        documentRef = newDocumentRef
        Toast.makeText(mainActivity, "Basement Successfully Set", Toast.LENGTH_SHORT).show()
    }

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
                    Toast.makeText(mainActivity, "Basement Successfully received", Toast.LENGTH_SHORT).show()
                }
            }
            BMObj.setTextBox(BMObj.setBasementText(returnedDoc))
        }
    }

    fun addBasementToDatabase(BMObj:BasementObject){
        val basementClassObject = BasementClass(basementId, BMObj.mHeaders, BMObj.mText, ShareCode)
        documentRef.set(basementClassObject)
            .addOnSuccessListener { Toast.makeText(mainActivity, "Successful", Toast.LENGTH_SHORT).show() }
            .addOnFailureListener { e -> Toast.makeText(mainActivity, "Failed$e", Toast.LENGTH_SHORT).show() }
    }

    data class BasementClass( //this will need to be expanded to hold the preferences
        var basementId: String? = null,
        var mHeaders: String? = null,
        var mText: String? = null,
        var mShareCode:String? = null
    )

    fun clearBasementDialog(){
        val alertBuilder = AlertDialog.Builder(mainActivity)
        val dialogView: View = LayoutInflater.from(mainActivity).inflate(R.layout.dialog_delete, null)
        alertBuilder.setView(dialogView)
        val dialogAlert = alertBuilder.create()

        val clearBasementBtn = dialogView.findViewById<Button>(R.id.ClearBasement)
        clearBasementBtn.setOnClickListener{
            addBasementToDatabase(BMObj.eraseBasement())
            val layout = mainActivity.findViewById<LinearLayout>(R.id.BasementScrollLinearLayout)
            while(layout.childCount > 1){ layout.removeViewAt(1)}
            dialogAlert.dismiss()
            Toast.makeText(mainActivity, "Basement Successfully erased", Toast.LENGTH_SHORT).show()
        }
        dialogAlert.show()
    }

    fun CreateAndSetShareCode():String{
        ShareCode = UUID.randomUUID().toString()
        addBasementToDatabase(BMObj)
        return ShareCode as String
    }

    fun DisableShareCode(){
        ShareCode = null
        addBasementToDatabase(BMObj)
    }
}



