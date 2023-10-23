package com.example.thebasementpart3

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.children
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject


class DataBase (private var mainActivity: Activity, var BMObj:BasementObject){
    private val db = FirebaseFirestore.getInstance()
    var BasementId = "Testbasement"
    var documentRef = db.collection("Basement").document(BasementId)
    var ReturnedDoc = BasementObject.BasementSection("", "")

    val GetBasementFromDatabase: () -> BasementObject.BasementSection = {
        RecieveData()
         ReturnedDoc
    }

    fun RecieveData(){
        documentRef.get().addOnSuccessListener { documentSnapshot ->
            val basement = documentSnapshot.toObject <basementclass>()
            if (basement != null) {
                if(basement.mHeaders != null &&  basement.mText != null) {
                    ReturnedDoc = BasementObject.BasementSection(basement.mHeaders.toString(), basement.mText.toString())
                }
            }
            BMObj.SetTextBox(BMObj.SetBasementText(ReturnedDoc))
        }
    }

    fun addBasementToDatabase(BMObj:BasementObject){
      //val Basement = hashMapOf("ID" to BMOBj.mBasementId, "Headers" to BMOBj.mHeaders, "Text" to BMOBj.mText)
        val BasementcalssObject = basementclass(BasementId, BMObj.mHeaders, BMObj.mText)

        documentRef.set(BasementcalssObject)
            .addOnSuccessListener { mBasementId  -> Toast.makeText(mainActivity, "Successful", Toast.LENGTH_SHORT).show() }
            .addOnFailureListener { e -> Toast.makeText(mainActivity, "Failed" + e.toString(), Toast.LENGTH_SHORT).show() }
    }

    data class basementclass(
        var basementId: String? = null,
        var mHeaders: String? = null,
        var mText: String? = null
    )

    fun ClearBasementDialog(MainText:View){
        var alertBuilder = AlertDialog.Builder(mainActivity)
        var dialogView: View = LayoutInflater.from(mainActivity).inflate(R.layout.dialog_delete, null)

        val Clearbasement = dialogView.findViewById<Button>(R.id.ClearBasement)
        Clearbasement.setOnClickListener{
            addBasementToDatabase(BMObj.EraseBasement())
            val Layout = mainActivity.findViewById<LinearLayout>(R.id.BasementScrollLinearLayout)
            val count = Layout.children
            for (a in count) {
                if (a == MainText) { }
                else { Layout.removeView(a) }
            }
        }
        alertBuilder.setView(dialogView)

        var dialogAlert = alertBuilder.create()
        dialogAlert.show()
    }

}



