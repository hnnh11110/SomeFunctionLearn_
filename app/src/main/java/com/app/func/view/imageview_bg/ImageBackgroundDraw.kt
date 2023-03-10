package com.app.func.view.imageview_bg

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.app.func.R

class ImageBackgroundDraw @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mPath = Path()
    private var mCenterCircleRectF: RectF? = null
    private val mBorderWidth = resources.getDimensionPixelSize(R.dimen._1dp).toFloat()
    private val mTopCornerRadius = resources.getDimensionPixelSize(R.dimen._20dp).toFloat()
    private val mMiddleCornerRadius = resources.getDimensionPixelSize(R.dimen._18dp).toFloat()
    private val mBottomCornerRadius = resources.getDimensionPixelSize(R.dimen._16dp).toFloat()
    private val mCenterCircleRadius = resources.getDimensionPixelSize(R.dimen._32dp).toFloat()

    init {
        mPaint.apply {
            style = Paint.Style.FILL
            color = Color.RED
            strokeWidth = mBorderWidth
        }
    }

    private fun drawPath() {
        val halfWidthWithoutCorners =
            width / 2 - mCenterCircleRadius - mMiddleCornerRadius - mTopCornerRadius
        val heightWithoutCorners = height - mTopCornerRadius - mBottomCornerRadius
        val widthWithoutCorners = width - mBottomCornerRadius * 2

        mPath.reset()
        mPath.apply {
            /*
            moveTo(0f + mTopCornerRadius, 0f)
            rQuadTo(-mTopCornerRadius, 0f, -mTopCornerRadius, mTopCornerRadius)
            rLineTo(0f, heightWithoutCorners)
            rQuadTo(0f, mBottomCornerRadius, mBottomCornerRadius, mBottomCornerRadius)
            rLineTo(widthWithoutCorners, 0f)
            rQuadTo(mBottomCornerRadius, 0f, mBottomCornerRadius, -mBottomCornerRadius)
            rLineTo(0f, -heightWithoutCorners)
            rQuadTo(0f, -mTopCornerRadius, -mTopCornerRadius, -mTopCornerRadius)
            rLineTo(-halfWidthWithoutCorners, 0f)
            rQuadTo(-mMiddleCornerRadius, 0f, -mMiddleCornerRadius, mMiddleCornerRadius)
            addArc(mCenterCircleRectF!!, 0f, 180f)
            rQuadTo(0f, -mMiddleCornerRadius, -mMiddleCornerRadius, -mMiddleCornerRadius)
            rLineTo(-halfWidthWithoutCorners, 0f)
             */
            moveTo(0f + mTopCornerRadius, 0f)
            rQuadTo(-mTopCornerRadius, 0f, -mTopCornerRadius, mTopCornerRadius)
            rLineTo(0f, heightWithoutCorners)
            rQuadTo(0f, mBottomCornerRadius, mBottomCornerRadius, mBottomCornerRadius)
            rLineTo(widthWithoutCorners, 0f)
            rQuadTo(mBottomCornerRadius, 0f, mBottomCornerRadius, -mBottomCornerRadius)
            rLineTo(0f, -heightWithoutCorners)
            rQuadTo(0f, -mTopCornerRadius, -mTopCornerRadius, -mTopCornerRadius)
            rLineTo(-halfWidthWithoutCorners, 0f)
            rQuadTo(-mMiddleCornerRadius, 0f, -mMiddleCornerRadius, mMiddleCornerRadius)
            mCenterCircleRectF?.let { addArc(it, 0f, 180f) }
            rQuadTo(0f, -mMiddleCornerRadius, -mMiddleCornerRadius, -mMiddleCornerRadius)
            rLineTo(-halfWidthWithoutCorners, 0f)
        }
    }

    private fun calculateCenterCircleRectF(): RectF {
        val centerX = width / 2
        val centerY = 0f + mMiddleCornerRadius
        return RectF(
            centerX - mCenterCircleRadius,
            centerY - mCenterCircleRadius,
            centerX + mCenterCircleRadius,
            centerY + mCenterCircleRadius
        )
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (mCenterCircleRectF == null) {
            mCenterCircleRectF = calculateCenterCircleRectF()
        }
        drawPath()
        canvas?.drawPath(mPath, mPaint)
    }
}