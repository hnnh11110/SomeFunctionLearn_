package com.app.func.textview_view

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.util.Log
import androidx.appcompat.widget.AppCompatTextView

class NoneSpaceTextView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
    AppCompatTextView(context, attrs) {

    override fun onDraw(canvas: Canvas?) {
        Log.d(
            "NoneSpaceTextView",
            "\nfont bottom:" + paint.fontMetricsInt.bottom + "  \ndescent:" + paint.fontMetricsInt.descent + " \nascent:" + paint.fontMetricsInt.ascent + " \ntop:" + paint.fontMetricsInt.top + " \nbaseline:" + baseline
        )
        //TextView Component properties
        Log.d(
            "NoneSpaceTextView",
            "Textview---bottom:$bottom   top:$top   baseline:$baseline"
        )
        /*
          int topDelta = (getPaint().getFontMetricsInt().ascent - getPaint().getFontMetricsInt().top);
       setTop(-topDelta);
         */
        val topDelta = paint.fontMetricsInt.ascent - paint.fontMetricsInt.top
        val bottomDelta = paint.fontMetricsInt.descent - paint.fontMetricsInt.bottom
        Log.d(
            "NoneSpaceTextView",
            "Textview---topDelta:$topDelta  bottomDelta:$bottomDelta"
        )
        top = -topDelta
        bottom = bottomDelta
        super.onDraw(canvas)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)

        /* int deltaHeight = (getPaint().getFontMetricsInt().ascent - getPaint().getFontMetricsInt().top + getPaint().getFontMetricsInt().bottom - getPaint().getFontMetricsInt().descent);
         setMeasuredDimension(getMeasuredWidth(), getMeasuredHeight() - deltaHeight);*/

        if (heightMode == MeasureSpec.AT_MOST) {
            val deltaHeight =
                paint.fontMetricsInt.ascent - paint.fontMetricsInt.top + paint.fontMetricsInt.bottom - paint.fontMetricsInt.descent
            setMeasuredDimension(measuredWidth, measuredHeight - deltaHeight)
        }
    }
}