package com.app.func.view.all_demo

import android.content.Context
import android.content.res.TypedArray
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.app.func.R
import kotlin.math.min

class EmotionalFaceView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    // 1 Paint object for coloring and styling
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    // 2 Some colors for the face background, eyes and mouth.
    private var faceColor = DEFAULT_FACE_COLOR
    private var eyesColor = DEFAULT_EYES_COLOR
    private var mouthColor = DEFAULT_MOUTH_COLOR
    private var borderColor = DEFAULT_BORDER_COLOR
    private var borderWidth = DEFAULT_BORDER_WIDTH
    private val mouthPath = Path()
    private var size = 0

    // 3
    var happinessState = HAPPY
        set(state) {
            field = state
            // 4
            invalidate()
        }

    // 5
    init {
        paint.isAntiAlias = true
        setupAttributes(attrs)
    }

    private fun setupAttributes(attrs: AttributeSet?) {
        // 6 Obtain a typed array of attributes
        val typedArray: TypedArray = context.obtainStyledAttributes(
            attrs, R.styleable.EmotionalFaceView, 0, 0
        )
        // 7 Extract custom attributes into member variables
        happinessState =
            typedArray.getInt(R.styleable.EmotionalFaceView_state, HAPPY)
        faceColor = typedArray.getColor(R.styleable.EmotionalFaceView_faceColor, DEFAULT_FACE_COLOR)
        eyesColor = typedArray.getColor(R.styleable.EmotionalFaceView_eyesColor, DEFAULT_EYES_COLOR)
        mouthColor =
            typedArray.getColor(R.styleable.EmotionalFaceView_mouthColor, DEFAULT_MOUTH_COLOR)
        borderColor = typedArray.getColor(
            R.styleable.EmotionalFaceView_borderColor, DEFAULT_BORDER_COLOR
        )
        borderWidth = typedArray.getDimension(
            R.styleable.EmotionalFaceView_borderWidth, DEFAULT_BORDER_WIDTH
        )
        // 8 TypedArray objects are shared and must be recycled.
        typedArray.recycle()
    }


    /*Here you:
    1. Set the paint color to the faceColor and make it fill the drawing area.
    2. Calculate a radius for a circle which you want to draw as the face background.
    3. Draw the background circle with a center of (x,y), where x and y are equal to the half of size, and with the calculated radius.
    4. Change the paint color to the borderColor and make it just draw a border around the drawing area by setting the style to STROKE
    5. Draw a border with the same center but with a radius shorter than the previous radius by the borderWidth.*/
    private fun drawFaceBackground(canvas: Canvas?) {
        //Step 1
        paint.color = faceColor
        paint.style = Paint.Style.FILL
        //Step 2
        val radius = size / 2f
        //Step 3
        canvas?.drawCircle(size / 2f, size / 2f, radius, paint)
        //Step 4: Border
        paint.color = borderColor
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = borderWidth
        //Step 5: Draw Border
        canvas?.drawCircle(size / 2f, size / 2f, radius - borderWidth, paint)
    }

    private fun drawEyes(canvas: Canvas?) {
        //Step 1
        paint.color = eyesColor
        paint.style = Paint.Style.FILL
        //Step 2: Oval Eye Left
        val left = size * 0.32f
        val top = size * 0.23f
        val right = size * 0.43f
        val bottom = size * 0.5f
        val leftEyeRect = RectF(left, top, right, bottom)
        canvas?.drawOval(leftEyeRect, paint)
        //Step 3: Oval Eye Right
        val rightEyeRect = RectF(size * 0.57f, size * 0.23f, size * 0.68f, size * 0.50f)
        canvas?.drawOval(rightEyeRect, paint)
    }

    /*
    Here you:
    Set the starting point of the path to (x0,y0) by using the moveTo() method where:
    x0 is equal to 22% of the size.
    y0 is equal to 70% of the size.
    Draw a curved path from the starting point and through (x1,y1) that ends with (x2,y2) where:
    x1 is equal to 50% of the size.
    y1 is equal to 80% of the size.
    x2 is equal to 78% of the size.
    y2 is equal to 70% of the size.
    Draw a curved path starting from the last end point (x2,y2) and through (x3,y3) and that ends with (x0,y0) where:
    x3 is equal to 50% of the size.
    y3 is equal to 90% of the size.
    x0 is equal to 22% of the size.
    y0 is equal to 70% of the size.
    Set the paint color to the mouthColor and make it filling the drawing area.
    Draw the path to the canvas.
    * */
    private fun drawMouth(canvas: Canvas?) {
        // Clear
        mouthPath.reset()
        mouthPath.moveTo(size * 0.22f, size * 0.7f)
        if (happinessState == HAPPY) {
            // Happy mouth path
            mouthPath.quadTo(size * 0.5f, size * 0.80f, size * 0.78f, size * 0.7f)
            mouthPath.quadTo(size * 0.5f, size * 0.90f, size * 0.22f, size * 0.7f)
        } else {
            // Sad mouth path
            mouthPath.quadTo(size * 0.5f, size * 0.50f, size * 0.78f, size * 0.7f)
            mouthPath.quadTo(size * 0.5f, size * 0.60f, size * 0.22f, size * 0.7f)
        }
        paint.color = mouthColor
        paint.style = Paint.Style.FILL
        // Draw mouth path
        canvas?.drawPath(mouthPath, paint)
    }

    // 1
    companion object {
        private const val DEFAULT_FACE_COLOR = Color.YELLOW
        private const val DEFAULT_EYES_COLOR = Color.BLACK
        private const val DEFAULT_MOUTH_COLOR = Color.MAGENTA
        private const val DEFAULT_BORDER_COLOR = Color.RED
        private const val DEFAULT_BORDER_WIDTH = 4.0f
        const val HAPPY = 0
        const val SAD = 1
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        drawFaceBackground(canvas)
        drawEyes(canvas)
        drawMouth(canvas)
    }

    /*Here you:
    1. Calculate the smaller dimension of your view
    2. Use setMeasuredDimension(int, int) to store the measured width and measured height of the view,
    in this case making your view width and height equivalent.
    */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        //Step 1
        size = min(measuredWidth, measuredHeight)
        //Step 2
        setMeasuredDimension(size, size)
    }

}