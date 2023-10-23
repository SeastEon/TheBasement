package com.example.thebasementpart3

import android.content.Context
import android.icu.lang.UCharacter.IndicPositionalCategory.LEFT
import android.icu.lang.UCharacter.IndicPositionalCategory.RIGHT
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.WindowManager
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import java.util.Vector


class NavigateHeader (var maincontext: Context) {

    var BasementSections = Vector<BasementObject.BasementSection>()
    private var Headers = Vector<String>() // the headers for our document are stored here

    fun GetHeader() {
        for (h in BasementSections){ Headers.add(h.BasementHeader)}
    } //separates the headers from the text and stores them inside a string vector

    fun StartUpHeader() {
        GetHeader()
        var alertBuilder = AlertDialog.Builder(maincontext)
        var dialogView: View = LayoutInflater.from(maincontext).inflate(R.layout.dialog_header_navigation, null)
        alertBuilder.setView(dialogView)
        var dialogAlert = alertBuilder.create()

        val ExitButton =  dialogView.findViewById<Button>(R.id.ExitHeaderNavigation)
        ExitButton.setOnClickListener{
            dialogAlert.dismiss()
        }

        val LinearScrollView = dialogView.findViewById<LinearLayout>(R.id.HeaderNavigationScrollLayouut)
        for (h in Headers){
            //this is where we create the text views
            var NewtextView= TextView(maincontext)
            NewtextView.setText(h)
            NewtextView.setTextColor(maincontext.getColor(R.color.white))
            NewtextView.textSize = 20f
            LinearScrollView.addView(NewtextView)
        }
        dialogAlert.show()
        var DialogWindow = dialogAlert.window
        DialogWindow?.setLayout(580, MATCH_PARENT)
        DialogWindow?.setGravity(RIGHT)
    }
}