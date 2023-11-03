package com.example.thebasementpart3

import android.content.Context
import android.icu.lang.UCharacter.IndicPositionalCategory.RIGHT
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import java.util.Vector


class NavigateHeader (private var mainContext: Context) {

    private var headers = Vector<String>() // the headers for our document are stored here
    var basementSections = Vector<BasementObject.BasementSection>()
    private fun getHeader() {  //separates the headers from the text and stores them inside a string vector
        for (h in basementSections){ headers.add(h.BasementHeader)}
    }

    fun startUpHeader() {
        getHeader()
        val alertBuilder = AlertDialog.Builder(mainContext)
        val dialogView: View = LayoutInflater.from(mainContext).inflate(R.layout.dialog_header_navigation, null)
        alertBuilder.setView(dialogView)
        val dialogAlert = alertBuilder.create()

        val exitButton =  dialogView.findViewById<Button>(R.id.ExitHeaderNavigation)
        exitButton.setOnClickListener{
            dialogAlert.dismiss()
        }

        val linearScrollView = dialogView.findViewById<LinearLayout>(R.id.HeaderNavigationScrollLayouut)
        for (h in headers){
            //this is where we create the text views
            val newTextView= TextView(mainContext)
            newTextView.text = h
            newTextView.setTextColor(mainContext.getColor(R.color.white))
            newTextView.textSize = 20f
            linearScrollView.addView(newTextView)
        }
        dialogAlert.show()
        val dialogWindow = dialogAlert.window
        dialogWindow?.setLayout(580, MATCH_PARENT)
        dialogWindow?.setGravity(RIGHT)
    }
}