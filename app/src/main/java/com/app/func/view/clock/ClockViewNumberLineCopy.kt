package com.app.func.view.clock

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import java.util.*

class ClockViewNumberLineCopy @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var mWidth = 120f //The X axis coordinate of the center of the clock
    private var mHeight = 120f //The Y-axis coordinate of the center of the clock
    private var mRadius = 110f //The radius of the clock

    //Get system time
    private val calendar: Calendar = Calendar.getInstance()
    private val hour: Int = calendar.get(Calendar.HOUR)
    private val minute: Int = calendar.get(Calendar.MINUTE)
    private val second: Int = calendar.get(Calendar.SECOND)
    private var mNumbers = intArrayOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11)

    private var hourPaint: Paint = Paint().apply {
        color = Color.BLACK
        style = Paint.Style.FILL
        strokeWidth = 6f
    }
    private var minPaint: Paint = Paint().apply {
        color = Color.GRAY
        style = Paint.Style.FILL
        strokeWidth = 5f
    }
    private var secPaint: Paint = Paint().apply {
        color = Color.RED
        style = Paint.Style.FILL
        strokeWidth = 3f
    }

    private val paint = Paint().apply {
        style = Paint.Style.STROKE
        strokeWidth = 3f
        color = Color.BLACK
    }

    private fun initView() {
//        mWidth = width.toFloat()
//        mHeight = height.toFloat()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        initView()
        drawPointsNumbers(canvas)
        drawCircleHeart(canvas)
        drawHourHand(canvas)
        drawMinusHand(canvas)
        drawSecondHand(canvas)
        //Call the ondraw method repeatedly, draw continuously, use the clock to show the effect of walking
        invalidate()
    }

    private fun drawPointsNumbers(canvas: Canvas) {
        //Draw a large scale
        for (i in 1..12) {
            canvas.save() //Save the current state
            //1: The angle of rotation
            // 2. The X coordinate of the rotation center
            // 3. The Y coordinate of the rotation center
            canvas.rotate((i * 30).toFloat(), mWidth, mHeight)
            //Draw tick marks equivalent to 250, 50, 250, 65
            if (i % 3 == 0) {
                paint.color = Color.RED
            } else {
                paint.color = Color.BLACK
            }
            canvas.drawLine(
                mWidth, (mHeight - mRadius), mWidth, (mHeight - mRadius + 15), paint
            )
            //Return to the initial saved state
            canvas.restore()
        }
    }


    private fun drawHourHand(canvas: Canvas) {
        //Draw the hour hand
        canvas.save()
        //The rotation of the canvas, parameter
        // 1: the angle of rotation
        // 2: the X-axis coordinate of the rotation around the rotating point
        // 3: the Y-axis coordinate
        //The first parameter: For example, 4:30 hour hand offset angle
        //4*30=120 indicates the position of the hour hand at this angle of the clock at four o'clock
        //30 minutes/60 means the percentage occupied and then *30 is the angle of 30 minutes (30 degrees)
        canvas.rotate(hour * 30f + minute / 60f * 30f, mWidth, mHeight)
        canvas.drawLine(mWidth, mHeight + 40f, mWidth, mHeight - 60f, hourPaint)
        canvas.restore()
    }

    private fun drawMinusHand(canvas: Canvas) {
        //Draw the minute hand
        canvas.save()
        //The minute hand moves 6 degrees every minute
        canvas.rotate(minute * 6f, mWidth, mHeight)
        canvas.drawLine(mWidth, mHeight + 30f, mWidth, mHeight - 80, minPaint)
        canvas.restore()
    }

    private fun drawSecondHand(canvas: Canvas) {
        //Draw the second hand
        canvas.save()
        //The hour hand moves 6 degrees every minute
        canvas.rotate(second * 6f, mWidth, mHeight)
        canvas.drawLine(mWidth, mHeight + 20, mWidth, mHeight - 110, secPaint)
        canvas.restore()
    }

    private fun drawCircleHeart(canvas: Canvas) {
        //Draw the heart of the clock
        val centerPaint = Paint()
        centerPaint.color = Color.RED
        centerPaint.style = Paint.Style.FILL
        canvas.drawCircle(mWidth, mHeight, 10f, centerPaint)
    }
}