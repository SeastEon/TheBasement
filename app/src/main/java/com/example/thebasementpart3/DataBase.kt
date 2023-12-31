package com.example.thebasementpart3

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import java.io.File
import java.util.UUID
import java.util.Vector


class DataBase (var BMObj:BasementObject, var NavHeader: NavigateHeader){
    var basementId = "TestBasment2"
    var shareCode:String? = null
    var basementchanges = basementChanges()
    private val db = FirebaseFirestore.getInstance()
    private var documentRef = db.collection("Basement").document(basementId)
    var returnedDoc = BasementObject.BasementSection("", "")

    data class BasementClass( //this will need to be expanded to hold the preferences
        var basementId: String? = null,
        var mHeaders: String? = null,
        var mText: String? = null,
        var mShareCode:String? = null,
        var basementAugmentations: basementChanges? = null
    )
    data class basementChanges(
        var GridContents: Vector<CreateCell.GridCapture>? = null,
        var AudioFileLocations:Vector<String>? = null,
        var PictureLocations:Vector<String>? = null,
        var VideoLocations:Vector<String>? = null,
        var textChanges:Vector<TextFormatConfig.StoreTextChanges>? = null
    )

    fun setDocumentRef(basementId: String){
        val newDocumentRef =db.collection("Basement").document(basementId)
        documentRef = newDocumentRef
        Toast.makeText(BMObj.mainActivity, "Basement Set", Toast.LENGTH_SHORT).show()
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
                    basementchanges = basement.basementAugmentations!!
                    DataReload(basementchanges)
                }
            }
            BMObj.vectorBasementObject = DecryptData(BMObj.GetBasementVectorsWithoutDelimiter(returnedDoc))
            BMObj.setTextBox()
        }
    }

    fun addBasementToDatabase(){
        documentRef.set( BasementClass(basementId, BMObj.ThisBasementCompiled.BasementHeader, BMObj.ThisBasementCompiled.basementText, shareCode, basementchanges))
            .addOnFailureListener { e -> Toast.makeText(BMObj.mainActivity, "Basement export Failed$e", Toast.LENGTH_SHORT).show() }
    }

    fun clearBasementDialog(){
        val alertBuilder = AlertDialog.Builder(BMObj.mainActivity)
        val dialogView: View = LayoutInflater.from(BMObj.mainActivity).inflate(R.layout.dialog_delete, null)
        alertBuilder.setView(dialogView)
        val dialogAlert = alertBuilder.create()
        val clearBasementBtn = dialogView.findViewById<Button>(R.id.ClearBasement)

        clearBasementBtn.setOnClickListener{
            BMObj.eraseBasement()
            addBasementToDatabase()
            val layout = BMObj.mainActivity.findViewById<LinearLayout>(R.id.BasementScrollLinearLayout)
            while(layout.childCount > 1){ layout.removeViewAt(1)}
            dialogAlert.dismiss()
            Toast.makeText(BMObj.mainActivity, "Basement erased", Toast.LENGTH_SHORT).show()
        }
        dialogAlert.show()
    }

    fun GetEncyptedBasementId(): Int {
        var basementIDCode = 0
        for (char in basementId) { basementIDCode += char.code }
        return basementIDCode / basementId.length / 'a'.code
    }

    fun EncrptData(BmVector:Vector<BasementObject.BasementSection>):Vector<BasementObject.BasementSection> {
        val encryptedBasementObjectVector = Vector<BasementObject.BasementSection>()
        val bmEncryptVal = GetEncyptedBasementId()
        var key = 0
        NavHeader.SetBasementHeaders(BmVector) //gives the decrypted data to the header
        for (basement in BmVector) {
            var encryptedHeader = ""; var encryptedText = ""
            if (basement.BasementHeader != "") {
                for (letter in basement.BasementHeader) {
                    val value = letter.code + bmEncryptVal
                    key += value
                    encryptedHeader += value.toChar()
                }
                key /= encryptedHeader.length
                for (letter in basement.basementText) {
                    val value = letter.code + key
                    encryptedText += value.toChar()
                }
                encryptedBasementObjectVector.add(BasementObject.BasementSection(encryptedHeader, encryptedText))
            }
        }
        return encryptedBasementObjectVector
    }

    fun DecryptData(BmVector:Vector<BasementObject.BasementSection>):Vector<BasementObject.BasementSection>{
        val decryptedBasementObjectVector = Vector<BasementObject.BasementSection>()
        val bmEncryptVal = GetEncyptedBasementId()
        var key = 0
        for (basement in BmVector) {
            var decryptedHeader = "";  var decryptedText = ""
            for (letter  in basement.BasementHeader) {
                key += letter.code
                val value = letter.code - bmEncryptVal
                decryptedHeader += value.toChar()
            }
            key /= decryptedHeader.length
            for (letter in basement.basementText) {
                val value = letter.code - key
                decryptedText += value.toChar()
            }
            decryptedBasementObjectVector.add(BasementObject.BasementSection(decryptedHeader,decryptedText))
        }
        NavHeader.SetBasementHeaders(decryptedBasementObjectVector) //gives the decrypted data to the header
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
    fun Configuredatabase(mainTextView:TextView){
        BMObj.createBasementSections(mainTextView.text.toString())
        BMObj.vectorBasementObject = EncrptData(BMObj.vectorBasementObject)
        BMObj.separateBasementHeaders()
        addBasementToDatabase()
    }

    fun DataReload(basementchanges:basementChanges):Basementpart{
        var basementparts = Basementpart()
        if(basementchanges.textChanges != null){
            var textFormatter = TextFormatConfig(this)
            textFormatter.ReloadEditTextSelection(basementchanges.textChanges)
            basementparts.TextF = textFormatter
        }
        else if(basementchanges.GridContents != null){
            var GridCreator = CreateCell(this)
            GridCreator.OnReload(basementchanges.GridContents!!)
            basementparts.GridCreator = GridCreator
        }
        else if(basementchanges.PictureLocations != null || basementchanges.VideoLocations != null){
            var cameraCreator = CameraConfig(this.BMObj.mainActivity)
            cameraCreator.createCameraDialog()
            if(basementchanges.PictureLocations != null){
                for (pictures in basementchanges.PictureLocations!!){
                    var file= File(pictures)
                    cameraCreator.addPicture(file)
                }
            }
            else if(basementchanges.VideoLocations != null){
                for (videos in basementchanges.VideoLocations!!){
                    val videoUri = Uri.fromFile(File(videos))
                    cameraCreator.addVideo(videoUri)
                }
            }
            basementparts.cameraCreator = cameraCreator

        }
        else if(basementchanges.AudioFileLocations != null){
            var AudioCreator = AudioConfig(this)
            for (audios in basementchanges.AudioFileLocations!!){
                AudioCreator.CreatePlayBackAudioBox(audios.toString())
            }
            basementparts.AudioCreator = AudioCreator
        }
        return basementparts
    }

    data class Basementpart(
        var TextF :TextFormatConfig? = null,
        var GridCreator:CreateCell? = null,
        var cameraCreator:CameraConfig? = null,
        var AudioCreator:AudioConfig? = null
    )

    fun getInformationFromDatabase(){
        getBasementFromDatabase()
        if(returnedDoc.BasementHeader != ""){
            DecryptData(BMObj.GetBasementVectorsWithoutDelimiter(returnedDoc))
            BMObj.setTextBox()
        }
    }
}