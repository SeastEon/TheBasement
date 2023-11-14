package com.example.thebasementpart3

import android.R.attr.label
import android.R.attr.text
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.getSystemService
import java.io.IOException
import java.io.OutputStreamWriter
import java.util.UUID


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
        val RandomiseCodeOrCopyCode = shareDialogView.findViewById<Button>(R.id.ResetCodeBtn)

        val loadBasementIdBtn = dialogView.findViewById<Button>(R.id.LoadShareCode)
        loadBasementIdBtn.setOnClickListener {
            shareCodeTxt.isEnabled = true
            shareCodeTxt.text = dataBase.CreateAndSetShareCode()
            shareCodeTxt.setInputType(InputType.TYPE_NULL);
            shareCodeTxt.textSize = 12f
            shareBtn.text = "Reset Code"
            RandomiseCodeOrCopyCode.text = "Copy Code"
            buttonCaller("Load",null,shareBtn, shareDialogAlert)
            buttonCaller("Copy",shareCodeTxt, RandomiseCodeOrCopyCode, shareDialogAlert)
            Toast.makeText(dataBase.BMObj.mainActivity, "Share code enabled", Toast.LENGTH_SHORT).show()
            shareDialogAlert.show()
        }

        val createShareCode =  dialogView.findViewById<Button>(R.id.CreateShareCode)
        createShareCode.setOnClickListener{
            shareCodeTxt.isEnabled = true //makes it so the user cannot edit the text view
            shareBtn.text = "Basement ID set"
            shareCodeTxt.text = dataBase.basementId
            RandomiseCodeOrCopyCode.text = "Randomise Code"
            ShareCodeprompt.text = "Edit your BasementID. \n This ID is the main identifier for your basement.\n No Basement Id already in use is available."
            ShareCodeprompt.textSize = 12f
            buttonCaller("Set",null,shareBtn, shareDialogAlert)
            buttonCaller("RandomiseCode", shareCodeTxt ,RandomiseCodeOrCopyCode, shareDialogAlert)
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

    fun buttonCaller(buttonThatCalled:String, shareCodeTxt:TextView?, ButtonToChange:Button, shareDialogAlert:AlertDialog ) {
        if (buttonThatCalled == "Load" || buttonThatCalled == "Set") {
            ButtonToChange.setOnClickListener {
                if (buttonThatCalled == "Load") { //loading and switching the share code
                    dataBase.DisableShareCode()
                    Toast.makeText(dataBase.BMObj.mainActivity, "Share code disabled", Toast.LENGTH_SHORT).show()
                    shareDialogAlert.dismiss()
                } else if (buttonThatCalled == "Set") { //setting the basement
                    if (shareCodeTxt != null) {
                        dataBase.basementId = shareCodeTxt.text.toString()
                        dataBase.setDocumentRef(shareCodeTxt.text.toString())
                    }

                    dataBase.getInformationFromDatabase()
                    Toast.makeText(dataBase.BMObj.mainActivity, "Basement ID loaded", Toast.LENGTH_SHORT).show()
                    shareDialogAlert.dismiss()
                }
            }
        } else if (buttonThatCalled == "Copy" || buttonThatCalled == "RandomiseCode") {
            ButtonToChange.setOnClickListener {
                if (buttonThatCalled == "Copy") {
                    if (shareCodeTxt != null) { dataBase.BMObj.mainActivity.copyToClipboard(shareCodeTxt.text) }
                    Toast.makeText(dataBase.BMObj.mainActivity, "Code Copied", Toast.LENGTH_SHORT).show()

                } else if (buttonThatCalled == "RandomiseCode") {
                    if (shareCodeTxt != null) { shareCodeTxt.text = UUID.randomUUID().toString() }
                }
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

    fun Context.copyToClipboard(text: CharSequence){
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("label",text)
        clipboard.setPrimaryClip(clip)
    }
}