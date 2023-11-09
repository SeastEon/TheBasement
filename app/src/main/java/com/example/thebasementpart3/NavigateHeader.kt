package com.example.thebasementpart3

import android.icu.lang.UCharacter.IndicPositionalCategory.RIGHT
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import java.util.Vector


class NavigateHeader (var BMObj: BasementObject) {
    var basementSections = Vector<BasementObject.BasementSection>()
    private fun getHeader() {  //separates the headers from the text and stores them inside a string vector
        basementSections = BMObj.vectorBasementObject
        if(basementSections[0].BasementHeader == ""){BMObj.createBasementSections(BMObj.mainActivity.findViewById<TextView>(R.id.addTextTxtView).text.toString())}
    }

    fun startUpHeader() {
        getHeader()
        val alertBuilder = AlertDialog.Builder(BMObj.mainActivity)
        val dialogView: View = LayoutInflater.from(BMObj.mainActivity).inflate(R.layout.dialog_header_navigation, null)
        alertBuilder.setView(dialogView)
        val dialogAlert = alertBuilder.create()
        val linearScrollView = dialogView.findViewById<LinearLayout>(R.id.HeaderNavigationScrollLayouut)

        val exitButton =  dialogView.findViewById<Button>(R.id.ExitHeaderNavigation)
        exitButton.setOnClickListener{
            linearScrollView.removeAllViews()
            dialogAlert.dismiss()
        }

        for (h in basementSections){
            //this is where we create the text views
            val newTextView= TextView(BMObj.mainActivity)
            newTextView.text = h.BasementHeader
            newTextView.setTextColor(BMObj.mainActivity.getColor(R.color.white))
            newTextView.textSize = 20f
            linearScrollView.addView(newTextView)
        }
        dialogAlert.show()
        val dialogWindow = dialogAlert.window
        dialogWindow?.setLayout(580, MATCH_PARENT)
        dialogWindow?.setGravity(RIGHT)
    }
}