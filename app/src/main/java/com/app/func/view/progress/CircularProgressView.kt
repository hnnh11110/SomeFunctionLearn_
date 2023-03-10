package com.app.func.view.progress

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import com.app.func.R
import kotlin.math.cos
import kotlin.math.sin

class CircularProgressView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val ovalSpace = RectF()
    private val parentArcColor = Color.parseColor("#DFE6F3")
    private val fillArcColor = Color.parseColor("#0097A7")
    private val startAngle = 150f
    private val sweepAngle = 250f
    private val minValue = 40f
    private val maxValue = 75f
    private val paddingValue = 30f
    private var radiusView = 0f
    private val ovalSize = resources.getDimensionPixelSize(R.dimen._200dp)
    private val widthSize = resources.getDimensionPixelSize(R.dimen._40dp)
    private val parentArcPaint = Paint().apply {
        style = Paint.Style.STROKE
        isAntiAlias = true
        color = parentArcColor
        strokeWidth = 40f
        strokeCap = Paint.Cap.ROUND
    }

    private val fillArcPaint = Paint().apply {
        style = Paint.Style.STROKE
        isAntiAlias = true
        color = fillArcColor
        strokeWidth = 40f
        strokeCap = Paint.Cap.ROUND
    }

    private val paintDots = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        isAntiAlias = true
        color = Color.parseColor("#90ee02")
        strokeWidth = 40f
        strokeCap = Paint.Cap.ROUND
    }

    private var textPaint: Paint = Paint().apply {
        color = Color.BLACK
        textSize = 30f
        isAntiAlias = true
    }


    private fun createBackGroundCircle() {
        val horizontalCenter = width.div(2f)
        val verticalCenter = height.div(2f)
        val ovalSize = 200f
        /* ovalSpace.set(
             horizontalCenter - ovalSize,
             verticalCenter - ovalSize,
             horizontalCenter + ovalSize,
             verticalCenter + ovalSize
         )*/

        radiusView = width.div(2f) - paddingValue
        ovalSpace.set(
            horizontalCenter - radiusView,
            verticalCenter - radiusView,
            horizontalCenter + radiusView,
            verticalCenter + radiusView
        )
    }

    private fun drawBackground(canvas: Canvas?) {
        canvas?.drawArc(ovalSpace, startAngle, sweepAngle, false, parentArcPaint)

    }

    private fun drawInnerArc(canvas: Canvas?, multiple: Int) {
//        val percentageToFill = 10.8f
        val percentageToFill = sweepAngle / (maxValue - minValue)
        canvas?.drawArc(ovalSpace, startAngle, percentageToFill * multiple, false, fillArcPaint)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        createBackGroundCircle()
        drawBackground(canvas)
        drawInnerArc(canvas, 2)
        drawCircleMark(canvas)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        //radiusView = width.div(2) - paddingValue
    }

    private fun drawCircleMark(canvas: Canvas?) {
        val cx = (cos(0f) * radiusView)
        val cy = (sin(0f) * radiusView)
        val textR = (measuredWidth / 2 - 50).toFloat() // radius of circle formed by text
//        val textR = (width / 2 - 50).toFloat() // radius of circle formed by text
        val textArray = arrayOf("12", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11")
        val cxx = (measuredWidth / 2 + (measuredWidth / 2) * sin(Math.PI / 6 * 1)).toFloat()
        val cyy = (measuredHeight / 2 - (measuredWidth / 2) * cos(Math.PI / 6 * 1)).toFloat()

        canvas?.drawCircle(cxx, cyy + 30f, 10f, paintDots)
        canvas?.drawCircle(cx, cy, 10f, fillArcPaint)


        for (i in 0..11) {
            val startX = (measuredWidth / 2 + textR * sin(Math.PI / 6 * i)
                    - textPaint.measureText(textArray[i]) / 2).toFloat()
            val startY = (measuredHeight / 2 - textR * cos(Math.PI / 6 * i)
                    + textPaint.measureText(textArray[i]) / 2).toFloat()
            canvas?.drawText(textArray[i], startX, startY, textPaint)
        }
    }
}