package com.app.func.view

import android.text.TextPaint
import android.text.style.SuperscriptSpan

class TopAlignSuperscriptSpan(shiftPercentage: Float) : SuperscriptSpan() {

    //divide superscript by this number
    private var fontScale = 2

    //shift value, 0 to 1.0
    private var shiftPercentage = 0f

    //sets the shift percentage
    init {
        if (shiftPercentage > 0.0 && shiftPercentage < 1.0) this.shiftPercentage = shiftPercentage
    }

    override fun updateDrawState(tp: TextPaint) {
        //original ascent
        val ascent = tp.ascent()
        //scale down the font
        tp.textSize = tp.textSize / fontScale
        //get the new font ascent
        val newAscent = tp.fontMetrics.ascent
        //move baseline to top of old font, then move down size of new font
        //adjust for errors with shift percentage
        tp.baselineShift += (ascent - ascent * shiftPercentage
                - (newAscent - newAscent * shiftPercentage)).toInt()
    }

    override fun updateMeasureState(tp: TextPaint) {
        updateDrawState(tp)
    }
}