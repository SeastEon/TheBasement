package com.example.thebasementpart3

import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import org.w3c.dom.Text
import java.util.UUID


class ShareConfig(var mainActivity: MainActivity, var dataBase: DataBase, var baseMTObj:BasementObject) {

    fun CreateShareDialog(){
        val alertBuilder = AlertDialog.Builder(mainActivity)
        val dialogView: View = LayoutInflater.from(mainActivity).inflate(R.layout.dialog_share_menu, null)
        alertBuilder.setView(dialogView)
        val dialogAlert = alertBuilder.create()



        val shareAlertBuilder = AlertDialog.Builder(mainActivity)
        val shareDialogView: View = LayoutInflater.from(mainActivity).inflate(R.layout.dialog_share_text, null)
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
            Toast.makeText(mainActivity, "Share code Successfully enabled", Toast.LENGTH_SHORT).show()
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
            mainActivity.getInformationFromDatabase(dataBase, baseMTObj)
        }
        val exportFromDataBase= dialogView.findViewById<Button>(R.id.ExportToDataBaseBtn)
        exportFromDataBase.setOnClickListener {
            dataBase.addBasementToDatabase(baseMTObj)
        }
        dialogAlert.show()
    }

    fun buttonCaller(buttonThatCalled:String, shareCodeTxt:TextView?, shareBtn:Button, shareDialogAlert:AlertDialog ){
        shareBtn.setOnClickListener {
            if (buttonThatCalled == "Load") { //loading and switching the share code
                dataBase.DisableShareCode()
                Toast.makeText(mainActivity, "Share code Successfully disabled", Toast.LENGTH_SHORT).show()
                shareDialogAlert.dismiss()
            } else if (buttonThatCalled == "Set") { //setting the basement
                dataBase.setDocumentRef(shareCodeTxt?.text.toString())
                mainActivity.getInformationFromDatabase(dataBase, baseMTObj)
                Toast.makeText(mainActivity, "Basement Successfully Set", Toast.LENGTH_SHORT).show()
                shareDialogAlert.dismiss()
            }
        }
    }
}