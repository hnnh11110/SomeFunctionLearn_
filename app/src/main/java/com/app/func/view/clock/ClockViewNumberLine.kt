package com.app.func.view.clock

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import java.util.*
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

class ClockViewNumberLine @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    /*
    Link: https://developpaper.com/android-custom-view-to-achieve-clock-effect/
    https://blog.actorsfit.com/a?ID=01650-bdf46078-7088-4844-b522-b706c95854ad
     */

    //    val width = 120f //The X axis coordinate of the center of the clock
//    val height = 120f //The Y-axis coordinate of the center of the clock
    val radius = 110f //The radius of the clock

    private var mHeight = 0f
    private var mWidth = 0f
    private var mRadius = 0f
    private var mCentreX = 0f
    private var mCentreY = 0f
    private var mAngle = 0.0
    private var mRect: Rect = Rect()
    private var mPadding = 50f


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
        mWidth = width.toFloat()
        mHeight = height.toFloat()
        mCentreX = mWidth / 2
        mCentreY = mHeight / 2
        mRadius = min(mHeight, mWidth) / 2 - mPadding
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        initView()
        drawPointsNumbers(canvas)
//        drawHourHand(canvas)
//        drawMinusHand(canvas)
//        drawSecondHand(canvas)
        drawCircleHeart(canvas)
        invalidate()
    }

    private fun drawPointsNumbers(canvas: Canvas) {
        //Draw a large scale
        for (number in mNumbers) {
            val num = number.toString()
            paint.getTextBounds(num, 0, num.length, mRect)
            val angle = Math.PI / 6 * (number - 3)
            val x = (mCentreX + cos(angle) * mRadius - mRect.width() / 2).toInt()
            val y = (mCentreY + sin(angle) * mRadius + mRect.height() / 2).toInt()
//            canvas.drawText("-", x.toFloat(), y.toFloat(), paint)
            if (number % 3 == 0) {
                paint.color = Color.RED
            } else {
                paint.color = Color.BLACK
            }
            canvas.drawLine(
                measuredWidth / 2f,
                5f,
                measuredWidth / 5f,
                mHeight - radius + 15,
                paint
            )
            canvas.restore() //Return to the initial saved state
        }
//        mAngle = (Math.PI / 30 - Math.PI / 2)
//        val angle = Math.PI / 6 * (number - 3)
//        val x = (mCentreX + cos(angle) * mRadius - mRect.width() / 2).toInt()
//        val y = (mCentreY + sin(angle) * mRadius + mRect.height() / 2).toInt()

//        for (i in 1..12) {
//            canvas.save() //Save the current state
//            //1: The angle of rotation 2. The X coordinate of the rotation center 3. The Y coordinate of the rotation center
//            canvas.rotate(i * 30f, mWidth, mHeight)
//            //Draw tick marks equivalent to 250, 50, 250, 65
//            if (i % 3 == 0) {
//                paint.color = Color.RED
//            } else {
//                paint.color = Color.BLACK
//            }
//            canvas.drawLine(mWidth, mHeight - radius, mWidth, mHeight - radius + 15, paint)
//            canvas.restore() //Return to the initial saved state
//        }
    }

    private fun drawHourHand(canvas: Canvas) {
        //Draw the hour hand
        canvas.save()
        //The rotation of the canvas, parameter 1: the angle of rotation 2: the X-axis coordinate of the rotation around the rotating point 3: the Y-axis coordinate
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
        canvas.drawCircle(mCentreX, mCentreY, 10f, centerPaint)
    }
}