package com.app.func.view.compass_views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin


class CompassView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var size = 0
    private val path = Path()
    private val baseAngle = 2f * Math.PI / 8

    private val paintBlackCircleBiggest = Paint().apply {
        isAntiAlias = true
        color = Color.BLACK
        style = Paint.Style.FILL
    }
    private val paintBlueCircle = Paint().apply {
        isAntiAlias = true
        color = Color.BLUE
        style = Paint.Style.FILL
    }
    private val paintWhiteCircle = Paint().apply {
        isAntiAlias = true
        color = Color.WHITE
        style = Paint.Style.FILL
    }

    private val paintBlackCircle = Paint().apply {
        isAntiAlias = true
        color = Color.BLACK
        style = Paint.Style.FILL
    }
    private val paintRedCircle = Paint().apply {
        isAntiAlias = true
        color = Color.RED
        style = Paint.Style.FILL
    }

    private val paintYellowCircle = Paint().apply {
        isAntiAlias = true
        color = Color.YELLOW
        style = Paint.Style.FILL
    }
    private val paintBlackLine = Paint().apply {
        isAntiAlias = true
        color = Color.BLACK
        style = Paint.Style.FILL
        strokeWidth = 8f
    }
    private val paintBlackArrow = Paint().apply {
        isAntiAlias = true
        color = Color.BLACK
        style = Paint.Style.FILL
    }

    private val paintText = Paint().apply {
        isAntiAlias = true
        color = Color.BLACK
        style = Paint.Style.STROKE
        textAlign = Paint.Align.CENTER
        textSize = 106f
    }

    private fun initView() {

    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        initView()
        drawCircleHeartBlackBiggest(canvas)
        drawCircleHeartYellow(canvas)
        drawCircleHeartRed(canvas)
        drawCircleHeartBlack(canvas)
        drawCircleHeartWhite(canvas)
        drawCircleHeartBlue(canvas)
        drawLineOfArrow(canvas)
        drawText(canvas)
        invalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        //Replace size = measuredWidth, measuredHeight
        size = min(measuredWidth, measuredHeight)
        setMeasuredDimension(size, size)
    }

    private fun drawCircleHeartBlue(canvas: Canvas?) {
        canvas?.drawCircle(size / 2f, size / 2f, size / 20f, paintBlueCircle)
    }

    private fun drawCircleHeartWhite(canvas: Canvas?) {
        canvas?.drawCircle(size / 2f, size / 2f, size / 18f, paintWhiteCircle)
    }

    private fun drawCircleHeartBlack(canvas: Canvas?) {
        canvas?.drawCircle(size / 2f, size / 2f, size / 16f, paintBlackCircle)
    }

    private fun drawCircleHeartRed(canvas: Canvas?) {
        canvas?.drawCircle(size / 2f, size / 2f, size / 2.8f, paintRedCircle)
    }

    private fun drawCircleHeartYellow(canvas: Canvas?) {
        canvas?.drawCircle(size / 2f, size / 2f, size / 2.2f, paintYellowCircle)
    }

    private fun drawCircleHeartBlackBiggest(canvas: Canvas?) {
        canvas?.drawCircle(size / 2f, size / 2f, size / 2.15f, paintBlackCircleBiggest)
    }

    private fun drawLineOfArrow(canvas: Canvas?) {
        for (i in 0..7) {
            val startX = (size / 2 - sin(baseAngle * i) * size / 16f).toFloat()
            val startY = (size / 2 - cos(baseAngle * i) * size / 16f).toFloat()
            val stopX = (size / 2 - sin(baseAngle * i) * size / 3.2f).toFloat()
            val stopY = (size / 2 - cos(baseAngle * i) * size / 3.2f).toFloat()
            canvas?.drawLine(startX, startY, stopX, stopY, paintBlackLine)

            val arrowStopX = (size / 2 - sin(baseAngle * i) * size / 2.8f).toFloat()
            val arrowStopY = (size / 2 - cos(baseAngle * i) * size / 2.8f).toFloat()
            val widthTriangle = size / 4 - size / 5
            drawArrowOfLine(canvas, arrowStopX, arrowStopY, widthTriangle.toFloat())
        }
    }

    private fun drawArrowOfLine(canvas: Canvas?, x: Float, y: Float, width: Float) {
        path.reset()
        path.moveTo(x, y) // Top
        path.lineTo(
            x - (sin(Math.PI / 6) * width).toFloat(),
            y - (cos(Math.PI / 6) * width).toFloat()
        ) // Bottom left
        path.lineTo(
            x + (sin(Math.PI / 6) * width).toFloat(),
            y - (cos(Math.PI / 6) * width).toFloat()
        ) // Bottom right
        path.lineTo(x, y) // Back to Top
        path.close()
        canvas?.drawPath(path, paintBlackArrow)
    }

    private fun drawText(canvas: Canvas?) {
        val stopX = (size / 2 - sin(baseAngle * 0) * size / 3.2f).toFloat()
        val stopY = (size / 2 - cos(baseAngle * 0) * size / 3.2f).toFloat()
        canvas?.drawText("a", stopX, stopY, paintText)
    }
}