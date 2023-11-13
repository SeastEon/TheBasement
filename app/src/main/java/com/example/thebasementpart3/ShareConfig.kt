package com.example.thebasementpart3

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import java.io.IOException
import java.io.OutputStreamWriter


class ShareConfig(var dataBase: DataBase) {
    fun CreateShareDialog(){
        val alertBuilder = AlertDialog.Builder(dataBase.BMObj.mainActivity)
        val dialogView: View = LayoutInflater.from(dataBase.BMObj.mainActivity).inflate(R.layout.dialog_share_menu, null)
        alertBuilder.setView(dialogView)
        val dialogAlert = alertBuilder.create()

        val shareAlertBuilder = AlertDialog.Builder(dataBase.BMObj.mainActivity)
        val shareDialogView: View = LayoutInflater.from(dataBase.BMObj.mainActivity).inflate(R.layout.dialog_share_text, null)
        shareAlertBuilder.setView(shareDialogView)
        val shareDialogAlert = shareAlertBuilder.create()

        val shareCodeTxt = shareDialogView.findViewById<TextView>(R.id.ShareTxt)
        val shareBtn = shareDialogView.findViewById<Button>(R.id.ShareTextBtn)
        val ShareCodeprompt = shareDialogView.findViewById<TextView>(R.id.ShareCodeText)

        val loadBasementIdBtn = dialogView.findViewById<Button>(R.id.LoadShareCode)
        loadBasementIdBtn.setOnClickListener {
            shareCodeTxt.isEnabled = false
            shareCodeTxt.text = dataBase.CreateAndSetShareCode()
            shareCodeTxt.textSize = 12f
            shareBtn.text = "Reset Code"
            buttonCaller("Load",null,shareBtn, shareDialogAlert)
            Toast.makeText(dataBase.BMObj.mainActivity, "Share code enabled", Toast.LENGTH_SHORT).show()
            shareDialogAlert.show()
        }

        val createShareCode =  dialogView.findViewById<Button>(R.id.CreateShareCode)
        createShareCode.setOnClickListener{
            shareCodeTxt.isEnabled = true //makes it so the user cannot edit the text view
            shareBtn.text = "Basement ID set"
            shareCodeTxt.text = dataBase.basementId
            ShareCodeprompt.text = "Edit your BasementID. \n This ID is the main identifier for your basement.\n No Basement Id already in use is available."
            buttonCaller("Set", shareCodeTxt ,shareBtn, shareDialogAlert)
            shareDialogAlert.show()
        }

        val importFromDataBase = dialogView.findViewById<Button>(R.id.ImportFromDatabase)
        importFromDataBase.setOnClickListener {
            dataBase.getInformationFromDatabase()
            Toast.makeText(dataBase.BMObj.mainActivity, "Basement received", Toast.LENGTH_SHORT).show()
        }
        val exportFromDataBase= dialogView.findViewById<Button>(R.id.ExportToDataBaseBtn)
        exportFromDataBase.setOnClickListener {
            dataBase.addBasementToDatabase()
            Toast.makeText(dataBase.BMObj.mainActivity, "Basement exported", Toast.LENGTH_SHORT).show()
        }

        val exportToFile = dialogView.findViewById<Button>(R.id.ExportToFile)
        exportToFile.setOnClickListener {
            writeToFile(dataBase.BMObj.BasementString , dataBase.BMObj.mainActivity ,dataBase.basementId)
        }
        dialogAlert.show()
    }

    fun buttonCaller(buttonThatCalled:String, shareCodeTxt:TextView?, shareBtn:Button, shareDialogAlert:AlertDialog ){
        shareBtn.setOnClickListener {
            if (buttonThatCalled == "Load") { //loading and switching the share code
                dataBase.DisableShareCode()
                Toast.makeText(dataBase.BMObj.mainActivity, "Share code disabled", Toast.LENGTH_SHORT).show()
                shareDialogAlert.dismiss()
            } else if (buttonThatCalled == "Set") { //setting the basement
                dataBase.setDocumentRef(shareCodeTxt?.text.toString())
                dataBase.getInformationFromDatabase()
                Toast.makeText(dataBase.BMObj.mainActivity, "Basement ID loaded", Toast.LENGTH_SHORT).show()
                shareDialogAlert.dismiss()
            }
        }
    }

    private fun writeToFile(data: String, context: Context, basementId:String) {
        try {
            val outputStreamWriter =
                OutputStreamWriter(context.openFileOutput("$basementId.base", Context.MODE_PRIVATE))
            outputStreamWriter.write(data)
            var BasementFile = dataBase.BMObj.mainActivity.getFileStreamPath("$basementId.base")
            outputStreamWriter.close()

            Toast.makeText(dataBase.BMObj.mainActivity, "Basement Successfully written to file $BasementFile", Toast.LENGTH_SHORT).show()
        } catch (e: IOException) {
            Log.e("Exception", "File write failed: $e")
        }
    }
}