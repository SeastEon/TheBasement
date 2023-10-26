package com.example.thebasementpart3

import android.app.Activity
import android.graphics.Typeface
import android.icu.lang.UProperty.INT_START
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.StrikethroughSpan
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.get


class TextFormatConfig(var mainActivity: Activity) {
    val TextBox = mainActivity.findViewById<EditText>(R.id.TxtVMainBasememnt)
    fun CreatetextFormatDialog() {
       val ScrollViewLinearLayout = mainActivity.findViewById<LinearLayout>(R.id.BottomScrollViewLinearLayout)
        val dialogView =  LayoutInflater.from(mainActivity).inflate(R.layout.dialog_text_edit, null)
        ScrollViewLinearLayout.addView(dialogView)

        val BoldBtn = dialogView.findViewById<Button>(R.id.Boldbtn)
        BoldBtn.setOnClickListener {
            EditTextSelection("Bold")
        }
        val italicBtn = dialogView.findViewById<Button>(R.id.Italicbtn)
        italicBtn.setOnClickListener {
            EditTextSelection("Italic")
        }
        val UnderlineBtn = dialogView.findViewById<Button>(R.id.Underline)
        UnderlineBtn.setOnClickListener {
            EditTextSelection("Underline")
        }
        val StrikeThroughBtn = dialogView.findViewById<Button>(R.id.StrikeThrough)
        StrikeThroughBtn.setOnClickListener {
            EditTextSelection("StrikeThrough")
        }

        val textSizeSpinner = dialogView.findViewById<Spinner>(R.id.TextSizeSpinner)
        ArrayAdapter.createFromResource(mainActivity, R.array.FontSizes, android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            textSizeSpinner.adapter = adapter
        }
        textSizeSpinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                TextBox.textSize = textSizeSpinner.selectedItem.toString().toFloat()
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
            }//implement function here
        }
        val FontSpinner = dialogView.findViewById<Spinner>(R.id.FontSpinner)
        ArrayAdapter.createFromResource(mainActivity, R.array.Fonts, android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            FontSpinner.adapter = adapter
        }
        FontSpinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {} //implement function here
            override fun onNothingSelected(parent: AdapterView<*>) {}//implement function here
        }
    }

    fun EditTextSelection(selection:String){
        val startSelection: Int = TextBox.selectionStart
        val endSelection: Int = TextBox.selectionEnd
        val str = SpannableStringBuilder(TextBox.text)

        when (selection) {
            "Bold" ->{  str.setSpan(StyleSpan(Typeface.BOLD), startSelection, endSelection, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)}
            "Italic" ->{ str.setSpan(StyleSpan(Typeface.ITALIC), startSelection, endSelection, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE) }
            "Underline" ->{ str.setSpan(UnderlineSpan(), startSelection, endSelection, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)}
            "StrikeThrough" ->{ str.setSpan(StrikethroughSpan(), startSelection, endSelection, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)}
        }
        TextBox.text = str
    }

    fun FontChange(fontName:String){

    }
}