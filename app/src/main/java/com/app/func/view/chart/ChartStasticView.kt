package com.app.func.view.chart

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.app.func.model.Temperature

class ChartStasticView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var listData = mutableListOf<Temperature>()
    private var listText = arrayListOf<Int>()

    private var paintLines = Paint()

    init {

        listData = mutableListOf(
            Temperature(10000L, 10),
            Temperature(12000L, 5),
            Temperature(20000L, -6),
            Temperature(23000L, 7),
            Temperature(31000L, 9),
            Temperature(43000L, 1),
            Temperature(55000L, 2),
            Temperature(61000L, 21),
        )

        paintLines.style = Paint.Style.STROKE
        paintLines.strokeCap = Paint.Cap.ROUND
        paintLines.strokeWidth = 3f
        paintLines.color = Color.RED
        paintLines.isAntiAlias = true
        //paintLines.alpha = 200
    }

    private fun drawLines(canvas: Canvas?) {
        canvas?.drawLine(10.toFloat(), y, 30f, y, paintLines)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        drawLines(canvas)

    }
}