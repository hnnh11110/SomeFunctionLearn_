package com.app.func.view.chart

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

class BezierView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    companion object {
        const val LINE_WIDTH: Float = 3F
        val TAG: String = BezierView::class.java.simpleName
    }

    // paints
    private val fillPaint = Paint()
        .apply {
            strokeWidth = LINE_WIDTH
            style = Paint.Style.FILL
            isAntiAlias = true
        }
    private val strokePaint = Paint()
        .apply {
            color = Color.BLACK
            strokeWidth = LINE_WIDTH
            style = Paint.Style.STROKE
            isAntiAlias = true
        }
    private val dashPaint = Paint()
        .apply {
            color = Color.GRAY
            strokeWidth = LINE_WIDTH
            style = Paint.Style.STROKE
            pathEffect = DashPathEffect(floatArrayOf(10f, 10f), 0f)
            isAntiAlias = true
        }

    // Initially allocate Points and Paths to prevent heavy allocations inside onDraw
    private val p1 = PointF(0.15f, 0.85f) // fixed
    private val p2 = PointF(0f, 0f)
    private val p3 = PointF(0f, 0f)
    private val p4 = PointF(0.85f, 0.85f) // fixed
    private val guideLinePath = Path()
    private val bezierPath = Path()

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawPoints(canvas, p1, p2, p3, p4)
        drawGuideLines(canvas, p1, p2, p3, p4)
        drawBezier(canvas, p1, p2, p3, p4)
    }

    /** Draw each point **/
    private fun drawPoints(canvas: Canvas, p1: PointF, p2: PointF, p3: PointF, p4: PointF) {
        drawPoint(canvas, p1, Color.BLACK)
        drawPoint(canvas, p2, Color.RED)
        drawPoint(canvas, p3, Color.GREEN)
        drawPoint(canvas, p4, Color.BLUE)
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

    /** draw the line with bezier coordinates **/
    private fun drawBezier(canvas: Canvas, p1: PointF, p2: PointF, p3: PointF, p4: PointF) {
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
        canvas.drawPath(bezierPath, strokePaint)
    }

    /** draw a single point circle **/
    private fun drawPoint(
        canvas: Canvas,
        point: PointF,
        color: Int,
        radius: Float = 10F
    ) {
        canvas.drawCircle(
            point.x * width,
            point.y * height,
            radius,
            fillPaint.apply { this.color = color })
    }
}