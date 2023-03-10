package com.app.func.view.clock

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.AttributeSet
import android.view.View
import java.util.*
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

/**
 * Created by zhang on 2017/12/20.
 */

class ClockView @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    companion object {
        private const val DEFAULT_WIDTH = 200 // default width
    }

    private var mBlackPaint: Paint = Paint().apply {
        color = Color.BLACK
        strokeWidth = 5f
        isAntiAlias = true
        style = Paint.Style.STROKE
    }
    private var mRedPaint: Paint = Paint().apply {
        color = Color.RED
        strokeWidth = 5f
        isAntiAlias = true
    }
    private var mBlackPaint2: Paint = Paint().apply {
        color = Color.BLACK
        isAntiAlias = true
        style = Paint.Style.FILL
    }
    private var mTextPaint: Paint = Paint().apply {
        color = Color.BLACK
        textSize = 30f
        isAntiAlias = true
    }
    private var hour: Int = Calendar.getInstance().get(Calendar.HOUR)
    private var minute: Int = Calendar.getInstance().get(Calendar.MINUTE)
    private var second: Int = Calendar.getInstance().get(Calendar.SECOND)
    private val textArray = arrayOf("12", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11")
    private var refreshThread: Thread? = null
    private var mHandler = @SuppressLint("HandlerLeak")
    object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                0 -> {
                    invalidate()
                }
            }

        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        //Get current time
        getCurrentTime()
        //Draw the outermost circle first
        drawOuterCircle(canvas)
        //Draw scale
        drawScale(canvas)
        //Draw text
        drawTimeText(canvas)
        //Draw the needle
        drawHand(canvas)
        //Draw epicenter
        drawCenter(canvas)
    }

    private fun getCurrentTime() {
        val calendar = Calendar.getInstance()
        hour = calendar.get(Calendar.HOUR)
        minute = calendar.get(Calendar.MINUTE)
        second = calendar.get(Calendar.SECOND)
    }

    private fun drawOuterCircle(canvas: Canvas?) {
        mBlackPaint.strokeWidth = 5f
        canvas?.drawCircle(
            measuredWidth / 2.toFloat(),
            measuredHeight / 2.toFloat(),
            (measuredWidth / 2 - 5).toFloat(),
            mBlackPaint
        )
    }

    private fun drawCenter(canvas: Canvas?) {
        canvas?.drawCircle(
            measuredWidth / 2.toFloat(),
            measuredHeight / 2.toFloat(),
            20f,
            mBlackPaint2
        )
    }

    private fun drawHand(canvas: Canvas?) {
        drawSecond(canvas, mRedPaint)
        mBlackPaint.strokeWidth = 7f
        drawMinute(canvas, mBlackPaint)
        mBlackPaint.strokeWidth = 9f
        drawHour(canvas, mBlackPaint)
    }

    private fun drawTimeText(canvas: Canvas?) {
        val textR = (measuredWidth / 2 - 50).toFloat() // radius of circle formed by text
//        val textR = (width / 2 - 50).toFloat() // radius of circle formed by text
        for (i in 0..11) {
            //Draw text的起始坐标
            val startX =
                (measuredWidth / 2 + textR * sin(Math.PI / 6 * i) - mTextPaint.measureText(
                    textArray[i]
                ) / 2).toFloat()
            val startY =
                (measuredHeight / 2 - textR * cos(Math.PI / 6 * i) + mTextPaint.measureText(
                    textArray[i]
                ) / 2).toFloat()
            canvas?.drawText(textArray[i], startX, startY, mTextPaint)
        }
    }

    private fun drawScale(canvas: Canvas?) {
        var scaleLength: Float?
        canvas?.save()
        //0.. 59 for [0,59]
        for (i in 0..59) {
            if (i % 5 == 0) {
                //Large scale
                mBlackPaint.strokeWidth = 5f
                scaleLength = 20f
            } else {
                //Small scale
                mBlackPaint.strokeWidth = 3f
                scaleLength = 10f
            }
            canvas?.drawLine(
                measuredWidth / 2.toFloat(),
                5f,
                measuredWidth / 2.toFloat(),
                (5 + scaleLength),
                mBlackPaint
            )
            canvas?.rotate(
                360f / 60,
                measuredWidth / 2.toFloat(),
                measuredHeight / 2.toFloat()
            )
        }
        //Restore the original state
        canvas?.restore()
    }

    /**
     *Draw second hand
     */
    private fun drawSecond(canvas: Canvas?, paint: Paint) {
        //Second hand long radius (the needle will pass through the center of the watch, so you need to calculate the start and end radius according to the two radii)
        val longR = measuredWidth / 2 - 50
        val shortR = 60
        val startX = (measuredWidth / 2 - shortR * sin(second.times(Math.PI / 30))).toFloat()
        val startY = (measuredWidth / 2 + shortR * cos(second.times(Math.PI / 30))).toFloat()
        val endX = (measuredWidth / 2 + longR * sin(second.times(Math.PI / 30))).toFloat()
        val endY = (measuredWidth / 2 - longR * cos(second.times(Math.PI / 30))).toFloat()
        canvas?.drawLine(startX, startY, endX, endY, paint)
    }

    /**
     *Draw minute hand
     */
    private fun drawMinute(canvas: Canvas?, paint: Paint) {
        //The radius is a little smaller than the second hand
        val longR = measuredWidth / 2 - 70
        val shortR = 50
        val startX = (measuredWidth / 2 - shortR * sin(minute.times(Math.PI / 30))).toFloat()
        val startY = (measuredWidth / 2 + shortR * cos(minute.times(Math.PI / 30))).toFloat()
        val endX = (measuredWidth / 2 + longR * sin(minute.times(Math.PI / 30))).toFloat()
        val endY = (measuredWidth / 2 - longR * cos(minute.times(Math.PI / 30))).toFloat()
        canvas?.drawLine(startX, startY, endX, endY, paint)
    }


    /**
     *Draw the hour hand
     */
    private fun drawHour(canvas: Canvas?, paint: Paint) {
        //The radius is a little smaller than the second hand
        val longR = measuredWidth / 2 - 90
        val shortR = 40
        val startX = (measuredWidth / 2 - shortR * sin(hour.times(Math.PI / 6))).toFloat()
        val startY = (measuredWidth / 2 + shortR * cos(hour.times(Math.PI / 6))).toFloat()
        val endX = (measuredWidth / 2 + longR * sin(hour.times(Math.PI / 6))).toFloat()
        val endY = (measuredWidth / 2 - longR * cos(hour.times(Math.PI / 6))).toFloat()
        canvas?.drawLine(startX, startY, endX, endY, paint)
    }

    /**
     *Conduct measurement
     */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthSpecMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSpecSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSpecMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSpecSize = MeasureSpec.getSize(heightMeasureSpec)
        val result =
            if (widthSpecMode == MeasureSpec.AT_MOST && heightSpecMode == MeasureSpec.AT_MOST) {
                DEFAULT_WIDTH
            } else {
                min(widthSpecSize, heightSpecSize)
            }
        setMeasuredDimension(result, result)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        //Start thread refresh interface
        refreshThread = Thread {
            while (true) {
                try {
                    Thread.sleep(500)
                    mHandler.sendEmptyMessage(0)
                } catch (e: InterruptedException) {
                    break
                }
            }
        }
        refreshThread?.start()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        mHandler.removeCallbacksAndMessages(null)
        //Interrupt thread
        refreshThread?.interrupt()
    }
}