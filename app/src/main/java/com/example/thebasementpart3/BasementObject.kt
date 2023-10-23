package com.example.thebasementpart3

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import org.w3c.dom.Text
import java.util.UUID
import java.util.Vector

class BasementObject(val basementTextView: TextView, var maincontext:Activity) {
    var mHeaders =""
    var mText =""
    var HeaderNav = NavigateHeader(maincontext)

    init {//constructs the varibles for the Basement
        var BasementSections = SeparatebasementHeaders(CreateBasementSections(basementTextView))
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
                }

                if(a.inc() == '\n') {
                    BasementSections.basementText += CharRecorder //adds a line of text to the text section of the Section
                    CharRecorder = "";//Resets the Character Recorder
                    BasementSectionVector.add(BasementSections);
                    BasementSections.basementText = ""
                    BasementSections.BasementHeader = ""
                    while (!a.isLetter()) { a.inc(); }//Skips every char that is not a letter
                }
            }
        }
        if(BasementSections.BasementHeader == ""){
            BasementSections.BasementHeader += CharRecorder;
        } else if(BasementSections.basementText == ""){
            BasementSections.basementText += CharRecorder;
        }
        BasementSectionVector.add(BasementSections)
        HeaderNav.BasementSections = BasementSectionVector
        return HeaderNav.BasementSections
    }

    fun SeparatebasementHeaders(basementSections: Vector<BasementSection>): BasementSection {
        var BasementHeader = ""
        var BasementText = ""
        for (Section in basementSections){
            BasementHeader = Section.BasementHeader + "<basementSeparator>";
            BasementText= Section.basementText + "<basementSeparator>";
        }
        BasementHeader.drop(BasementHeader.length) //removes the last ","
        BasementText.drop(BasementText.length)
        var BasementSections = BasementSection(BasementHeader, BasementText);

        return BasementSections;
    }

    fun SetBasementText(CombinedHeaderAndtext: BasementSection): Vector<BasementSection> {
        var Separatedbasment = Vector<BasementSection>()

        if (CombinedHeaderAndtext.BasementHeader == "<basementSeparator>" || CombinedHeaderAndtext.BasementHeader == ""){
            //We know there is no data in this file
            Separatedbasment.add(BasementSection("", ""))
        } else if(CombinedHeaderAndtext.basementText == "<basementSeparator>" ||  CombinedHeaderAndtext.basementText == ""){
            // we know there is only a header in this file so we can just add the that element to the vector
            Separatedbasment.add(BasementSection(CombinedHeaderAndtext.BasementHeader.substringBefore("<basementSeparator>"), ""))
        } else {
            var EndText = false; var EndHeader = false //these booleans tell us when the

            while (EndHeader != true && EndText != true) {

                //these variblies are used to decide where we need to delete the recorded text
                var HeaderHolder =
                    CombinedHeaderAndtext.BasementHeader.substringBefore("<basementSeparator>")
                var TextHolder =
                    CombinedHeaderAndtext.basementText.substringBefore("<basementSeparator>")

                if (!EndHeader) {
                    var HeaderIteratorVal = HeaderHolder.length + "<basementSeparator>".length
                    CombinedHeaderAndtext.BasementHeader = CombinedHeaderAndtext.BasementHeader.removeRange(0, HeaderIteratorVal)
                }

                if (!EndText) {
                    var TextIteratorVal = TextHolder.length + "<basementSeparator>".length
                    CombinedHeaderAndtext.basementText =
                        CombinedHeaderAndtext.basementText.removeRange(0, TextIteratorVal)
                }

                if (CombinedHeaderAndtext.BasementHeader == "") {
                    EndHeader = true
                }
                if (CombinedHeaderAndtext.basementText == "") {
                    EndText = true
                }
                Separatedbasment.add(BasementSection(HeaderHolder, TextHolder))
            }
        }
        return Separatedbasment
    }

    fun SetTextBox(TextFrombasement:Vector<BasementSection>){
        //separates the strings, String pair into a vector of String, String pairs
        var HeaderTextPair = Vector<String>()
        var CompiledString = String()

        for (iterator in TextFrombasement)
            HeaderTextPair.add(iterator.BasementHeader + iterator.basementText)

        for (iter in HeaderTextPair)
            CompiledString += iter

        if(CompiledString == ""){CompiledString += "Enter Text here"}

        basementTextView.setText(CompiledString)
    }

    fun EraseBasement():BasementObject{
        basementTextView.text = ""
        mHeaders = ""
        mText =""
        return this
    }

    fun TextAdded(){
        var TextAddertextView =  maincontext.findViewById<TextView>(R.id.addTextTxtView)
        basementTextView.text = basementTextView.text.toString() + '\n' + TextAddertextView.text .toString()
        TextAddertextView.text = ""
    }
}
