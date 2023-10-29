package com.example.thebasementpart3

import android.app.Activity
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.RelativeSizeSpan
import android.text.style.StrikethroughSpan
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView

class TextFormatConfig(private var mainActivity: Activity) {
    val textBox: TextView = mainActivity.findViewById<TextView>(R.id.addTextTxtView)
    fun createTextFormatDialog() {
        val scrollViewLinearLayout =
            mainActivity.findViewById<LinearLayout>(R.id.BottomScrollViewLinearLayout)
        val dialogView = LayoutInflater.from(mainActivity).inflate(R.layout.dialog_text_edit, null)
        scrollViewLinearLayout.addView(dialogView)

        val boldBtn = dialogView.findViewById<Button>(R.id.Boldbtn)
        boldBtn.setOnClickListener {
            editTextSelection("Bold")
        }
        val italicBtn = dialogView.findViewById<Button>(R.id.Italicbtn)
        italicBtn.setOnClickListener {
            editTextSelection("Italic")
        }
        val underlineBtn = dialogView.findViewById<Button>(R.id.Underline)
        underlineBtn.setOnClickListener {
            editTextSelection("Underline")
        }
        val strikeThroughBtn = dialogView.findViewById<Button>(R.id.StrikeThrough)
        strikeThroughBtn.setOnClickListener {
            editTextSelection("StrikeThrough")
        }

        val textSizeSpinner = dialogView.findViewById<Spinner>(R.id.TextSizeSpinner)
        textSizeSpinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                if (textSizeSpinner.selectedItem != null) {updateTextSize(textSizeSpinner.selectedItem.toString().toFloat() / 12)}
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
            }//implement function here
        }

        val fontSpinner = dialogView.findViewById<Spinner>(R.id.FontSpinner)
        fontSpinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {} //implement function here
            override fun onNothingSelected(parent: AdapterView<*>) {}//implement function here
        }
    }

    private fun editTextSelection(selection: String) {
        val startSelection: Int = textBox.selectionStart
        val endSelection: Int = textBox.selectionEnd
        val str = SpannableStringBuilder(textBox.text)
        when (selection) {
            "Bold" -> { str.setSpan(StyleSpan(Typeface.BOLD), startSelection, endSelection, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE) }
            "Italic" -> { str.setSpan(StyleSpan(Typeface.ITALIC), startSelection, endSelection, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE) }
            "Underline" -> { str.setSpan(UnderlineSpan(), startSelection, endSelection, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE) }
            "StrikeThrough" -> { str.setSpan(StrikethroughSpan(), startSelection, endSelection, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)}
        }
    }

    private fun updateTextSize(Size: Float) {
        val startSelection: Int = textBox.selectionStart
        val endSelection: Int = textBox.selectionEnd
        if (textBox.text != "") {
            val str = SpannableStringBuilder(textBox.text)
            if (str.isNotEmpty()) {
                str.setSpan(RelativeSizeSpan(Size), startSelection, endSelection, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                textBox.text = str
            }
        }
    }
}
