package com.example.thebasementpart3

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.media.AudioRecord
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ToggleButton
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.File
import java.io.FileOutputStream
import android.Manifest
import android.graphics.drawable.ClipDrawable.VERTICAL
import android.graphics.drawable.Drawable
import android.icu.lang.UCharacter.VerticalOrientation
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.net.VpnService.prepare
import android.util.Log
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.PackageManagerCompat.LOG_TAG
import com.google.api.Distribution.BucketOptions.Linear
import com.google.common.collect.ComparisonChain.start
import org.w3c.dom.Text
import java.io.IOException
import java.util.Vector

private var permissionToRecordAccepted = false
private const val REQUEST_RECORD_AUDIO_PERMISSION = 200

class AudioConfig (var mainActivity: Activity): AppCompatActivity() {
    private var permissions: Array<String> = arrayOf(Manifest.permission.RECORD_AUDIO)
    private var fileName: String = ""
    private var recorder: MediaRecorder? = null
    private var player: MediaPlayer? = null

    //Allows us to prompt the user to record audio
    fun CreateAudioDialog(){

        val ScrollViewLinearLayout = mainActivity.findViewById<LinearLayout>(R.id.BottomScrollViewLinearLayout)
        val dialogView =  LayoutInflater.from(mainActivity).inflate(R.layout.dialog_record_audio, null)
        ScrollViewLinearLayout.addView(dialogView)
        var EditTetRedordName = dialogView.findViewById<EditText>(R.id.TextRecording)
        var AudioName = EditTetRedordName.text.toString()

        var RecordBtn = dialogView.findViewById<ToggleButton>(R.id.RecordBtn)
        RecordBtn.setOnCheckedChangeListener { _, isChecked ->

            if (isChecked) {
                RecordBtn.foreground = mainActivity.getDrawable(R.drawable.pause_icon)
                StartRecording(AudioName) }
            else {
                RecordBtn.foreground = mainActivity.getDrawable(R.drawable.play_arrow)
                StopRecording()
                CreatePlayBackAudioBox(AudioName)
            }
        }
    }

    fun StartRecording(FileName:String) {
        ActivityCompat.requestPermissions(mainActivity, permissions, REQUEST_RECORD_AUDIO_PERMISSION)
        if(permissionToRecordAccepted == true){
        recorder = MediaRecorder(mainActivity).apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setOutputFile(FileName)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)

            try { prepare() }
            catch (e: IOException) { Toast.makeText(mainActivity, "Failed" + e.toString(), Toast.LENGTH_SHORT).show() }
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

    fun isMicroPhonePresent():Boolean{
        return mainActivity.packageManager.hasSystemFeature(PackageManager.FEATURE_MICROPHONE)
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
        var MainScrollBarLinearLayout = mainActivity.findViewById<LinearLayout>(R.id.BasementScrollLinearLayout) //creates an object we can use to access the linear layout

        var playBarsHolder = LinearLayout(mainActivity)
        playBarsHolder.orientation = LinearLayout.VERTICAL
        playBarsHolder.background =  mainActivity.getDrawable(R.color.darkGreyHeader)

        var PlayButtonHolderText = TextView(mainActivity)
        PlayButtonHolderText.text = "" //audio name
        PlayButtonHolderText.setTextColor(mainActivity.getColor(R.color.white))

        var playBars = LinearLayout(mainActivity)
        playBars.orientation = LinearLayout.HORIZONTAL //we set the linearlayout to a horizon view
        playBars.background =  mainActivity.getDrawable(R.color.GreyBg)

        var playAndPauseButton = ToggleButton(mainActivity)
        playAndPauseButton.foreground = mainActivity.getDrawable(R.drawable.play_arrow)
        playAndPauseButton.background = mainActivity.getDrawable(R.drawable.record_shape)

        var PressToPlayAudio = TextView(mainActivity)
        PressToPlayAudio.text = "Play audio"
        PressToPlayAudio.setTextColor(mainActivity.getColor(R.color.white))

        playAndPauseButton.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                playAndPauseButton.foreground = mainActivity.getDrawable(R.drawable.pause_icon)
                fileName = "${mainActivity.externalCacheDir?.absolutePath}/" + GivenfileName + ".3gp"
                startPlaying()
            } else {
                playAndPauseButton.foreground = mainActivity.getDrawable(R.drawable.play_arrow)
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
            } catch (e: IOException) { Toast.makeText(mainActivity, "To be implemented", Toast.LENGTH_SHORT).show() }
        }
    }

    private fun stopPlaying() {
        player?.release()
        player = null
    }

}