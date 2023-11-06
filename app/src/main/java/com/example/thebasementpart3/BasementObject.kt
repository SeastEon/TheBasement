package com.example.thebasementpart3

import android.app.Activity
import android.graphics.Typeface
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout
import android.widget.Space
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import java.util.Vector

class BasementObject(var mainContext:Activity, var headerNav:NavigateHeader) {
    var BasementString = ""
    var mHeaders =""
    var mText =""

    init {//constructs the variables for the Basement
        val basementSections = separateBasementHeaders(createBasementSections(BasementString))
        mHeaders = basementSections.BasementHeader
        mText = basementSections.basementText
    }

    //Holds the Headers and Text for the Basement file

    data class BasementSection(
        var BasementHeader: String,
        var basementText: String
    )

    //This text a block text and creates header and text sections
    //When the basement is encypted it gets its headers text from here
     fun createBasementSections(basementTextView:String): Vector<BasementSection> {
        val basementText = basementTextView
        val basementSectionVector = Vector<BasementSection>()
        val basementTextVector = ""
        var charRecorder = ""
        val basementSections = BasementSection("", basementTextVector) //Creates an Empty BasementSection

        for (a in basementText){
            charRecorder += a //records the char for every loop
            if(a == '\n'){
                if(basementSections.BasementHeader == "") {
                    basementSections.BasementHeader += charRecorder //Sets the Header for BasementSection
                    charRecorder = "" //Resets the Character Recorder
                }

                if(a.inc() == '\n') {
                    basementSections.basementText += charRecorder //adds a line of text to the text section of the Section
                    charRecorder = ""//Resets the Character Recorder
                    basementSectionVector.add(basementSections)
                    basementSections.basementText = ""
                    basementSections.BasementHeader = ""
                    while (!a.isLetter()) { a.inc(); }//Skips every char that is not a letter
                }
            }
        }
        if(basementSections.BasementHeader == ""){
            basementSections.BasementHeader += charRecorder
        } else if(basementSections.basementText == ""){
            basementSections.basementText += charRecorder
        }
        basementSectionVector.add(basementSections)
        headerNav.basementSections = basementSectionVector
        return headerNav.basementSections
    }

    fun separateBasementHeaders(basementSections: Vector<BasementSection>): BasementSection {
        var basementHeader = ""
        var basementText = ""
        for (Section in basementSections) {
            basementHeader = Section.BasementHeader + "<basementSeparatorHeader>"
            basementText = Section.basementText + "<basementSeparatorText>"
        }
        basementHeader.drop(basementHeader.length) //removes the last ","
        basementText.drop(basementText.length)
        BasementString = basementHeader + basementText
        this.mHeaders = basementHeader
        this.mText = basementText
        return BasementSection(basementHeader, basementText)
    }

    //Gets the text from the basement and separates it into the a vector of basements
    fun setBasementText(CombinedHeaderAndText: BasementSection): Vector<BasementSection> {
        val separatedBasement = Vector<BasementSection>()

        if (CombinedHeaderAndText.BasementHeader == "<basementSeparatorHeader>" || CombinedHeaderAndText.BasementHeader == ""){
            //We know there is no data in this file
            separatedBasement.add(BasementSection("", ""))
        } else if(CombinedHeaderAndText.basementText == "<basementSeparatorText>" ||  CombinedHeaderAndText.basementText == ""){
            // we know there is only a header in this file so we can just add the that element to the vector
            separatedBasement.add(BasementSection(CombinedHeaderAndText.BasementHeader.substringBefore("<basementSeparatorHeader>"), ""))
        } else {
            var endText = false; var endHeader = false //these booleans tell us when the

            while (!endHeader && !endText) { //these variables are used to decide where we need to delete the recorded text
                val headerHolder = CombinedHeaderAndText.BasementHeader.substringBefore("<basementSeparatorHeader>")
                val textHolder = CombinedHeaderAndText.basementText.substringBefore("<basementSeparatorText>")

                val headerIteratorVal = headerHolder.length + "<basementSeparatorHeader>".length
                CombinedHeaderAndText.BasementHeader = CombinedHeaderAndText.BasementHeader.removeRange(0, headerIteratorVal)
                val textIteratorVal = textHolder.length + "<basementSeparatorText>".length
                CombinedHeaderAndText.basementText = CombinedHeaderAndText.basementText.removeRange(0, textIteratorVal)

                if (CombinedHeaderAndText.BasementHeader == "") { endHeader = true }
                if (CombinedHeaderAndText.basementText == "") { endText = true }

                separatedBasement.add(BasementSection(headerHolder, textHolder))
            }
        }
        return separatedBasement
    }

    fun setTextBox(TextFromBasement:Vector<BasementSection>){
        //separates the strings, String pair into a vector of String, String pairs
        val headerTextPair = Vector<String>()
        var compiledString = String()
        for (iterator in TextFromBasement){headerTextPair.add(iterator.BasementHeader + iterator.basementText)}
        for (iterator in headerTextPair){compiledString += iterator}
        if(compiledString == ""){compiledString += "Enter Text here"}
        BasementString = compiledString
    }

    fun eraseBasement():BasementObject{
        BasementString = ""
        mHeaders = ""
        mText =""
        return this
    }
}
