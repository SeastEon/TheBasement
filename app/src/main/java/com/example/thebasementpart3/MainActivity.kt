package com.example.thebasementpart3

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


class MainActivity : AppCompatActivity() {
    private val db = DataBase(this) //the database is initialized using the main context to display successes or failures
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val exportBtn = findViewById<Button>(R.id.ExportTodataBase)
        exportBtn.setOnClickListener {
            val baseMTObj = BasementObject(findViewById<Button>(R.id.TxtVMainBasememnt))// we create our basement Object
            db.addBasementToDatabase(baseMTObj)
        }

        val openRecordDialog = findViewById<Button>(R.id.OpenRecordAudioDialogBtn)
        openRecordDialog.setOnClickListener{ AudioConfig(this).CreateAudioDialog() }

        //Setting up recalling the database for each user
        val PREFS_NAME = "MyPrefsFile"
        val settings = getSharedPreferences(PREFS_NAME, 0)
        if (settings.getBoolean("my_first_time", true)) {
            //the app is being launched for first time, do something
            Log.d("Comments", "First time")

            // record the fact that the app has been started at least once
            settings.edit().putBoolean("my_first_time", false).commit()
        }
    }
}