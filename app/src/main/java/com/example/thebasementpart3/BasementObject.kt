package com.example.thebasementpart3

import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import java.util.UUID
import java.util.Vector

class BasementObject(basementTextView: TextView) {
    var mBasementId = ""
    var mHeaders =""
    var mText =""

    init {//constructs the varibles for the Basement
        var BasementSections = SeparatebasementHeaders(CreateBasementSections(basementTextView))
        mBasementId = UUID.randomUUID().toString()
        mHeaders = BasementSections.BasementHeader
        mText = BasementSections.basementText
    }

    //Holds the Headers and Text for the Basement file
    data class BasementSection(
        var BasementHeader: String,
        var basementText: String
    )

    fun CreateBasementSections(basementtextView:TextView): Vector<BasementSection> {
        val BasementText = basementtextView
        var BasementSectionVector = Vector<BasementSection>()
        var BasementTextVector = ""
        var CharRecorder = ""
        var BasementSections = BasementSection("", BasementTextVector); //Creates an Empty BasementSection

        for (a in BasementText.text){
            CharRecorder += a //records the char for every loop
            if(a == '\n'){
                if(BasementSections.BasementHeader == "") {
                    BasementSections.BasementHeader += CharRecorder //Sets the Header for BasementSection
                    CharRecorder = ""; //Resets the Character Recorder
                }else if(a.inc() == '\n'){
                    BasementSections.basementText += CharRecorder //adds a line of text to the text section of the Section
                    CharRecorder = "";//Resets the Character Recorder
                    BasementSectionVector.add(BasementSections);
                    BasementSections.BasementHeader = ""
                    BasementSections.basementText = ""
                    while (!a.isLetter()) {a.inc(); }//Skips every char that is not a letter
                }
            }
        }
        if(BasementSections.BasementHeader == ""){
            BasementSections.BasementHeader += CharRecorder;
        } else if(BasementSections.basementText == ""){
            BasementSections.basementText += CharRecorder;
        }
        BasementSectionVector.add(BasementSections)
        return BasementSectionVector
    }

    fun SeparatebasementHeaders(basementSections: Vector<BasementSection>): BasementSection {
        var BasementHeader = ""
        var BasementText = ""
        for (Section in basementSections){
            BasementHeader = Section.BasementHeader + ",";
            BasementText= Section.basementText + ",";
        }
        BasementHeader.drop(BasementHeader.length) //removes the last ","
        BasementText.drop(BasementText.length)
        var BasementSections = BasementSection(BasementHeader, BasementText);

        return BasementSections;
    }
}
