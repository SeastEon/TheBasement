package com.example.thebasementpart3

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.os.CountDownTimer
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import java.util.Timer
import kotlin.concurrent.timerTask


class MainActivity : AppCompatActivity() {
    private val requestImageCapture = 1
    private val requestVideoCapture = 2

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // var BasementId = UUID.randomUUID().toString()
        val bottomLayout = findViewById<LinearLayout>(R.id.BottomScrollViewLinearLayout)

        var baseMTObj = BasementObject(this)
        val db = DataBase(
            this,
            baseMTObj
        ) //the database is initialized using the main context to display successes or failures

        db.getBasementFromDatabase()
        baseMTObj.setTextBox(baseMTObj.setBasementText(db.returnedDoc))

        val exportBtn = findViewById<Button>(R.id.ExportToDataBaseBtn)
        exportBtn.setOnClickListener {
            baseMTObj = BasementObject(this) //resets the textBox to the text inside before using
            db.addBasementToDatabase(baseMTObj)
        }

        val secondaryEditText = findViewById<EditText>(R.id.addTextTxtView)
        secondaryEditText.setOnClickListener { mainTextClick(bottomLayout) }

        val eraseBasementBtn = findViewById<Button>(R.id.EraseBasement)
        eraseBasementBtn.setOnClickListener { } // clear Basement needs to be overhauled

        val openHeaderNavigationBtn = findViewById<Button>(R.id.OpenHeaderNavigation)
        openHeaderNavigationBtn.setOnClickListener { BasementObject(this).headerNav.startUpHeader() }

        val openRecordDialogBtn = findViewById<Button>(R.id.OpenRecordAudioDialogBtn)
        openRecordDialogBtn.setOnClickListener {
            callBottomLinearLayoutFunctions(
                "RecordAudio",
                bottomLayout
            )
        }

        val openCameraBtn = findViewById<Button>(R.id.cameraBtn)
        openCameraBtn.setOnClickListener { callBottomLinearLayoutFunctions("Camera", bottomLayout) }

        val openTextFormattedBtn = findViewById<Button>(R.id.TextFormatter)
        openTextFormattedBtn.setOnClickListener {
            callBottomLinearLayoutFunctions(
                "TextFormatter",
                bottomLayout
            )
        }

        val openGridCreationBtn = findViewById<Button>(R.id.OpenGridDialogBtn)
        openGridCreationBtn.setOnClickListener {
            callBottomLinearLayoutFunctions(
                "CreateCells",
                bottomLayout
            )
        }

        val textAdderBtn = findViewById<Button>(R.id.AddTextBtn)
        textAdderBtn.setOnClickListener {
            val RadioTime = findViewById<RadioButton>(R.id.RadioTimeBtn)
            val RadioHeader = findViewById<RadioButton>(R.id.RadioHeaderBtn)

            if(RadioTime.isChecked){
                baseMTObj.SetText()
                RadioTime.isChecked = false
            }//Do Nothing yet. Will allow the user to set a notification for later
            else if(RadioHeader.isChecked){
                baseMTObj.SetHeader()
                RadioHeader.isChecked = false
            }
            else{baseMTObj.SetText() }
            baseMTObj = BasementObject(this)
            db.addBasementToDatabase(baseMTObj)
        }
    }

    private fun callBottomLinearLayoutFunctions(FunctionToCall:String, bottomLayout:LinearLayout){
        if(bottomLayout.childCount >= 2){ bottomLayout.removeViewAt(1) }
        when (FunctionToCall) {
            "RecordAudio" -> {AudioConfig(this).CreateAudioDialog()}
            "Camera" -> { CameraConfig(this).createCameraDialog() }
            "TextFormatter" -> { TextFormatConfig(this).createTextFormatDialog() }
            "CreateCells" -> {CreateCell(this).createGridDialog()}
        }
        this.currentFocus?.let { view ->
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    private fun mainTextClick(bottomLayout:LinearLayout){
        if (bottomLayout.childCount > 1 ){ bottomLayout.removeViewAt(1) }
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