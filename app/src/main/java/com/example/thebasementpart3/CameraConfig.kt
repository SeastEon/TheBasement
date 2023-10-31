package com.example.thebasementpart3

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.MediaController
import android.widget.Toast
import android.widget.VideoView
import androidx.core.app.ActivityCompat.startActivityForResult


class CameraConfig(private var mainActivity: Activity) {
    private val requestCameraCapture = 1
    private val requestVideoCapture = 2
    private val scrollViewLinearLayout: LinearLayout = mainActivity.findViewById(R.id.BottomScrollViewLinearLayout)
    private val textBoxLinearlayout = mainActivity.findViewById<LinearLayout>(R.id.BasementScrollLinearLayout)
    fun createCameraDialog() {
        val dialogView =  LayoutInflater.from(mainActivity).inflate(R.layout.dialog_camera, null)
        dialogView.elevation = 2f
        scrollViewLinearLayout.addView(dialogView, 0)

        val takePictureBtn = dialogView.findViewById<Button>(R.id.TakePictureBtn)
        takePictureBtn.setOnClickListener{
            scrollViewLinearLayout.removeView(dialogView)
            dispatchTakePictureIntent()
        }
        val takeVideoBtn = dialogView.findViewById<Button>(R.id.TakeVideoBtn)
        takeVideoBtn.setOnClickListener{
            scrollViewLinearLayout.removeView(dialogView)
            dispatchTakeVideoIntent()
        }
        val selectPictureOrVideo =  dialogView.findViewById<Button>(R.id.SelectVideoOrPictureBtn)
        selectPictureOrVideo.setOnClickListener{
            scrollViewLinearLayout.removeView(dialogView)
            Toast.makeText(mainActivity, "Not Implemented Yet", Toast.LENGTH_SHORT).show()
        }
    }

fun addPicture(ImageBitmap:Bitmap){
    val imageHolder = ImageView(mainActivity)
    imageHolder.adjustViewBounds = true
    imageHolder.maxHeight= 400
    imageHolder.maxWidth= MATCH_PARENT
    imageHolder.setImageBitmap(ImageBitmap)

    imageHolder.scaleType = ImageView.ScaleType.FIT_CENTER
    textBoxLinearlayout.addView(imageHolder)
    }

    fun addVideo(videoHolder: VideoView){
        val mediaController = MediaController(mainActivity)
        mediaController.setAnchorView(videoHolder)
        mediaController.setMediaPlayer(videoHolder)
        videoHolder.setMediaController(mediaController)
        textBoxLinearlayout.addView(videoHolder)
    }


    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(mainActivity, takePictureIntent,requestCameraCapture , null)
            Toast.makeText(mainActivity, "Picture taken Successfully", Toast.LENGTH_SHORT).show()
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(mainActivity, "Shit's broke", Toast.LENGTH_SHORT).show()
        }
    }

    private fun dispatchTakeVideoIntent() {
        val takeVideoIntent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
        if (takeVideoIntent.resolveActivity(mainActivity.packageManager) != null){
            startActivityForResult(mainActivity, takeVideoIntent, requestVideoCapture, null)
            Toast.makeText(mainActivity, "Video taken Successfully", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(mainActivity, "Shit's broke", Toast.LENGTH_SHORT).show()
            }
    }
}