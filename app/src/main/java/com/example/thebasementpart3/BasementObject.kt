package com.example.thebasementpart3

import android.widget.TextView
import java.util.Vector

class BasementObject(var mainActivity:MainActivity) {
    //holds the compiled assets we use
    var BasementString = "" //holds the complete string we get from the textview
    var ThisBasementCompiled = BasementSection("", "")
    var vectorBasementObject = Vector<BasementSection>()
    init { if(BasementString != ""){ separateBasementHeaders()} }

    data class BasementSection(
        var BasementHeader: String,
        var basementText: String
    )

    //This text a block text and creates header and text sections
    //When the basement is encrypted it gets its headers text from here
     fun createBasementSections(basementTextView:String) {
        var charRecorder = "" // this is used to parse through the string and separate the header from the text
        val basementSections = BasementSection("", "") //Creates an Empty BasementSection
        var lastNewline : Boolean = false //we need to check if the last character was a newline. If it was we know that is the end of that basement section
        var increase = 0; //this variable is used to increase the iterator
        vectorBasementObject.removeAllElements()

        for (i in 0 until basementTextView.length){
            if(basementTextView.length <= (i+increase)){ break } //Skips every char that is not a letter
            charRecorder += basementTextView[i+ increase]//records the char for every loop

            if(basementTextView[i+ increase] == '\n'){
                if(basementSections.BasementHeader == "") { //checks if the header was cleared or npt
                    basementSections.BasementHeader += charRecorder //Sets the Header for BasementSection
                    charRecorder = "" //Resets the Character Recorder
                    lastNewline = true
                }
                else if(lastNewline) {
                    basementSections.basementText += charRecorder //adds a line of text to the text section of the Section
                    while (basementTextView[i+increase] == '\n') {
                        if(increase != 0){charRecorder += basementTextView[i+ increase]}
                        increase += 1
                        if((i+increase) >= basementTextView.length){ //if we get to the end of the text or we get a character that is not a newline
                            basementSections.basementText = charRecorder
                            vectorBasementObject.add(BasementSection(basementSections.BasementHeader, basementSections.basementText))
                            charRecorder = ""//Resets the Character Recorder
                            basementSections.basementText = "" //clear the basement section text we are using to hold the new basement sections
                            basementSections.BasementHeader = ""
                            break
                        }
                    }
                    if(basementSections.BasementHeader != "" && basementSections.basementText != ""){
                        basementSections.basementText = charRecorder
                        vectorBasementObject.add(BasementSection(basementSections.BasementHeader, basementSections.basementText))
                        basementSections.basementText = "" //clear the basement section text we are using to hold the new basement sections
                        basementSections.BasementHeader = ""
                        charRecorder = ""
                        increase -= 1
                    }
                    lastNewline = false
                }
                else if (basementTextView[i+ increase] == '\n'){lastNewline = true}
            }
            else { lastNewline = false }
        }
        if(basementSections.BasementHeader == ""){ basementSections.BasementHeader += charRecorder }
        else if(basementSections.basementText == ""){ basementSections.basementText += charRecorder }
        vectorBasementObject.add(BasementSection(basementSections.BasementHeader, basementSections.basementText))
    }

    fun separateBasementHeaders() {
        var basementHeader = ""
        var basementText = ""

        for (Section in vectorBasementObject) {
            basementHeader += Section.BasementHeader + "<basementSeparatorHeader>"
            basementText += Section.basementText + "<basementSeparatorText>"
        }
        BasementString = basementHeader + basementText
        ThisBasementCompiled.BasementHeader = basementHeader
        ThisBasementCompiled.basementText = basementText
    }

    //Gets the text from the basement and separates it into the a vector of basements
    fun GetBasementVectorsWithoutDelimiter(CombinedHeaderAndText: BasementSection): Vector<BasementSection> {
        val separatedBasement = Vector<BasementSection>()

        if (CombinedHeaderAndText.BasementHeader == "<basementSeparatorHeader>" || CombinedHeaderAndText.BasementHeader == ""){
            separatedBasement.add(BasementSection("", ""))//We know there is no data in this file
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
                vectorBasementObject = separatedBasement
            }
        }
        return vectorBasementObject
    }

    fun setTextBox(){ //separates the strings, String pair into a vector of String, String pairs
        var compiledString = String()
        for (iterator in vectorBasementObject){
            compiledString += iterator.BasementHeader + iterator.basementText
        }
        BasementString = compiledString
        mainActivity.findViewById<TextView>(R.id.addTextTxtView).text = BasementString
    }

    fun eraseBasement(){
        BasementString = ""
        ThisBasementCompiled.BasementHeader = ""
        ThisBasementCompiled.basementText =""
        vectorBasementObject.removeAllElements()
        mainActivity.findViewById<TextView>(R.id.addTextTxtView).text = BasementString
    }
}
