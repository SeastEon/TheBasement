package com.example.thebasementpart3

import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import java.util.UUID
import java.util.Vector


class DataBase (var NavHeader:NavigateHeader){
    var basementId = "TestBasement"
    var shareCode:String? = null
    private val db = FirebaseFirestore.getInstance()
    private var documentRef = db.collection("Basement").document(basementId)
    var returnedDoc = BasementObject.BasementSection("", "")
    var headerKeyVector = Vector<Int>()

    fun setDocumentRef(basementId: String){
        val newDocumentRef =db.collection("Basement").document(basementId)
        documentRef = newDocumentRef
        Toast.makeText(NavHeader.BMObj.mainActivity, "Basement Successfully Set", Toast.LENGTH_SHORT).show()
    }

    val getBasementFromDatabase: () -> BasementObject.BasementSection = {
         receiveData()
         returnedDoc
    }

    fun UpdateBasementObject(BMObj:BasementObject){ this.NavHeader.BMObj = BMObj }

    private fun receiveData(){
        documentRef.get().addOnSuccessListener { documentSnapshot ->
            val basement = documentSnapshot.toObject <BasementClass>()
            if (basement != null) {
                if(basement.mHeaders != null &&  basement.mText != null) {
                    returnedDoc = BasementObject.BasementSection(basement.mHeaders.toString(), basement.mText.toString())
                    Toast.makeText(NavHeader.BMObj.mainActivity, "Basement Successfully received", Toast.LENGTH_SHORT).show()
                }
            }
            NavHeader.BMObj.setTextBox(DecryptData(NavHeader.BMObj.setBasementText(returnedDoc)))
        }
    }

    fun addBasementToDatabase(){
        val basementClassObject = BasementClass(basementId, NavHeader.BMObj.ThisBasementCompiled.BasementHeader, NavHeader.BMObj.ThisBasementCompiled.basementText, shareCode)
        documentRef.set(basementClassObject)
            .addOnSuccessListener { Toast.makeText(NavHeader.BMObj.mainActivity, "Successful", Toast.LENGTH_SHORT).show() }
            .addOnFailureListener { e -> Toast.makeText(NavHeader.BMObj.mainActivity, "Failed$e", Toast.LENGTH_SHORT).show() }
    }

    data class BasementClass( //this will need to be expanded to hold the preferences
        var basementId: String? = null,
        var mHeaders: String? = null,
        var mText: String? = null,
        var mShareCode:String? = null
    )

    fun clearBasementDialog(){
        val alertBuilder = AlertDialog.Builder(NavHeader.BMObj.mainActivity)
        val dialogView: View = LayoutInflater.from(NavHeader.BMObj.mainActivity).inflate(R.layout.dialog_delete, null)
        alertBuilder.setView(dialogView)
        val dialogAlert = alertBuilder.create()
        val clearBasementBtn = dialogView.findViewById<Button>(R.id.ClearBasement)

        clearBasementBtn.setOnClickListener{
            NavHeader.BMObj.eraseBasement()
            addBasementToDatabase()
            val layout = NavHeader.BMObj.mainActivity.findViewById<LinearLayout>(R.id.BasementScrollLinearLayout)
            while(layout.childCount > 1){ layout.removeViewAt(1)}
            dialogAlert.dismiss()
            Toast.makeText(NavHeader.BMObj.mainActivity, "Basement Successfully erased", Toast.LENGTH_SHORT).show()
        }
        dialogAlert.show()
    }

    fun GetEncyptedBasementId():Int{
        var basementIDCode = 0
        for (char in basementId ){basementIDCode += char.code}
        val bmEncryptVal = basementIDCode / basementId.length / 'a'.code
        return bmEncryptVal
    }

    fun EncrptData(header: NavigateHeader):Vector<BasementObject.BasementSection> {
        var encryptedHeader = ""
        var encryptedText =""
        var headerKey = 0
        val bmEncryptVal = GetEncyptedBasementId()
        var encryptedBasementObject = BasementObject.BasementSection("", "")
        var encryptedBasementObjectVector = Vector<BasementObject.BasementSection>()

        for (Header in header.basementSections) {
            if (Header.BasementHeader != ""){
                for (letter in Header.BasementHeader) {
                    val value = letter.code + bmEncryptVal
                    encryptedHeader += value.toChar()
                }
                for (letter in encryptedHeader){ headerKey += letter.code }
                headerKeyVector.add(headerKey/encryptedHeader.length)
                encryptedBasementObject.BasementHeader = encryptedHeader

                for(Text in header.basementSections) {
                    var i = 0
                    for (letter in Text.basementText) {
                        val value = letter.code + (headerKeyVector[i])
                        encryptedText += value.toChar()
                    }
                    i += 1
                    encryptedBasementObject.basementText = encryptedText
                }
                encryptedBasementObjectVector.add(encryptedBasementObject)
            }
        }
        return encryptedBasementObjectVector
    }

    fun DecryptData(BmVector:Vector<BasementObject.BasementSection>):Vector<BasementObject.BasementSection>{
        var decryptedHeader = ""
        var decryptedText =""
        var decryptedBasementObject = BasementObject.BasementSection("", "")
        var decryptedBasementObjectVector = Vector<BasementObject.BasementSection>()
        val bmEncryptVal = GetEncyptedBasementId()
        var i = 0

        for (Header in BmVector) {
            for (letter in Header.BasementHeader) {
                val value = letter.code - bmEncryptVal
                decryptedHeader += value.toChar()
            }
            decryptedBasementObject.BasementHeader = decryptedHeader

            for(Text in BmVector) {
                for (letter in Text.basementText) {
                    val value = letter.code - headerKeyVector[i]
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
        addBasementToDatabase()
        return shareCode as String
    }

    fun DisableShareCode(){
        shareCode = null
        addBasementToDatabase()
    }
}



