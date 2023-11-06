package com.example.thebasementpart3

import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Space
import android.widget.Spinner
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.children
import java.util.Vector

class CreateCell(private var mainActivity: MainActivity) {
    private var xCellValue = 1
    private var cellYSize = 1
    var CellsInGrid = Vector<GridCapture>()

       fun createGridDialog() {
        val scrollViewLinearLayout = mainActivity.findViewById<LinearLayout>(R.id.BottomScrollViewLinearLayout)
        val dialogView = LayoutInflater.from(mainActivity).inflate(R.layout.dialog_create_cells, null)
        scrollViewLinearLayout.addView(dialogView, 0)

        val xSizeSpinner = dialogView.findViewById<Spinner>(R.id.CellXSize)
        ArrayAdapter.createFromResource(mainActivity, R.array.CellSizes, android.R.layout.simple_spinner_item).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)// Specify the layout to use when the list of choices appears.
            xSizeSpinner.adapter = adapter// Apply the adapter to the spinner.
        }
        xSizeSpinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                xCellValue = xSizeSpinner.selectedItem.toString().toInt()
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}// tell the user to pick something
        }

        val ySizeSpinner = dialogView.findViewById<Spinner>(R.id.CellYZise)
        ArrayAdapter.createFromResource(mainActivity, R.array.CellSizes, android.R.layout.simple_spinner_item).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) // Specify the layout to use when the list of choices appears.
            ySizeSpinner.adapter = adapter// Apply the adapter to the spinner.
        }
        ySizeSpinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                cellYSize = ySizeSpinner.selectedItem.toString().toInt()
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}// write code to perform some action
        }

        val createGridBtn= mainActivity.findViewById<Button>(R.id.CreateGrid)
        createGridBtn.setOnClickListener{
            scrollViewLinearLayout.removeView(dialogView)
            createGrid()
        }
    }

    data class GridCapture(var Layout:LinearLayout, var gridX:Int, var gridY:Int, var maxXval:Int, val masYVal:Int, var GridLayout:EditText)

    private fun createGrid(){
        val scrollViewLinearLayout = mainActivity.findViewById<LinearLayout>(R.id.BasementScrollLinearLayout)
        val screenWidth = scrollViewLinearLayout.width

        val yCellLayout = LinearLayout(mainActivity) //needed once
        yCellLayout.orientation = LinearLayout.VERTICAL
        yCellLayout.background = AppCompatResources.getDrawable(mainActivity,R.color.LightGrey )
        yCellLayout.background.alpha = 40

        var gridX = screenWidth / xCellValue

        // creates the grid
        for (Y in 0 until cellYSize){
            val xCellLayout = LinearLayout(mainActivity) //need multiple of these to create the frid
            xCellLayout.orientation = LinearLayout.HORIZONTAL
            for (X in 0 until gridX * screenWidth){
                val newEditText = EditText(mainActivity)
                newEditText.width = gridX
                xCellLayout.addView(newEditText)
                CellsInGrid.add(GridCapture(yCellLayout,X, Y, xCellValue, cellYSize, newEditText))
            }
            yCellLayout.addView(xCellLayout)
        }
        val spacer = Space(mainActivity)
        spacer.minimumHeight = 10
        scrollViewLinearLayout.addView(spacer)
        scrollViewLinearLayout.addView(yCellLayout)
    }
}

