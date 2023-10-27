package com.example.thebasementpart3

import android.graphics.drawable.ClipDrawable.HORIZONTAL
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.GridLayout
import android.widget.LinearLayout
import android.widget.Space
import android.widget.Spinner
import android.widget.Toast
import androidx.core.view.get

class CreateCell(var mainActivity: MainActivity) {
    private var XCellValue = 1
    private var CellYSize = 1
    fun CreateGridDialouge() {
        val ScrollViewLinearLayout = mainActivity.findViewById<LinearLayout>(R.id.BottomScrollViewLinearLayout)
        val dialogView = LayoutInflater.from(mainActivity).inflate(R.layout.dialog_create_cells, null)
        ScrollViewLinearLayout.addView(dialogView)

        val XSizeSpinner = dialogView.findViewById<Spinner>(R.id.CellXSize)
        ArrayAdapter.createFromResource(mainActivity, R.array.CellSizes, android.R.layout.simple_spinner_item).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)// Specify the layout to use when the list of choices appears.
            XSizeSpinner.adapter = adapter// Apply the adapter to the spinner.
        }
        XSizeSpinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                XCellValue = XSizeSpinner.selectedItem.toString().toInt()
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}// tell the user to pick something
        }

        val YSizeSpinner = dialogView.findViewById<Spinner>(R.id.CellYZise)
        ArrayAdapter.createFromResource(mainActivity, R.array.CellSizes, android.R.layout.simple_spinner_item).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) // Specify the layout to use when the list of choices appears.
            YSizeSpinner.adapter = adapter// Apply the adapter to the spinner.
        }
        YSizeSpinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                CellYSize = YSizeSpinner.selectedItem.toString().toInt()
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}// write code to perform some action
        }

        val CreatGrid= mainActivity.findViewById<Button>(R.id.CreateGrid)
        CreatGrid.setOnClickListener{
            ScrollViewLinearLayout.removeView(dialogView)
            CreateGrid()
        }
    }

    fun CreateGrid(){
        val ScrollViewLinearLayout = mainActivity.findViewById<LinearLayout>(R.id.BasementScrollLinearLayout)
        val screenWidth = ScrollViewLinearLayout.width
        var CellXSize = screenWidth / XCellValue

        var YCellLayout = LinearLayout(mainActivity) //needed once
        YCellLayout.orientation = LinearLayout.VERTICAL
        YCellLayout.background = mainActivity.getDrawable(R.color.LightGrey)
        YCellLayout.background.alpha = 40

        // creates the grid
        for (Y in 0 until CellYSize){
            var XCellLayout = LinearLayout(mainActivity) //need mulitple of
            XCellLayout.orientation = LinearLayout.HORIZONTAL
            for (X in 0 until XCellValue){
                var newEditText = EditText(mainActivity)
                newEditText.width = CellXSize
                XCellLayout.addView(newEditText)
            }
            YCellLayout.addView(XCellLayout)
        }
        val spacer = Space(mainActivity)
        spacer.minimumHeight = 10
        ScrollViewLinearLayout.addView(spacer)
        ScrollViewLinearLayout.addView(YCellLayout)
    }
}

