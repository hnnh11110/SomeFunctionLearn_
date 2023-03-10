package com.app.func.view.polygon

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.app.func.R
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

class PolygonPathView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val maxNumOfSides = 8
    private var size = 600
    private var cornerRadius = 24f
    private var cornerPathEffect = CornerPathEffect(cornerRadius)
    private lateinit var bitmap: Bitmap
    private var pos = FloatArray(size = 2)
    private var tan = FloatArray(size = 2)
    private var bm_offsetX: Float = 0f
    private var bm_offsetY: Float = 0f
    private var imageMatrix: Matrix = Matrix()
    var pathPercentage = 50f
        set(value) {
            field = when {
                value < 0f -> {
                    0f
                }
                value > 100f -> {
                    100f
                }
                else -> value
            }
            invalidate()
        }


    private val polygons = listOf(
        Polygon(3, 20f, Color.WHITE),
        Polygon(4, 40f, Color.BLUE),
        Polygon(5, 60f, Color.GREEN),
        Polygon(6, 80f, Color.RED),
        Polygon(7, 100f, Color.MAGENTA),
        Polygon(8, 120f, Color.CYAN)
    )

    init {
        loadImageBitmap()
    }

    private fun loadImageBitmap() {
        bitmap =
            Bitmap.createScaledBitmap(
                BitmapFactory.decodeResource(resources, R.drawable.mouse),
                16,
                16,
                false
            )

        bm_offsetX = bitmap.width/2f
        bm_offsetY = bitmap.height/2f

    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.drawColor(Color.BLACK)
        paint.style = Paint.Style.STROKE
        for (i in 0..(maxNumOfSides - 3)) {
            val polygon = polygons[i]
            paint.color = polygon.color
            val path = createPathAndEffect(polygon, paint)
            canvas?.drawPath(path, paint)
            calculateBitmap(path, canvas)
        }
        super.onDraw(canvas)

    }

    private fun calculateBitmap(path: Path, canvas: Canvas?) {
        val pathMeasure = PathMeasure(path, false)
        val length = pathMeasure.length
        pathMeasure.getPosTan(length * pathPercentage/100f, pos, tan)
        imageMatrix.reset()
        val degrees = (Math.atan2(tan[1].toDouble(), tan[0].toDouble()) * 180.0 / Math.PI).toFloat()
        imageMatrix.postRotate(degrees, bm_offsetX, bm_offsetY)
        imageMatrix.postTranslate(pos[0] - bm_offsetX, pos[1] - bm_offsetY)
        canvas?.drawBitmap(bitmap, imageMatrix, null)
    }

    private fun createPathAndEffect(polygon: Polygon, paint: Paint): Path {
        val path = Path()
        val angle = 2f * Math.PI / polygon.side
        val radius = polygon.radius
        val cx = pivotX
        val cy = pivotY
        path.moveTo(cx + (radius * cos(0f)), cy + (radius * sin(0f)))
        for (i in 1 until polygon.side) {
            path.lineTo(
                cx + (radius * cos(angle * i)).toFloat(),
                cy + (radius * sin(angle * i)).toFloat()
            )
        }
        path.close()
        //Corner
        paint.pathEffect = cornerPathEffect
        //Percent display
        val pathMeasure = PathMeasure(path, false)
        val length = pathMeasure.length
        val pathPercentage = 100
        val intervals: FloatArray =
            floatArrayOf(length * pathPercentage / 100f, length * (100f - pathPercentage) / 100f)
        val dashPathEffect = DashPathEffect(intervals, 1f)
        paint.pathEffect = ComposePathEffect(dashPathEffect, cornerPathEffect)

        return path
    }

    /*
    Used:
      btnAnimate.setOnClickListener {
        ValueAnimator.ofFloat(0f, 100f).apply {
            duration = 2000L
            interpolator = LinearInterpolator()
            repeatCount = INFINITE
            repeatMode = RESTART
            addUpdateListener {
                polygonView.pathPercentage = it.animatedValue as Float
                progressSeekBar.progress = (it.animatedValue as Float).toInt()
            }
        }.start()
    }
     */


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        //Step 1
        size = min(measuredWidth, measuredHeight)
        //Step 2
        setMeasuredDimension(size, size)
    }
}