package com.example.thebasementpart3

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    val REQUEST_IMAGE_CAPTURE = 1
    val REQUEST_VIDEO_CAPTURE = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)        // var BasementId = UUID.randomUUID().toString()
        var Bottomlayout = findViewById<LinearLayout>(R.id.BottomScrollViewLinearLayout)
        var MainText = findViewById<EditText>(R.id.TxtVMainBasememnt)
        MainText.background.alpha = 150

        var BaseMTObj = BasementObject(findViewById<EditText>(R.id.TxtVMainBasememnt), this)
        val db = DataBase(this, BaseMTObj) //the database is initialized using the main context to display successes or failures

        db.GetBasementFromDatabase()
        BaseMTObj.SetTextBox(BaseMTObj.SetBasementText(db.ReturnedDoc))

        val exportBtn = findViewById<Button>(R.id.ExportTodataBase)
        exportBtn.setOnClickListener {
            BaseMTObj = BasementObject(findViewById<EditText>(R.id.TxtVMainBasememnt), this) //resets the textbox to the text inside before using
            db.addBasementToDatabase(BaseMTObj)
        }

        MainText.setOnClickListener{ MainTextClick(Bottomlayout, "Main") }

        val SecondaryText = findViewById<EditText>(R.id.addTextTxtView)
        SecondaryText.setOnClickListener{MainTextClick(Bottomlayout, "")}

        val eraseBasement = findViewById<Button>(R.id.EraseBasement)
        eraseBasement.setOnClickListener{ db.ClearBasementDialog(MainText) }

        val OpenHeaderNavigation = findViewById<Button>(R.id.OpenHeaderNavigation)
        OpenHeaderNavigation.setOnClickListener { BasementObject(findViewById<Button>(R.id.TxtVMainBasememnt), this).HeaderNav.StartUpHeader() }

        val openRecordDialog = findViewById<Button>(R.id.OpenRecordAudioDialogBtn)
        openRecordDialog.setOnClickListener{ CallBottomLinearLayoutFunctions("RecordAudio", Bottomlayout) }

        val OpenCamera= findViewById<Button>(R.id.cameraBtn)
        OpenCamera.setOnClickListener{ CallBottomLinearLayoutFunctions("Camera", Bottomlayout) }

        val OpenTextFormatted = findViewById<Button>(R.id.TextFormatter)
        OpenTextFormatted.setOnClickListener{ CallBottomLinearLayoutFunctions("TextFormatter", Bottomlayout) }

        val OpenGridCreation = findViewById<Button>(R.id.OpenGridDialouge)
        OpenGridCreation.setOnClickListener{ CallBottomLinearLayoutFunctions("CreateCells", Bottomlayout) }

        val TextAdderBtn = findViewById<Button>(R.id.AddTextBtn)
        TextAdderBtn.setOnClickListener{ BasementObject(findViewById<Button>(R.id.TxtVMainBasememnt), this).TextAdded() }
    }

    fun CallBottomLinearLayoutFunctions(FunctionToCall:String, Bottomlayout:LinearLayout){
        if(Bottomlayout.childCount >= 3){ Bottomlayout.removeViewAt(2) }
        when (FunctionToCall) {
            "RecordAudio" -> {AudioConfig(this).CreateAudioDialog()}
            "Camera" -> { CameraConfig(this).CreatecameraDialouge() }
            "TextFormatter" -> { TextFormatConfig(this).CreatetextFormatDialog() }
            "CreateCells" -> {CreateCell(this).CreateGridDialouge()}
        }
        this.currentFocus?.let { view ->
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    fun MainTextClick(Bottomlayout:LinearLayout, WhichTextSelected:String){
        if (Bottomlayout.childCount > 2 ){ Bottomlayout.removeViewAt(2) }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        var camaraConfig = CameraConfig(this)

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            camaraConfig.AddPicture(imageBitmap)
        }
        else if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            val videoUri = data?.data
            if (videoUri != null) {
                val videoUri: Uri? = intent.data
                var videoView = VideoView(this)
                videoView.setVideoURI(videoUri)
                camaraConfig.AddVideo(videoView)
            }
            else{
                Toast.makeText(this, "Video not loaded", Toast.LENGTH_SHORT).show()
            }
        }
    }
}