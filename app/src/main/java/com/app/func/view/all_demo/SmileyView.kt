package com.app.func.view.all_demo

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import kotlin.math.min

class SmileyView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var mCirclePaint: Paint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        color = Color.YELLOW
    }
    private var eyePaint: Paint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.STROKE
        strokeWidth = 10 * resources.displayMetrics.density
        strokeCap = Paint.Cap.ROUND
        color = Color.RED
    }
    private var mouthPaint: Paint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.STROKE
        strokeWidth = 16 * resources.displayMetrics.density
        strokeCap = Paint.Cap.ROUND
        color = Color.RED
    }

    private var mCenterX = 0f
    private var mCenterY = 0f
    private var mRadius = 0f
    private val mArcBounds = RectF()

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val w = MeasureSpec.getSize(widthMeasureSpec)
        val h = MeasureSpec.getSize(heightMeasureSpec)
        //val size = w.coerceAtMost(h)
        val size = min(w, h)
        setMeasuredDimension(size, size)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mCenterX = w / 2f
        mCenterY = h / 2f
        mRadius = min(w, h) / 2f
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        // draw face
        canvas?.drawCircle(mCenterX, mCenterY, mRadius, mCirclePaint)

        // draw eyes
        val eyeRadius = mRadius / 5f
        val eyeOffsetX = mRadius / 3f
        val eyeOffsetY = mRadius / 3f
        canvas?.drawCircle(mCenterX - eyeOffsetX, mCenterY - eyeOffsetY, eyeRadius, eyePaint)
        canvas?.drawCircle(mCenterX + eyeOffsetX, mCenterY - eyeOffsetY, eyeRadius, eyePaint)

        // draw mouth
        val mouthInset = mRadius / 3f
        mArcBounds[mouthInset, mouthInset, mRadius * 2 - mouthInset] = mRadius * 2 - mouthInset
        canvas?.drawArc(mArcBounds, 45f, 90f, false, mouthPaint)
    }
}