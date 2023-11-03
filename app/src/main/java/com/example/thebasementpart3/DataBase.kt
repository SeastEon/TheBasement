package com.example.thebasementpart3

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import java.io.IOException
import java.io.OutputStreamWriter
import java.util.UUID
import java.util.Vector


class DataBase (private var mainActivity: Activity, private var BMObj:BasementObject){
    var basementId = "TestBasement"
    var shareCode:String? = null
    private val db = FirebaseFirestore.getInstance()
    private var documentRef = db.collection("Basement").document(basementId)
    var returnedDoc = BasementObject.BasementSection("", "")
    var headerKeyVector = Vector<Int>()

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
        val basementClassObject = BasementClass(basementId, BMObj.mHeaders, BMObj.mText, shareCode)
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

    fun EncrptData(header: NavigateHeader):Vector<BasementObject.BasementSection> {
        val bmEncryptVal = basementId.toInt() / basementId.length
        var encryptedHeader = ""
        var encryptedText =""
        var headerKey = 0

        var encryptedBasementObject = BasementObject.BasementSection("", "")

        var encryptedBasementObjectVector = Vector<BasementObject.BasementSection>()

        for (Header in header.basementSections) {
            for (letter in Header.BasementHeader) {
                val value = letter + letter.toString().toInt().plus(bmEncryptVal)
                encryptedHeader += value
            }
            for (letter in encryptedHeader){ headerKey += letter.code }
            headerKeyVector.add(headerKey/encryptedHeader.length )

            encryptedBasementObject.BasementHeader = encryptedHeader
            for(Text in header.basementSections) {
                var i = 0
                for (letter in Text.basementText) {
                    val value = letter.toString().toInt().plus(headerKeyVector[i])
                    encryptedText += value.toChar()
                    i += 1
                }
                encryptedBasementObject.basementText = encryptedText
            }
            encryptedBasementObjectVector.add(encryptedBasementObject)
        }
        return encryptedBasementObjectVector
    }

    fun DecryptData(BmVector:Vector<BasementObject.BasementSection>):Vector<BasementObject.BasementSection>{
        var basementIDCode = 0

        var decryptedHeader = ""
        var decryptedText =""
        var decryptedBasementObject = BasementObject.BasementSection("", "")
        var decryptedBasementObjectVector = Vector<BasementObject.BasementSection>()

        for (char in basementId ){basementIDCode += char.code}
        val bmEncryptVal = basementIDCode / basementId.length

        var i = 0
        for (Header in BmVector) {
            for (letter in Header.BasementHeader) {
                val value = letter.toString().toInt() - bmEncryptVal
                decryptedHeader += value
            }
            decryptedBasementObject.BasementHeader = decryptedHeader


            for(Text in BmVector) {
                for (letter in Text.basementText) {
                    val value = letter.toString().toInt() - headerKeyVector[i]
                    decryptedText += value.toChar()
                }
                i += 1
                decryptedBasementObject.basementText = decryptedText
            }
            decryptedBasementObjectVector.add(decryptedBasementObject)
        }
        return decryptedBasementObjectVector
    }

    fun CreateAndSetShareCode():String{
        shareCode = UUID.randomUUID().toString()
        addBasementToDatabase(BMObj)
        return shareCode as String
    }

    fun DisableShareCode(){
        shareCode = null
        addBasementToDatabase(BMObj)
    }


}



