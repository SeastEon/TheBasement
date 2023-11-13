package com.example.thebasementpart3

import android.app.Activity
import android.graphics.Paint
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.style.RelativeSizeSpan
import android.text.style.StrikethroughSpan
import android.text.style.StyleSpan
import android.text.style.TypefaceSpan
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.Spinner
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat.getFont


class TextFormatConfig(private var mainActivity: Activity) {
    val textBox: TextView = mainActivity.findViewById<TextView>(R.id.addTextTxtView)
    var currentTextSize = 12f
    val dialogView = LayoutInflater.from(mainActivity).inflate(R.layout.dialog_text_edit, null)
    fun createTextFormatDialog() {
        val scrollViewLinearLayout = mainActivity.findViewById<LinearLayout>(R.id.BottomScrollViewLinearLayout)
        scrollViewLinearLayout.addView(dialogView, 0)

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
                if (textSizeSpinner.selectedItem != null) {updateTextSize(textSizeSpinner.selectedItem.toString().toFloat())}
            }
            override fun onNothingSelected(parent: AdapterView<*>) {} //does not do anything when nothing is selected
        }

        val fontSpinner = dialogView.findViewById<Spinner>(R.id.FontSpinner)
        fontSpinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                if (textBox.text != "" &&  fontSpinner.selectedItem != ""){
                    if (textSizeSpinner.selectedItem != null) {updateFontType(fontSpinner.selectedItem.toString())}
                }

            }
            override fun onNothingSelected(parent: AdapterView<*>) {}//does not do anything when nothing is selected
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
        textBox.text = str
    }

    private fun updateTextSize(Size: Float) {
        val startSelection: Int = textBox.selectionStart
        val endSelection: Int = textBox.selectionEnd
        var calculatedSize = (Size/ currentTextSize)
        if (textBox.text != "") {
            val str = SpannableStringBuilder(textBox.text)

            if (str.isNotEmpty()) {
                str.setSpan(RelativeSizeSpan(calculatedSize), startSelection, endSelection, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                textBox.text = str
            }
        }
        currentTextSize = Size
    }

    private fun updateFontType(Font:String) {
        val startSelection: Int = textBox.selectionStart
        val endSelection: Int = textBox.selectionEnd
        if (textBox.text != "") {
            val str = SpannableStringBuilder(textBox.text)
            if (str.isNotEmpty()) {
            when (Font) {
                "Droid Sans" -> {}
                "Mono" -> {}
                "Droid Serif" -> {}
                "Broken 15" -> { str.setSpan(getFont(mainActivity, R.font.font_broken15)?.let { CustomTypefaceSpan("", it) }, startSelection, endSelection, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)}
                "Fantasy Magist" ->  { str.setSpan(getFont(mainActivity, R.font.font_fantasy_magist)?.let { CustomTypefaceSpan("", it) }, startSelection, endSelection, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)}
                "Cheese Burger" ->   { str.setSpan(getFont(mainActivity, R.font.font_cheeseburger)?.let { CustomTypefaceSpan("", it) }, startSelection, endSelection, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)}
                "Caviar Dreams" ->   { str.setSpan(getFont(mainActivity, R.font.font_caviar_dreams)?.let { CustomTypefaceSpan("", it) }, startSelection, endSelection, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)}
                "DCC-Ash" ->   { str.setSpan(getFont(mainActivity, R.font.font_dcc_ash)?.let { CustomTypefaceSpan("", it) }, startSelection, endSelection, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)}
                "Louis George Cafe" ->  { str.setSpan(getFont(mainActivity, R.font.font_louis_george_cafe)?.let { CustomTypefaceSpan("", it) }, startSelection, endSelection, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)}
                "Next Sunday" ->  { str.setSpan(getFont(mainActivity, R.font.font_next_sunday)?.let { CustomTypefaceSpan("", it) }, startSelection, endSelection, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)}
                "Verve" ->   { str.setSpan(getFont(mainActivity, R.font.font_verve)?.let { CustomTypefaceSpan("", it) }, startSelection, endSelection, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)}
            }
                textBox.text = str
            }
        }
    }
}

class CustomTypefaceSpan(family: String?, private val newType: Typeface) :
    TypefaceSpan(family) {
    override fun updateDrawState(ds: TextPaint) {
        applyCustomTypeFace(ds, newType)
    }

    override fun updateMeasureState(paint: TextPaint) {
        applyCustomTypeFace(paint, newType)
    }

    companion object {
        private fun applyCustomTypeFace(paint: Paint, tf: Typeface) {
            val oldStyle: Int
            val old = paint.typeface
            oldStyle = old?.style ?: 0
            val fake = oldStyle and tf.style.inv()
            if (fake and Typeface.BOLD != 0) {
                paint.isFakeBoldText = true
            }
            if (fake and Typeface.ITALIC != 0) {
                paint.textSkewX = -0.25f
            }
            paint.typeface = tf
        }
    }

}
