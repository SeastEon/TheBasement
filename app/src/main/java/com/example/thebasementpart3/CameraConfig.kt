package com.example.thebasementpart3

import android.app.Activity
import android.graphics.Matrix
import android.view.Gravity.CENTER
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog


class CameraConfig(var mainActivity: Activity) {

    fun CreatecameraDialouge() {
        val ScrollViewLinearLayout = mainActivity.findViewById<LinearLayout>(R.id.BottomScrollViewLinearLayout)
        val dialogView =  LayoutInflater.from(mainActivity).inflate(R.layout.dialog_camera, null)
        ScrollViewLinearLayout.addView(dialogView)

        var TakePictureBtn = dialogView.findViewById<Button>(R.id.TakePictureBtn)
        TakePictureBtn.setOnClickListener{
            ScrollViewLinearLayout.removeView(dialogView)
            AddStandInPicture()
        }
        var takeVideoBtn = dialogView.findViewById<Button>(R.id.TakeVideoBtn)
        takeVideoBtn.setOnClickListener{
            ScrollViewLinearLayout.removeView(dialogView)
            Toast.makeText(mainActivity, "Not Implemented Yet", Toast.LENGTH_SHORT).show()
        }
        var SelectPictureOrVideo =  dialogView.findViewById<Button>(R.id.SelectVideoOrPictureBtn)
        SelectPictureOrVideo.setOnClickListener{
            ScrollViewLinearLayout.removeView(dialogView)
            Toast.makeText(mainActivity, "Not Implemented Yet", Toast.LENGTH_SHORT).show()
        }

    }

fun AddStandInPicture(){
    var Scrolllayout = mainActivity.findViewById<LinearLayout>(R.id.BasementScrollLinearLayout)
    var ImageHolder = ImageView(mainActivity)
    ImageHolder.adjustViewBounds = true
    ImageHolder.maxHeight= 400
    ImageHolder.maxWidth= MATCH_PARENT
    ImageHolder.setImageDrawable(mainActivity.getDrawable(R.drawable.img_0168))

    ImageHolder.scaleType = ImageView.ScaleType.FIT_CENTER
    Scrolllayout.addView(ImageHolder)
    }
}