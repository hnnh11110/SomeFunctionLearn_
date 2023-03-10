package com.app.func.textview_view

import android.graphics.Paint
import android.graphics.Typeface
import android.text.TextPaint
import android.text.style.TypefaceSpan

class CustomTypefaceSpan(family: String, typeface: Typeface) : TypefaceSpan(family) {

    private val newType: Typeface? = null

    private fun applyCustomTypeFace(paint: Paint, tf: Typeface) {
        val oldStyle: Int
        val oldTypeface = paint.typeface
        oldStyle = oldTypeface?.style ?: 0
        //int fake = oldStyle & ~tf.getStyle();
        val fake = oldStyle and tf.style.inv()
        if (fake and Typeface.BOLD != 0) {
            paint.isFakeBoldText = true
        }
        if (fake and Typeface.ITALIC != 0) {
            paint.textSkewX = -0.25f
        }
        paint.typeface = tf
    }

    override fun updateDrawState(ds: TextPaint) {
        super.updateDrawState(ds)
        newType?.let { applyCustomTypeFace(ds, it) }

    }

    override fun updateMeasureState(paint: TextPaint) {
        super.updateMeasureState(paint)
        newType?.let { applyCustomTypeFace(paint, it) }

    }

}