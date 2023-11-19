package com.example.thebasementpart3

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.MediaController
import android.widget.Toast
import android.widget.VideoView
import androidx.core.app.ActivityCompat.startActivityForResult
import java.io.File
import java.io.IOException
import java.io.OutputStreamWriter
import java.util.Vector


class CameraConfig(private var mainActivity: Activity) {
    private val requestCameraCapture = 1
    private val requestVideoCapture = 2
    var numberOfPictures = 0

    private val scrollViewLinearLayout: LinearLayout = mainActivity.findViewById(R.id.BottomScrollViewLinearLayout)
    private val textBoxLinearlayout = mainActivity.findViewById<LinearLayout>(R.id.BasementScrollLinearLayout)
    var dataBase:DataBase? = null
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

    fun SetDB(dataBase: DataBase){
        this.dataBase = dataBase
    }
fun addPicture(ImageBitmap:Bitmap){
    val imageHolder = ImageView(mainActivity)
    imageHolder.adjustViewBounds = true
    imageHolder.maxHeight= 400
    imageHolder.maxWidth= MATCH_PARENT
    imageHolder.setImageBitmap(ImageBitmap)
    numberOfPictures++
    writeToFile(ImageBitmap, null, mainActivity, "Camera", dataBase)
    imageHolder.scaleType = ImageView.ScaleType.FIT_CENTER
    textBoxLinearlayout.addView(imageHolder)
    }
    fun addPicture(ImageFile:File){
        var FileBitmap = BitmapFactory.decodeFile(ImageFile.path);
        val imageHolder = ImageView(mainActivity)
        imageHolder.adjustViewBounds = true
        imageHolder.maxHeight= 400
        imageHolder.maxWidth= MATCH_PARENT
        imageHolder.setImageBitmap(FileBitmap)
        numberOfPictures++
        writeToFile(FileBitmap, null, mainActivity, "Camera", dataBase)
        imageHolder.scaleType = ImageView.ScaleType.FIT_CENTER
        textBoxLinearlayout.addView(imageHolder)
    }

    fun addVideo(uri: Uri?){
        val videoView = VideoView(mainActivity)
        videoView.setVideoURI(uri)
        val mediaController = MediaController(mainActivity)
        mediaController.setAnchorView(videoView)
        mediaController.setMediaPlayer(videoView)
        videoView.setMediaController(mediaController)
        writeToFile(null, uri, mainActivity, "Video", dataBase)
        textBoxLinearlayout.addView(videoView)
    }


    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(mainActivity, takePictureIntent,requestCameraCapture , null)
            Toast.makeText(mainActivity, "Picture taken Successfully", Toast.LENGTH_SHORT).show()
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(mainActivity, "To be implemented", Toast.LENGTH_SHORT).show()
        }
    }

    private fun dispatchTakeVideoIntent() {
        val takeVideoIntent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
        if (takeVideoIntent.resolveActivity(mainActivity.packageManager) != null){
            startActivityForResult(mainActivity, takeVideoIntent, requestVideoCapture, null)
            Toast.makeText(mainActivity, "Video taken Successfully", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(mainActivity, "To be implemented", Toast.LENGTH_SHORT).show()
            }
    }

    private fun writeToFile(PictureData:Bitmap?, VideoData:Uri?,  context: Context, Type:String, dataBase: DataBase?) {
        var basementPicture: Vector<String>? = null
        if(Type == "Camera"){
            try {
                var fileOutputStream = context.openFileOutput("BasementPicture$numberOfPictures.jpeg", Context.MODE_PRIVATE);
                if (PictureData != null) { PictureData.compress(Bitmap.CompressFormat.JPEG, 90, fileOutputStream) };
                var BasementPictureFile = dataBase?.BMObj?.mainActivity?.getFileStreamPath("BasementPicture$numberOfPictures.jpeg")
                if (basementPicture != null && dataBase != null && BasementPictureFile != null) {
                        dataBase.basementchanges.PictureLocations?.add(BasementPictureFile.path)
                }
                fileOutputStream.close();
            } catch (e: IOException) {
                Log.e("Exception", "File write failed: $e")
            }
        }
        else if(Type == "Video"){
            var file = File(VideoData?.getPath().toString())
            var BasementVideoFile = dataBase?.BMObj?.mainActivity?.getFileStreamPath(file.name)
            if (basementPicture != null && dataBase != null && BasementVideoFile != null) {
              dataBase.basementchanges.VideoLocations?.add(BasementVideoFile.path)
            }
        }
    }
}