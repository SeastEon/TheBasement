package com.example.thebasementpart3

import android.app.Activity
import android.graphics.Typeface
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout
import android.widget.Space
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import java.util.Vector

class BasementObject( private var mainContext:Activity) {
    var BasementString = ""
    var mHeaders =""
    var mText =""
    var headerNav = NavigateHeader(mainContext)
    val textAdderTextView =  mainContext.findViewById<TextView>(R.id.addTextTxtView)

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

    private fun createBasementSections(basementTextView:String): Vector<BasementSection> {
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

    private fun separateBasementHeaders(basementSections: Vector<BasementSection>): BasementSection {
        var basementHeader = ""
        var basementText = ""
        for (Section in basementSections) {
            basementHeader = Section.BasementHeader + "<basementSeparator>"
            basementText = Section.basementText + "<basementSeparator>"
        }
        basementHeader.drop(basementHeader.length) //removes the last ","
        basementText.drop(basementText.length)

        return BasementSection(basementHeader, basementText)
    }

    fun setBasementText(CombinedHeaderAndText: BasementSection): Vector<BasementSection> {
        val separatedBasement = Vector<BasementSection>()

        if (CombinedHeaderAndText.BasementHeader == "<basementSeparator>" || CombinedHeaderAndText.BasementHeader == ""){
            //We know there is no data in this file
            separatedBasement.add(BasementSection("", ""))
        } else if(CombinedHeaderAndText.basementText == "<basementSeparator>" ||  CombinedHeaderAndText.basementText == ""){
            // we know there is only a header in this file so we can just add the that element to the vector
            separatedBasement.add(BasementSection(CombinedHeaderAndText.BasementHeader.substringBefore("<basementSeparator>"), ""))
        } else {
            var endText = false; var endHeader = false //these booleans tell us when the

            while (!endHeader && !endText) { //these variables are used to decide where we need to delete the recorded text
                val headerHolder = CombinedHeaderAndText.BasementHeader.substringBefore("<basementSeparator>")
                val textHolder = CombinedHeaderAndText.basementText.substringBefore("<basementSeparator>")

                if (!endHeader) {
                    val headerIteratorVal = headerHolder.length + "<basementSeparator>".length
                    CombinedHeaderAndText.BasementHeader = CombinedHeaderAndText.BasementHeader.removeRange(0, headerIteratorVal)
                }

                if (!endText) {
                    val textIteratorVal = textHolder.length + "<basementSeparator>".length
                    CombinedHeaderAndText.basementText =
                        CombinedHeaderAndText.basementText.removeRange(0, textIteratorVal)
                }
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

    fun createTextBoxes(TextType:String) {
        val basementMainLinearLayout =  mainContext.findViewById<LinearLayout>(R.id.BasementScrollLinearLayout)
        val newBasementTextView = TextView(mainContext)

        if(TextType == "Text") {
            newBasementTextView.textSize = 20f
            newBasementTextView.setTextColor(mainContext.getColor(R.color.white))
            newBasementTextView.text = textAdderTextView.text
            newBasementTextView.background = AppCompatResources.getDrawable(mainContext, R.drawable.light_grey_text_adder)
            newBasementTextView.background.alpha = 40
        }  else if (TextType == "Header"){
            newBasementTextView.textSize = 25f
            newBasementTextView.setTypeface(null, Typeface.BOLD)
            newBasementTextView.setTextColor(mainContext.getColor(R.color.lightPurple))
            newBasementTextView.text = textAdderTextView.text.toString()
        }

        newBasementTextView.minimumWidth = WRAP_CONTENT

        val spacer = Space(mainContext)
        spacer.minimumHeight = 10

        basementMainLinearLayout.addView(spacer)
        basementMainLinearLayout.addView(newBasementTextView)

        textAdderTextView.text = "" //clears the user's input section
    }
}
