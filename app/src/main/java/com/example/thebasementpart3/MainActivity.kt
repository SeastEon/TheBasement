package com.example.thebasementpart3

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private val requestImageCapture = 1
    private val requestVideoCapture = 2

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val mainLayout = findViewById<RelativeLayout>(R.id.MainLinearLayout)
        val mainTextView = findViewById<TextView>(R.id.addTextTxtView)
        val bottomLayout = findViewById<LinearLayout>(R.id.BottomScrollViewLinearLayout)
        var baseMTObj = BasementObject(this)
        var header = NavigateHeader(baseMTObj)
        val db = DataBase(baseMTObj) //the database is initialized using the main context to display successes or failures
        db.getInformationFromDatabase()

        mainLayout.setOnClickListener{ db.Configuredatabase(mainTextView) }

        val exportBtn = findViewById<Button>(R.id.ExportToDataBaseBtn)
        exportBtn.setOnClickListener {ShareConfig(db).CreateShareDialog() }

        findViewById<Button>(R.id.EraseBasement).setOnClickListener { db.clearBasementDialog() } // clear Basement needs to be overhauled

        findViewById<Button>(R.id.OpenHeaderNavigation).setOnClickListener { header.startUpHeader() }

        var recordOn = false
        findViewById<Button>(R.id.OpenRecordAudioDialogBtn).setOnClickListener { recordOn = callBottomLinearLayoutFunctions("RecordAudio", bottomLayout, recordOn) }

        var cameraOn = false
        findViewById<Button>(R.id.cameraBtn).setOnClickListener { cameraOn = callBottomLinearLayoutFunctions("Camera", bottomLayout, cameraOn)}

        var textOn = false
        findViewById<Button>(R.id.TextFormatter).setOnClickListener { textOn = callBottomLinearLayoutFunctions("TextFormatter", bottomLayout, textOn)}

        var gridOn = false
        findViewById<Button>(R.id.OpenGridDialogBtn).setOnClickListener { gridOn = callBottomLinearLayoutFunctions("CreateCells", bottomLayout, gridOn)}
    }

    private fun callBottomLinearLayoutFunctions(FunctionToCall:String, bottomLayout:LinearLayout, FeatureOn:Boolean): Boolean{
        var BoolReturn = false
        if(!FeatureOn){
            if(bottomLayout.childCount >= 2){ bottomLayout.removeViewAt(0) }
            when (FunctionToCall) {
                "RecordAudio" -> { AudioConfig(this).CreateAudioDialog() }
                "Camera" -> { CameraConfig(this).createCameraDialog() }
                "TextFormatter" -> { TextFormatConfig(this).createTextFormatDialog() }
                "CreateCells" -> { CreateCell(this).createGridDialog() }
            }
            this.currentFocus?.let { view ->
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                imm?.hideSoftInputFromWindow(view.windowToken, 0)
            }
            BoolReturn = true
        }
        else if(FeatureOn){
            if (bottomLayout.childCount > 1){ bottomLayout.removeViewAt(0) }
            BoolReturn = false
        }
        return BoolReturn
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val camaraConfig = CameraConfig(this)

        if (requestCode == requestImageCapture && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            camaraConfig.addPicture(imageBitmap)
        }
        else if (requestCode == requestVideoCapture && resultCode == RESULT_OK) {
            val videoUri = data?.data
            if (videoUri != null) {
                val videoView = VideoView(this)
                videoView.setVideoURI(intent.data)
                camaraConfig.addVideo(videoView)
            }
            else{
                Toast.makeText(this, "Video not loaded", Toast.LENGTH_SHORT).show()
            }
        }
    }
}