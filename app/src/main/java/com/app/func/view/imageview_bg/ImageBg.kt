package com.app.func.view.imageview_bg

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View


class ImageBg @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {


    private var mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mPath = Path()
    private val mRectF = RectF(0f, 0f, 200f, 200f)


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
//        drawRectCorner(canvas)
        canvas?.drawRoundRectPath(
            mRectF, 40f, true, true,
            true, true, mPaint
        )
    }

    private fun Canvas.drawRoundRectPath(
        rectF: RectF,
        radius: Float,
        roundTopLeft: Boolean,
        roundTopRight: Boolean,
        roundBottomLeft: Boolean,
        roundBottomRight: Boolean,
        paint: Paint
    ) {
        val path = Path()

        //Move path cursor to start point
        if (roundBottomLeft) {
            path.moveTo(rectF.left, rectF.bottom - radius)
        } else {
            path.moveTo(rectF.left, rectF.bottom)
        }

        // drawing line and rounding top left curve
        if (roundTopLeft) {
            path.lineTo(rectF.left, rectF.top + radius)
            path.quadTo(rectF.left, rectF.top, rectF.left + radius, rectF.top)
        } else {
            path.lineTo(rectF.left, rectF.top)
        }

        // drawing line an rounding top right curve
        if (roundTopRight) {
            path.lineTo(rectF.right - radius, rectF.top)
            path.quadTo(rectF.right, rectF.top, rectF.right, rectF.top + radius)
        } else {
            path.lineTo(rectF.right, rectF.top)
        }

        // drawing line an rounding bottom right curve
        if (roundBottomRight) {
            path.lineTo(rectF.right, rectF.bottom - radius)
            path.quadTo(rectF.right, rectF.bottom, rectF.right - radius, rectF.bottom)
        } else {
            path.lineTo(rectF.right, rectF.bottom)
        }

        // drawing line an rounding bottom left curve
        if (roundBottomLeft) {
            path.lineTo(rectF.left + radius, rectF.bottom)
            path.quadTo(rectF.left, rectF.bottom, rectF.left, rectF.bottom - radius)
        } else {
            path.lineTo(rectF.left, rectF.bottom)
        }

        path.close()
        drawPath(path, paint)
    }

    private fun drawRectCorner(canvas: Canvas?) {
        mPaint.color = Color.RED
        /*
        // Top, left in px
       // Top, right in px
        // Bottom, right in px
         // Bottom,left in px
         */
        val corners = floatArrayOf(15f, 15f, 15f, 15f, 15f, 15f, 15f, 15f)
        mPath.addRoundRect(mRectF, corners, Path.Direction.CW)
        canvas?.drawPath(mPath, mPaint)
    }
}