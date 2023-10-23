package com.example.thebasementpart3

import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.Toast

class CreateCell(var mainActivity: MainActivity) {
    fun CreateGridDialouge() {
        val ScrollViewLinearLayout =
            mainActivity.findViewById<LinearLayout>(R.id.BottomScrollViewLinearLayout)
        val dialogView =
            LayoutInflater.from(mainActivity).inflate(R.layout.dialog_create_cells, null)
        ScrollViewLinearLayout.addView(dialogView)

        val XSizeSpinner = dialogView.findViewById<Spinner>(R.id.CellXSize)
        ArrayAdapter.createFromResource(mainActivity, R.array.CellSizes, android.R.layout.simple_spinner_item).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)// Specify the layout to use when the list of choices appears.
            XSizeSpinner.adapter = adapter// Apply the adapter to the spinner.
        }
        XSizeSpinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                //implement function here
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
                //implement function here
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}// write code to perform some action
        }

        val CreatGrid= mainActivity.findViewById<Button>(R.id.CreateGrid)
        CreatGrid.setOnClickListener{
            ScrollViewLinearLayout.removeView(dialogView)
            Toast.makeText(mainActivity, "Not Implemented Yet", Toast.LENGTH_SHORT).show()
        }
    }
}

