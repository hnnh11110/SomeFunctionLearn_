package com.app.func.view.progress

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.app.func.R


class CustomProgressBar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val mArcPaintBackground: Paint = Paint().apply {
        isDither = true
        style = Paint.Style.STROKE
        color = Color.CYAN
        strokeWidth = 20f
        isAntiAlias = true
        strokeCap = Paint.Cap.ROUND
    }
    private val mArcPaintPrimary: Paint = Paint().apply {
        isDither = true
        style = Paint.Style.STROKE
        color = Color.RED
        strokeWidth = 20f
        isAntiAlias = true
        strokeCap = Paint.Cap.ROUND
    }
    private val mTextBounds: Rect = Rect()
    private var mProgress = 0
    private val mTextPaint: Paint = Paint().apply {
        isAntiAlias = true
        textSize = 24f
        style = Paint.Style.FILL
        color = Color.BLACK
        strokeWidth = 20f
    }
    private var centerX = 0
    private var centerY = 0
    private var mWidthArcBG = resources.getDimensionPixelSize(R.dimen._20dp).toFloat()
    private var mWidthAcrPrimary = resources.getDimensionPixelSize(R.dimen._20dp).toFloat()
    private var mTextSizeProgress = resources.getDimensionPixelSize(R.dimen._16dp).toFloat()
    val rect = RectF()

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val viewWidthHeight =
            MeasureSpec.getSize(resources.getDimensionPixelSize(R.dimen._200dp))
        centerX = viewWidthHeight / 2
        centerY = viewWidthHeight / 2
        setMeasuredDimension(viewWidthHeight, viewWidthHeight)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        rect.set(0 + 10f, 0 + 10f, (width - 10).toFloat(), (height - 10).toFloat())
        drawBackgroundProgress(canvas)
        drawPrimaryProgress(canvas)
        setProgress(13)
        drawTextCentred(canvas)
    }

    private fun drawPrimaryProgress(canvas: Canvas?) {
        canvas?.drawArc(rect, 270f, -(360 * (mProgress / 100f)), false, mArcPaintPrimary)
    }

    private fun drawBackgroundProgress(canvas: Canvas?) {
        canvas?.drawArc(rect, 270f, 360f, false, mArcPaintBackground)
    }

    fun setProgress(progress: Int) {
        mProgress = progress
        invalidate()
        requestLayout()
    }

    private fun drawTextCentred(canvas: Canvas?) {
        val text = "$mProgress%"
        mTextPaint.getTextBounds(text, 0, text.length, mTextBounds)
        canvas?.drawText(
            text, centerX - mTextBounds.exactCenterX(), centerY - mTextBounds.exactCenterY(),
            mTextPaint
        )
    }
}