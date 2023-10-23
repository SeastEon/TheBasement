package com.example.thebasementpart3

import android.app.Activity
import android.content.Context
import android.graphics.drawable.ClipDrawable.HORIZONTAL
import android.icu.lang.UCharacter.IndicPositionalCategory.TOP
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.common.io.LineReader

class TextFormatConfig(var mainActivity: Activity) {
    fun CreatetextFormatDialog() {
       val ScrollViewLinearLayout = mainActivity.findViewById<LinearLayout>(R.id.BottomScrollViewLinearLayout)
        val dialogView =  LayoutInflater.from(mainActivity).inflate(R.layout.dialog_text_edit, null)
        ScrollViewLinearLayout.addView(dialogView)

        val BoldBtn = dialogView.findViewById<Button>(R.id.Boldbtn)
        BoldBtn.setOnClickListener {
            ScrollViewLinearLayout.removeView(dialogView)
            Toast.makeText(mainActivity, "Not Implemented Yet", Toast.LENGTH_SHORT).show()
        }
        val italicBtn = dialogView.findViewById<Button>(R.id.Italicbtn)
        italicBtn.setOnClickListener {
            ScrollViewLinearLayout.removeView(dialogView)
            Toast.makeText(mainActivity, "Not Implemented Yet", Toast.LENGTH_SHORT).show()
        }
        val UnderlineBtn = dialogView.findViewById<Button>(R.id.Underline)
        UnderlineBtn.setOnClickListener {
            ScrollViewLinearLayout.removeView(dialogView)
            Toast.makeText(mainActivity, "Not Implemented Yet", Toast.LENGTH_SHORT).show()
        }
        val StrikeThroughBtn = dialogView.findViewById<Button>(R.id.StrikeThrough)
        StrikeThroughBtn.setOnClickListener {
            ScrollViewLinearLayout.removeView(dialogView)
            Toast.makeText(mainActivity, "Not Implemented Yet", Toast.LENGTH_SHORT).show()
        }

        val textSizeSpinner = dialogView.findViewById<Spinner>(R.id.TextSizeSpinner)
        ArrayAdapter.createFromResource(
            mainActivity,
            R.array.Fonts,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears.
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner.
            textSizeSpinner.adapter = adapter
        }

        textSizeSpinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {}//implement function here
            override fun onNothingSelected(parent: AdapterView<*>) {}//implement function here
        }
        val FontSpinner = dialogView.findViewById<Spinner>(R.id.FontSpinner)
        ArrayAdapter.createFromResource(
            mainActivity,
            R.array.FontSizes,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears.
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner.
            FontSpinner.adapter = adapter
        }
        FontSpinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {} //implement function here
            override fun onNothingSelected(parent: AdapterView<*>) {}//implement function here
        }
    }
}