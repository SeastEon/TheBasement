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
        val alertBuilder = AlertDialog.Builder(dataBase.NavHeader.BMObj.mainActivity)
        val dialogView: View = LayoutInflater.from(dataBase.NavHeader.BMObj.mainActivity).inflate(R.layout.dialog_share_menu, null)
        alertBuilder.setView(dialogView)
        val dialogAlert = alertBuilder.create()

        val shareAlertBuilder = AlertDialog.Builder(dataBase.NavHeader.BMObj.mainActivity)
        val shareDialogView: View = LayoutInflater.from(dataBase.NavHeader.BMObj.mainActivity).inflate(R.layout.dialog_share_text, null)
        shareAlertBuilder.setView(shareDialogView)
        val shareDialogAlert = shareAlertBuilder.create()

        val shareCodeTxt = shareDialogView.findViewById<TextView>(R.id.ShareTxt)
        val shareBtn = shareDialogView.findViewById<Button>(R.id.ShareTextBtn)

        val loadBasementIdBtn = dialogView.findViewById<Button>(R.id.LoadShareCode)
        loadBasementIdBtn.setOnClickListener {
            shareCodeTxt.isEnabled = false
            shareCodeTxt.text = dataBase.CreateAndSetShareCode()
            shareCodeTxt.textSize = 12f
            shareBtn.text = "Reset Code"
            buttonCaller("Load",null,shareBtn, shareDialogAlert)
            Toast.makeText(dataBase.NavHeader.BMObj.mainActivity, "Share code Successfully enabled", Toast.LENGTH_SHORT).show()
            shareDialogAlert.show()
        }

        val createShareCode =  dialogView.findViewById<Button>(R.id.CreateShareCode)
        createShareCode.setOnClickListener{
            shareCodeTxt.isEnabled = true //makes it so the user cannot edit the text view
            shareBtn.text = "Set Basement"
            shareCodeTxt.text = ""
            buttonCaller("Set", shareCodeTxt ,shareBtn, shareDialogAlert)
            shareDialogAlert.show()
        }

        val importFromDataBase = dialogView.findViewById<Button>(R.id.ImportFromDatabase)
        importFromDataBase.setOnClickListener {
            dataBase.NavHeader.BMObj.mainActivity.getInformationFromDatabase(dataBase)
        }
        val exportFromDataBase= dialogView.findViewById<Button>(R.id.ExportToDataBaseBtn)
        exportFromDataBase.setOnClickListener {
            dataBase.addBasementToDatabase()
        }

        val exportToFile = dialogView.findViewById<Button>(R.id.ExportToFile)
        exportToFile.setOnClickListener {
            writeToFile(dataBase.NavHeader.BMObj.BasementString , dataBase.NavHeader.BMObj.mainActivity ,dataBase.basementId)
        }
        dialogAlert.show()
    }

    fun buttonCaller(buttonThatCalled:String, shareCodeTxt:TextView?, shareBtn:Button, shareDialogAlert:AlertDialog ){
        shareBtn.setOnClickListener {
            if (buttonThatCalled == "Load") { //loading and switching the share code
                dataBase.DisableShareCode()
                Toast.makeText(dataBase.NavHeader.BMObj.mainActivity, "Share code Successfully disabled", Toast.LENGTH_SHORT).show()
                shareDialogAlert.dismiss()
            } else if (buttonThatCalled == "Set") { //setting the basement
                dataBase.setDocumentRef(shareCodeTxt?.text.toString())
                dataBase.NavHeader.BMObj.mainActivity.getInformationFromDatabase(dataBase)
                Toast.makeText(dataBase.NavHeader.BMObj.mainActivity, "Basement Successfully Set", Toast.LENGTH_SHORT).show()
                shareDialogAlert.dismiss()
            }
        }
    }

    private fun writeToFile(data: String, context: Context, basementId:String) {
        try {
            val outputStreamWriter =
                OutputStreamWriter(context.openFileOutput("$basementId.base", Context.MODE_PRIVATE))
            outputStreamWriter.write(data)
            var BasementFile = dataBase.NavHeader.BMObj.mainActivity.getFileStreamPath("$basementId.base")
            outputStreamWriter.close()

            Toast.makeText(dataBase.NavHeader.BMObj.mainActivity, "Basement Successfully written to file $BasementFile", Toast.LENGTH_SHORT).show()
        } catch (e: IOException) {
            Log.e("Exception", "File write failed: $e")
        }
    }
}