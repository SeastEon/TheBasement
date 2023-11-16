package com.example.thebasementpart3

import android.app.Activity
import android.content.pm.PackageManager
import android.view.LayoutInflater
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import android.Manifest
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import java.io.IOException
import java.util.Vector

private var permissionToRecordAccepted = false
private const val REQUEST_RECORD_AUDIO_PERMISSION = 200

class AudioConfig (var db: DataBase): AppCompatActivity() {
    private var permissions: Array<String> = arrayOf(Manifest.permission.RECORD_AUDIO)
    private var fileName: String = ""
    private var recorder: MediaRecorder? = null
    private var player: MediaPlayer? = null
    var AudioFilesLocations = Vector<String>()

    //Allows us to prompt the user to record audio
    fun CreateAudioDialog(){
        val ScrollViewLinearLayout = db.BMObj.mainActivity.findViewById<LinearLayout>(R.id.BottomScrollViewLinearLayout)
        val dialogView =  LayoutInflater.from(db.BMObj.mainActivity).inflate(R.layout.dialog_record_audio, null)
        ScrollViewLinearLayout.addView(dialogView, 0)
        val EditTetRedordName = dialogView.findViewById<EditText>(R.id.TextRecording)
        val AudioName = EditTetRedordName.text.toString()

        val RecordBtn = dialogView.findViewById<ToggleButton>(R.id.RecordBtn)
        RecordBtn.setOnCheckedChangeListener { _, isChecked ->

            if (isChecked) {
                RecordBtn.foreground = AppCompatResources.getDrawable(db.BMObj.mainActivity, R.drawable.pause_icon)
                StartRecording(AudioName) }
            else {
                RecordBtn.foreground = AppCompatResources.getDrawable(db.BMObj.mainActivity, R.drawable.play_arrow)
                StopRecording()
                CreatePlayBackAudioBox(AudioName)
            }
        }
    }

    fun StartRecording(FileName:String) {
        ActivityCompat.requestPermissions(db.BMObj.mainActivity, permissions, REQUEST_RECORD_AUDIO_PERMISSION)
        if(permissionToRecordAccepted == true){
        recorder = MediaRecorder(db.BMObj.mainActivity).apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setOutputFile(FileName)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)

            try { prepare() }
            catch (e: IOException) { Toast.makeText(db.BMObj.mainActivity, "Failed" + e.toString(), Toast.LENGTH_SHORT).show() }
            start()
             }
        }
    }

   fun StopRecording(){
        recorder?.apply {
            stop()
            release()
        }
       recorder = null
   }

    // Requesting permission to RECORD_AUDIO
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionToRecordAccepted = if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION) {
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        } else { false }
        if (!permissionToRecordAccepted) finish()
    }

    //Creates the play back button
    fun CreatePlayBackAudioBox(GivenfileName:String) {
        val MainScrollBarLinearLayout = db.BMObj.mainActivity.findViewById<LinearLayout>(R.id.BasementScrollLinearLayout) //creates an object we can use to access the linear layout

        val playBarsHolder = LinearLayout(db.BMObj.mainActivity)
        playBarsHolder.orientation = LinearLayout.VERTICAL
        playBarsHolder.background =  AppCompatResources.getDrawable(db.BMObj.mainActivity, R.color.darkGreyHeader)

        val PlayButtonHolderText = TextView(db.BMObj.mainActivity)
        PlayButtonHolderText.text = "" //audio name
        PlayButtonHolderText.setTextColor(db.BMObj.mainActivity.getColor(R.color.white))

        val playBars = LinearLayout(db.BMObj.mainActivity)
        playBars.orientation = LinearLayout.HORIZONTAL //we set the linearlayout to a horizon view
        playBars.background =  AppCompatResources.getDrawable(db.BMObj.mainActivity,R.color.GreyBg)

        val playAndPauseButton = ToggleButton(db.BMObj.mainActivity)
        playAndPauseButton.foreground = AppCompatResources.getDrawable(db.BMObj.mainActivity, R.drawable.play_arrow)
        playAndPauseButton.background = AppCompatResources.getDrawable(db.BMObj.mainActivity, R.drawable.record_shape)

        val PressToPlayAudio = TextView(db.BMObj.mainActivity)
        PressToPlayAudio.text = "Play audio"
        PressToPlayAudio.setTextColor(db.BMObj.mainActivity.getColor(R.color.white))

        playAndPauseButton.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                playAndPauseButton.foreground = AppCompatResources.getDrawable(db.BMObj.mainActivity,R.drawable.pause_icon)
                fileName = "${db.BMObj.mainActivity.externalCacheDir?.absolutePath}/" + GivenfileName + ".3gp"
                db.basementchanges.AudioFileLocations?.add(fileName)
                startPlaying()
            } else {
                playAndPauseButton.foreground = AppCompatResources.getDrawable(db.BMObj.mainActivity,R.drawable.play_arrow)
                stopPlaying()
            }
        }
        playBarsHolder.addView(PlayButtonHolderText)
        playBars.addView(playAndPauseButton)
        playBars.addView(PressToPlayAudio)
        playBarsHolder.addView(playBars)
        MainScrollBarLinearLayout.addView(playBarsHolder)
    }

    private fun startPlaying() {
        player = MediaPlayer().apply {
            try {
                setDataSource(fileName)
                prepare()
                start()
            } catch (e: IOException) { Toast.makeText(db.BMObj.mainActivity, "To be implemented", Toast.LENGTH_SHORT).show() }
        }
    }

    private fun stopPlaying() {
        player?.release()
        player = null
    }

}