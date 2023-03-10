package com.app.func.view.chart

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

class BezierCurveLine @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paintLineHorizontal = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.STROKE
        strokeWidth = 3f
        color = Color.RED
    }
    private val paintLineVertical = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.STROKE
        strokeWidth = 6f
        color = Color.RED
    }

    private val p1 = PointF(0.15f, 0.85f) // fixed
    private val p2 = PointF(0f, 0f)
    private val p3 = PointF(0f, 0f)
    private val p4 = PointF(0.85f, 0.85f) // fixed
    private val bezierPath = Path()

    private val strokePaint = Paint().apply {
        color = Color.BLACK
        strokeWidth = LINE_WIDTH
        style = Paint.Style.STROKE
        isAntiAlias = true
    }

    private val fillPaint = Paint().apply {
        strokeWidth = LINE_WIDTH
        style = Paint.Style.FILL
        isAntiAlias = true
    }

    private val dashPaint = Paint().apply {
        color = Color.GRAY
        strokeWidth = LINE_WIDTH
        style = Paint.Style.STROKE
        pathEffect = DashPathEffect(floatArrayOf(10f, 10f), 0f)
        isAntiAlias = true
    }
    private val guideLinePath = Path()

    /** draw a single point circle **/
    private fun drawPoint(
        canvas: Canvas?,
        point: PointF,
        color: Int,
        radius: Float = 10F
    ) {
        canvas?.drawCircle(
            point.x * width,
            point.y * height,
            radius,
            fillPaint.apply { this.color = color })
    }

    /** Draw each point **/
    private fun drawPoints(canvas: Canvas?, p1: PointF, p2: PointF, p3: PointF, p4: PointF) {
        drawPoint(canvas, p1, Color.BLACK)
        drawPoint(canvas, p2, Color.RED)
        drawPoint(canvas, p3, Color.GREEN)
        drawPoint(canvas, p4, Color.BLUE)
    }


    /** draw the line with bezier coordinates **/
    private fun drawBezier(canvas: Canvas?, p1: PointF, p2: PointF, p3: PointF, p4: PointF) {
        bezierPath.apply {
            reset()
            moveTo(p1.x * width, p1.y * height)
            cubicTo(
                p2.x * width,
                p2.y * height,
                p3.x * width,
                p3.y * height,
                p4.x * width,
                p4.y * height
            )
        }
        // draw!
        canvas?.drawPath(bezierPath, strokePaint)
    }

    /** Draw dashed straight lines connecting points **/
    private fun drawGuideLines(canvas: Canvas, p1: PointF, p2: PointF, p3: PointF, p4: PointF) {
        guideLinePath.apply {
            reset()
            moveTo(p1.x * width, p1.y * height)
            lineTo(p2.x * width, p2.y * height)
            lineTo(p3.x * width, p3.y * height)
            lineTo(p4.x * width, p4.y * height)
        }
        // draw
        canvas.drawPath(guideLinePath, dashPaint)
    }

    private val listData =
        arrayOf(
            DataInfo(120f, 150f),
            DataInfo(121f, 183f),
            DataInfo(123f, 191f),
            DataInfo(124f, 185f),
            DataInfo(125f, 234f),
            DataInfo(126f, 266f),
            DataInfo(127f, 192f),
            DataInfo(128f, 188f),
            DataInfo(129f, 322f)
        )

    init {

    }

    var paint: Paint = Paint().apply {
        style = Paint.Style.STROKE
        color = Color.RED
        strokeWidth = 3f
    }
    var path: Path = Path()

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        path.moveTo(34f, 259f)
        path.cubicTo(68f, 151f, 286f, 350f, 336f, 252f)
//        for (i in listData.indices - 3) {
//            path.cubicTo(
//                listData[i].cx,
//                listData[i].cy,
//                listData[i + 1].cx,
//                listData[i + 1].cy,
//                listData[i + 2].cx,
//                listData[i + 2].cy
//            )
//        }
        canvas?.drawPath(path, paint)
        for (i in 1..5) {
            canvas?.drawLine(
                0 + 20f,
                ((height - 30f) / 5f) * i,
                width - 50f,
                ((height - 30f) / 5f) * i,
                paintLineHorizontal
            )
        }

        canvas?.drawLine(
            0 + 20f,
            ((height - 30f) / 5f) - 30f,
            0 + 20f,
            height - 30f,
            paintLineVertical
        )

    }

    companion object {
        const val LINE_WIDTH = 2f
    }
}

data class DataInfo(val cx: Float, val cy: Float) {

//    fun createData(){
//        thi
//    }
}
