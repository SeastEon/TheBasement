package com.example.thebasementpart3

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View.VISIBLE
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.MediaController
import android.widget.Toast
import android.widget.VideoView
import androidx.core.app.ActivityCompat.startActivityForResult


class CameraConfig(var mainActivity: Activity) {
    val REQUEST_IMAGE_CAPTURE = 1
    val REQUEST_VIDEO_CAPTURE = 2
    val ScrollViewLinearLayout = mainActivity.findViewById<LinearLayout>(R.id.BottomScrollViewLinearLayout)
    val TextBoxLinearlayout = mainActivity.findViewById<LinearLayout>(R.id.BasementScrollLinearLayout)
    fun CreatecameraDialouge() {

        val dialogView =  LayoutInflater.from(mainActivity).inflate(R.layout.dialog_camera, null)
        ScrollViewLinearLayout.addView(dialogView)

        var TakePictureBtn = dialogView.findViewById<Button>(R.id.TakePictureBtn)
        TakePictureBtn.setOnClickListener{
            ScrollViewLinearLayout.removeView(dialogView)
            dispatchTakePictureIntent()
        }
        var takeVideoBtn = dialogView.findViewById<Button>(R.id.TakeVideoBtn)
        takeVideoBtn.setOnClickListener{
            ScrollViewLinearLayout.removeView(dialogView)
            dispatchTakeVideoIntent()
        }
        var SelectPictureOrVideo =  dialogView.findViewById<Button>(R.id.SelectVideoOrPictureBtn)
        SelectPictureOrVideo.setOnClickListener{
            ScrollViewLinearLayout.removeView(dialogView)
            Toast.makeText(mainActivity, "Not Implemented Yet", Toast.LENGTH_SHORT).show()
        }

    }

fun AddPicture(ImageBitmap:Bitmap){
    var ImageHolder = ImageView(mainActivity)
    ImageHolder.adjustViewBounds = true
    ImageHolder.maxHeight= 400
    ImageHolder.maxWidth= MATCH_PARENT
    ImageHolder.setImageBitmap(ImageBitmap)

    ImageHolder.scaleType = ImageView.ScaleType.FIT_CENTER
    TextBoxLinearlayout.addView(ImageHolder)
    }

    fun AddVideo(videoHolder: VideoView){
        val mediaController = MediaController(mainActivity)
        mediaController.setAnchorView(videoHolder)
        mediaController.setMediaPlayer(videoHolder)
        videoHolder.setMediaController(mediaController);
        TextBoxLinearlayout.addView(videoHolder)
    }


    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(mainActivity, takePictureIntent,REQUEST_IMAGE_CAPTURE , null)
            Toast.makeText(mainActivity, "Picture taken Successfully", Toast.LENGTH_SHORT).show()
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(mainActivity, "Shit's broke", Toast.LENGTH_SHORT).show()
        }
    }

    private fun dispatchTakeVideoIntent() {
        val takeVideoIntent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
        try {
            takeVideoIntent.resolveActivity(mainActivity.packageManager)
            startActivityForResult(mainActivity, takeVideoIntent, REQUEST_VIDEO_CAPTURE, null)
            Toast.makeText(mainActivity, "Video taken Successfully", Toast.LENGTH_SHORT).show()
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(mainActivity, "Shit's broke", Toast.LENGTH_SHORT).show()
            }
    }
}