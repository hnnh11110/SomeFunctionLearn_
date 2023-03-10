package com.app.func.view.all_demo

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View


class CustomViewDemo @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var mPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var mPath: Path? = null

    //    private val listPoint: FloatArray = floatArrayOf(
//        PointF(12f, 14f),
//        PointF(10f, 8f),
//        PointF(100f, 80f),
//        PointF(78f, 88f),
//        PointF(109f, 48f),
//        PointF(60f, 8f),
//        PointF(10f, 90f),
//        PointF(78f, 58f),
//        PointF(95f, 88f),
//        PointF(120f, 118f),
//    )
    private val listPoint: FloatArray = floatArrayOf(
        12f, 14f, 100f, 80f, 78f, 88f, 109f, 48f, 60f, 90f, 78f, 58f, 95f, 88f, 120f, 118f,
    )


    init {
        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaint.color = Color.BLUE
//        mPaint.strokeWidth = 30f

        mPath = Path()
        mPaint.style = Paint.Style.STROKE
    }

    private fun drawPointByCanvas(canvas: Canvas?) {
        canvas?.drawPoint(120f, 120f, mPaint)
    }

    private fun drawPointsByCanvas(canvas: Canvas?) {
        canvas?.drawPoints(listPoint, mPaint)
    }

    private fun drawCurves(canvas: Canvas?) {
//This method fills in the starting point coordinates, that is, the P0 in the graph.
        mPath?.moveTo(100f, 300f)
        //Parametric 1 and 2: Represents the control point, which is the P1 in the graph.
        //Parameters 3 and 4: Represents the end point, which is the P2 in the graph.
        mPath?.quadTo(200f, 100f, 300f, 300f)
        //The meaning of the parameter is the same as above.
        mPath?.quadTo(400f, 500f, 500f, 300f)
        mPath?.let { canvas?.drawPath(it, mPaint) }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
//        drawPointByCanvas(canvas)
//        drawPointsByCanvas(canvas)
        drawCurves(canvas)
    }
}