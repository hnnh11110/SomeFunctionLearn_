package com.app.func.view.imageview_bg

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import com.app.func.R
import com.bumptech.glide.Glide
import kotlin.math.roundToInt

class CircleImage @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
    AppCompatImageView(context, attrs) {

    companion object {
        private const val STROKE_WIDTH = 5f
    }

    private val mRect = RectF()
    private val mClipPath: Path = Path()
    private var mImageSize = 0
    private var mDrawable: Drawable? = null
    private val mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mBorderPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var mBorderColor = 0
    private var mImageResource = 0

    init {
        getDefaultAttributes(attrs)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH
            && Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB
        ) {
            setLayerType(LAYER_TYPE_SOFTWARE, null)
        }
        mImageSize = context.resources.getDimensionPixelSize(R.dimen._300dp)
        setBorderColor(mBorderColor)
    }

    private fun getDefaultAttributes(attrs: AttributeSet?) {
        val typedArray = context.theme
            .obtainStyledAttributes(attrs, R.styleable.CircleImage, 0, 0)
        try {
            mBorderColor = typedArray.getColor(
                R.styleable.CircleImage_border_color,
                ContextCompat.getColor(context, R.color.colorPrimary)
            )
//            mImageResource = typedArray.getInteger(R.styleable.CircleImage_src, R.drawable.her)
        } finally {
            typedArray.recycle()
        }
    }

    private fun setBorderColor(color: Int) {
        mBorderPaint.color = color
        mBorderPaint.style = Paint.Style.STROKE
        mBorderPaint.isAntiAlias = true
        mBorderPaint.strokeWidth = STROKE_WIDTH
        invalidate()
    }

    private fun createDrawable() {
        mDrawable = object : Drawable() {
            @SuppressLint("CanvasSize")
            override fun draw(canvas: Canvas) {
                val centerX = (canvas.width * 0.5f).roundToInt()
                val centerY = (canvas.height * 0.5f).roundToInt()

                /**
                 * draw a circle shape for placeholder image
                 */

                canvas.drawCircle(
                    centerX.toFloat(), centerY.toFloat(),
                    (canvas.height / 2).toFloat(), mPaint
                )
                canvas.drawCircle(
                    centerX.toFloat(),
                    centerY.toFloat(), (canvas.height / 2).toFloat(), mBorderPaint
                )
            }

            override fun setAlpha(alpha: Int) {

            }

            override fun setColorFilter(colorFilter: ColorFilter?) {

            }

            override fun getOpacity(): Int = PixelFormat.UNKNOWN
        }
    }

    private fun fillImages() {
        mPaint.color = Color.TRANSPARENT
        createDrawable()
        Glide.with(context)
            .load(mImageResource)
            .placeholder(mDrawable)
            .centerCrop()
            .override(mImageSize, mImageSize)
            .into(this)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        /**
         * Set the canvas bounds here
         **/
        val screenWidth = MeasureSpec.getSize(widthMeasureSpec)
        val screenHeight = MeasureSpec.getSize(heightMeasureSpec)
        mRect[0f, 0f, screenWidth.toFloat()] = screenHeight.toFloat()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val borderWidth = 1f
        canvas?.drawCircle(mRect.centerX(), mRect.centerY(), (mRect.height() / 2) - borderWidth, mPaint)
        canvas?.drawCircle(mRect.centerX(), mRect.centerY(), (mRect.height() / 2) - borderWidth, mBorderPaint)
        mClipPath.addCircle(mRect.centerX(), mRect.centerY(), (mRect.height() / 2), Path.Direction.CW)
        canvas?.clipPath(mClipPath)
        super.onDraw(canvas)
        showImage()
        canvas?.drawColor(Color.TRANSPARENT)
    }

    private fun showImage() {
        fillImages()
    }

}