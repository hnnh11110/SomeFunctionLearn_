package com.app.func.view.progress

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import com.app.func.R
import kotlin.math.*


class SwagPointsCopy @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    companion object{
        private var INVALID_VALUE = -1
        private const val MAX = 100
        private const val MIN = 0

        /**
         * Offset = -90 indicates that the progress starts from 12 o'clock.
         */
        private const val ANGLE_OFFSET = -90f
    }

    /**
     * The current points value.
     */
    private var mPoints = MIN

    /**
     * The min value of progress value.
     */
    private var mMin = MIN

    /**
     * The Maximum value that this SeekArc can be set to
     */
    private var mMax = MAX

    /**
     * The increment/decrement value for each movement of progress.
     */
    private var mStep = 10

    /**
     * The Drawable for the seek arc thumbnail
     */
    private var mIndicatorIcon: Drawable? = null


    private var mProgressWidth = 12f
    private var mArcWidth = 12f
    private var mClockwise = true
    private var mEnabled = true


    // internal variables
    /**
     * The counts of point update to determine whether to change previous progress.
     */
    private var mUpdateTimes = 0
    private var mPreviousProgress = -1f
    private var mCurrentProgress = 0f

    /**
     * Determine whether reach max of point.
     */
    private var isMax = false

    /**
     * Determine whether reach min of point.
     */
    private var isMin = false

    private var mArcRadius = 0
    private val mArcRect = RectF()
    private var mArcPaint: Paint = Paint()

    private var mProgressSweep = 0f
    private var mProgressPaint: Paint = Paint()

    private var mTextSize = 72f
    private var mTextPaint: Paint = Paint()
    private val mTextRect: Rect = Rect()

    private var mTranslateX = 0
    private var mTranslateY = 0

    // the (x, y) coordinator of indicator icon
    private var mIndicatorIconX = 0
    private var mIndicatorIconY = 0

    /**
     * The current touch angle of arc.
     */
    private var mTouchAngle = 0.0
    private var mOnSwagPointsChangeListener: OnSwagPointsChangeListener? = null


    init {
        val density = resources.displayMetrics.density
        // Defaults, may need to link this into theme settings
        var arcColor = ContextCompat.getColor(context, R.color.color_arc)
        var progressColor = ContextCompat.getColor(context, R.color.color_progress)
        var textColor = ContextCompat.getColor(context, R.color.color_text)
        mProgressWidth *= density
        mArcWidth *= density
        mTextSize *= density
        mIndicatorIcon = ContextCompat.getDrawable(context, R.drawable.mouse)
        if (attrs != null) {
            // Attribute initialization
            val a = context.obtainStyledAttributes(
                attrs,
                R.styleable.SwagPointsCopy, 0, 0
            )
            val indicatorIcon = a.getDrawable(R.styleable.SwagPointsCopy_indicatorIcon)
            if (indicatorIcon != null) mIndicatorIcon = indicatorIcon
            val indicatorIconHalfWidth = mIndicatorIcon!!.intrinsicWidth / 2
            val indicatorIconHalfHeight = mIndicatorIcon!!.intrinsicHeight / 2
            mIndicatorIcon!!.setBounds(
                -indicatorIconHalfWidth, -indicatorIconHalfHeight, indicatorIconHalfWidth,
                indicatorIconHalfHeight
            )
            mPoints = a.getInteger(R.styleable.SwagPointsCopy_points, mPoints)
            mMin = a.getInteger(R.styleable.SwagPointsCopy_min, mMin)
            mMax = a.getInteger(R.styleable.SwagPointsCopy_max, mMax)
            mStep = a.getInteger(R.styleable.SwagPointsCopy_step, mStep)
            mProgressWidth =
                a.getDimension(R.styleable.SwagPointsCopy_progressWidth, mProgressWidth)

            progressColor = a.getColor(R.styleable.SwagPointsCopy_progressColor, progressColor)
            mArcWidth = a.getDimension(R.styleable.SwagPointsCopy_arcWidth, mArcWidth)
            arcColor = a.getColor(R.styleable.SwagPointsCopy_arcColor, arcColor)
            mTextSize = a.getDimension(R.styleable.SwagPointsCopy_textSize, mTextSize)
            textColor = a.getColor(R.styleable.SwagPointsCopy_textColor, textColor)
            mClockwise = a.getBoolean(
                R.styleable.SwagPointsCopy_clockwise,
                mClockwise
            )
            mEnabled = a.getBoolean(R.styleable.SwagPointsCopy_enabled, mEnabled)
            a.recycle()
        }

        // range check
        mPoints = if (mPoints > mMax) mMax else mPoints
        mPoints = if (mPoints < mMin) mMin else mPoints
        mProgressSweep = mPoints.toFloat() / valuePerDegree()

        mArcPaint.color = arcColor
        mArcPaint.isAntiAlias = true
        mArcPaint.style = Paint.Style.STROKE
        mArcPaint.strokeWidth = mArcWidth

        mProgressPaint.color = progressColor
        mProgressPaint.isAntiAlias = true
        mProgressPaint.style = Paint.Style.STROKE
        mProgressPaint.strokeWidth = mProgressWidth

        mTextPaint.color = textColor
        mTextPaint.isAntiAlias = true
        mTextPaint.style = Paint.Style.FILL
        mTextPaint.textSize = mTextSize
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = getDefaultSize(suggestedMinimumWidth, widthMeasureSpec)
        val height = getDefaultSize(suggestedMinimumHeight, heightMeasureSpec)
        val min = min(width, height)
        mTranslateX = (width * 0.5f).toInt()
        mTranslateY = (height * 0.5f).toInt()
        val arcDiameter = min - paddingLeft
        mArcRadius = arcDiameter / 2
        val top = (height / 2 - arcDiameter / 2).toFloat()
        val left = (width / 2 - arcDiameter / 2).toFloat()
        mArcRect[left, top, left + arcDiameter] = top + arcDiameter
        updateIndicatorIconPosition()
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onDraw(canvas: Canvas) {
        if (!mClockwise) {
            canvas.scale(-1f, 1f, mArcRect.centerX(), mArcRect.centerY())
        }

        // draw the text
        val textPoint = mPoints.toString()
        mTextPaint.getTextBounds(textPoint, 0, textPoint.length, mTextRect)
        // center the text
        val xPos = width / 2f - mTextRect.width() / 2f
        val yPos = (mArcRect.centerY() - (mTextPaint.descent() + mTextPaint.ascent()) / 2)
        //		Log.d("onDraw", String.valueOf(mPoints));
        canvas.drawText(mPoints.toString(), xPos, yPos, mTextPaint)

        // draw the arc and progress
        canvas.drawArc(mArcRect, ANGLE_OFFSET, 360f, false, mArcPaint)
        canvas.drawArc(mArcRect, ANGLE_OFFSET, mProgressSweep, false, mProgressPaint)
        if (mEnabled) {
            // draw the indicator icon
            canvas.translate((mTranslateX - mIndicatorIconX.toFloat()), (mTranslateY - mIndicatorIconY).toFloat())
            mIndicatorIcon!!.draw(canvas)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (mEnabled) {
            // 阻止父View去攔截onTouchEvent()事件，確保touch事件可以正確傳遞到此層View。
            this.parent.requestDisallowInterceptTouchEvent(true)
            when (event.action) {
                MotionEvent.ACTION_DOWN -> if (mOnSwagPointsChangeListener != null) mOnSwagPointsChangeListener!!.onStartTrackingTouch(
                    this
                )
                MotionEvent.ACTION_MOVE -> updateOnTouch(event)
                MotionEvent.ACTION_UP -> {
                    if (mOnSwagPointsChangeListener != null) mOnSwagPointsChangeListener!!.onStopTrackingTouch(
                        this
                    )
                    isPressed = false
                    this.parent.requestDisallowInterceptTouchEvent(false)
                }
                MotionEvent.ACTION_CANCEL -> {
                    if (mOnSwagPointsChangeListener != null) mOnSwagPointsChangeListener!!.onStopTrackingTouch(
                        this
                    )
                    isPressed = false
                    this.parent.requestDisallowInterceptTouchEvent(false)
                }
            }
            return true
        }
        return false
    }

    override fun drawableStateChanged() {
        super.drawableStateChanged()
        if (mIndicatorIcon != null && mIndicatorIcon!!.isStateful) {
            val state = drawableState
            mIndicatorIcon!!.state = state
        }
        invalidate()
    }

    /**
     * Update all the UI components on touch events.
     *
     * @param event MotionEvent
     */
    private fun updateOnTouch(event: MotionEvent) {
        isPressed = true
        mTouchAngle = convertTouchEventPointToAngle(event.x, event.y)
        val progress = convertAngleToProgress(mTouchAngle)
        updateProgress(progress, true)
    }

    private fun convertTouchEventPointToAngle(xPos: Float, yPos: Float): Double {
        // transform touch coordinate into component coordinate
        var x = xPos - mTranslateX
        val y = yPos - mTranslateY
        x = if (mClockwise) x else -x
        var angle = Math.toDegrees(atan2(y.toDouble(), x.toDouble()) + Math.PI / 2)
        angle = if (angle < 0) angle + 360 else angle
        //		System.out.printf("(%f, %f) %f\n", x, y, angle);
        return angle
    }

    private fun convertAngleToProgress(angle: Double): Int {
        return (valuePerDegree() * angle).roundToInt()
    }

    private fun valuePerDegree(): Float {
        return mMax.toFloat() / 360.0f
    }

    private fun updateIndicatorIconPosition() {
        val thumbAngle = (mProgressSweep + 90).toInt()
        mIndicatorIconX = (mArcRadius * cos(Math.toRadians(thumbAngle.toDouble()))).toInt()
        mIndicatorIconY = (mArcRadius * sin(Math.toRadians(thumbAngle.toDouble()))).toInt()
    }

    private fun updateProgress(progress: Int, fromUser: Boolean) {

        // detect points change closed to max or min
        var progress = progress
        val maxDetectValue = (mMax.toDouble() * 0.95).toInt()
        val minDetectValue = (mMax.toDouble() * 0.05).toInt() + mMin
        //		System.out.printf("(%d, %d) / (%d, %d)\n", mMax, mMin, maxDetectValue, minDetectValue);
        mUpdateTimes++
        if (progress == INVALID_VALUE) {
            return
        }

        // avoid accidentally touch to become max from original point
        // 避免在靠近原點點到直接變成最大值
        if (progress > maxDetectValue && mPreviousProgress == INVALID_VALUE.toFloat()) {
//			System.out.printf("Skip (%d) %.0f -> %.0f %s\n",
//					progress, mPreviousProgress, mCurrentProgress, isMax ? "Max" : "");
            return
        }


        // record previous and current progress change
        // 紀錄目前和前一個進度變化
        if (mUpdateTimes == 1) {
            mCurrentProgress = progress.toFloat()
        } else {
            mPreviousProgress = mCurrentProgress
            mCurrentProgress = progress.toFloat()
        }

//		if (mPreviousProgress != mCurrentProgress)
//			System.out.printf("Progress (%d)(%f) %.0f -> %.0f (%s, %s)\n",
//					progress, mTouchAngle,
//					mPreviousProgress, mCurrentProgress,
//					isMax ? "Max" : "",
//					isMin ? "Min" : "");

        // 不能直接拿progress來做step
        mPoints = progress - progress % mStep
        /**
         * Determine whether reach max or min to lock point update event.
         *
         * When reaching max, the progress will drop from max (or maxDetectPoints ~ max
         * to min (or min ~ minDetectPoints) and vice versa.
         *
         * If reach max or min, stop increasing / decreasing to avoid exceeding the max / min.
         */
        // 判斷超過最大值或最小值，最大最小值不重複判斷
        // 用數值範圍判斷預防轉太快直接略過最大最小值。
        // progress變化可能從98 -> 0/1 or 0/1 -> 98/97，而不會過0或100
        if (mUpdateTimes > 1 && !isMin && !isMax) {
            if (mPreviousProgress >= maxDetectValue && mCurrentProgress <= minDetectValue && mPreviousProgress > mCurrentProgress) {
                isMax = true
                progress = mMax
                mPoints = mMax
                //				System.out.println("Reach Max " + progress);
                if (mOnSwagPointsChangeListener != null) {
                    mOnSwagPointsChangeListener!!
                        .onPointsChanged(this, progress, fromUser)
                    return
                }
            } else if (mCurrentProgress >= maxDetectValue && mPreviousProgress <= minDetectValue && mCurrentProgress > mPreviousProgress || mCurrentProgress <= mMin) {
                isMin = true
                progress = mMin
                mPoints = mMin
                //				Log.d("Reach", "Reach Min " + progress);
                if (mOnSwagPointsChangeListener != null) {
                    mOnSwagPointsChangeListener!!
                        .onPointsChanged(this, progress, fromUser)
                    return
                }
            }
            invalidate()
        } else {

            // Detect whether decreasing from max or increasing from min, to unlock the update event.
            // Make sure to check in detect range only.
            if (isMax and (mCurrentProgress < mPreviousProgress) && mCurrentProgress >= maxDetectValue) {
//				System.out.println("Unlock max");
                isMax = false
            }
            if (isMin
                && mPreviousProgress < mCurrentProgress
                && mPreviousProgress <= minDetectValue && mCurrentProgress <= minDetectValue && mPoints >= mMin
            ) {
//				Log.d("Unlock", String.format("Unlock min %.0f, %.0f\n", mPreviousProgress, mCurrentProgress));
                isMin = false
            }
        }
        if (!isMax && !isMin) {
            progress = if (progress > mMax) mMax else progress
            progress = if (progress < mMin) mMin else progress
            if (mOnSwagPointsChangeListener != null) {
                progress = progress - progress % mStep
                mOnSwagPointsChangeListener!!
                    .onPointsChanged(this, progress, fromUser)
            }
            mProgressSweep = progress.toFloat() / valuePerDegree()
            //			if (mPreviousProgress != mCurrentProgress)
//				System.out.printf("-- %d, %d, %f\n", progress, mPoints, mProgressSweep);
            updateIndicatorIconPosition()
            invalidate()
        }
    }

    interface OnSwagPointsChangeListener {
        /**
         * Notification that the point value has changed.
         *
         * @param swagPoints The SwagPoints view whose value has changed
         * @param points     The current point value.
         * @param fromUser   True if the point change was triggered by the user.
         */
        fun onPointsChanged(swagPoints: SwagPointsCopy?, points: Int, fromUser: Boolean)
        fun onStartTrackingTouch(swagPoints: SwagPointsCopy?)
        fun onStopTrackingTouch(swagPoints: SwagPointsCopy?)
    }

    fun setPoints(points: Int) {
        var points = points
        points = if (points > mMax) mMax else points
        points = if (points < mMin) mMin else points
        updateProgress(points, false)
    }

    fun getPoints(): Int {
        return mPoints
    }

    fun getProgressWidth(): Float {
        return mProgressWidth
    }

    fun setProgressWidth(mProgressWidth: Float) {
        this.mProgressWidth = mProgressWidth
        mProgressPaint.strokeWidth = mProgressWidth
    }

    fun getArcWidth(): Float {
        return mArcWidth
    }

    fun setArcWidth(mArcWidth: Float) {
        this.mArcWidth = mArcWidth
        mArcPaint.strokeWidth = mArcWidth
    }

    fun setClockwise(isClockwise: Boolean) {
        mClockwise = isClockwise
    }

    fun isClockwise(): Boolean {
        return mClockwise
    }

    override fun isEnabled(): Boolean {
        return mEnabled
    }

    override fun setEnabled(enabled: Boolean) {
        mEnabled = enabled
    }

    fun getProgressColor(): Int {
        return mProgressPaint.color
    }

    fun setProgressColor(color: Int) {
        mProgressPaint.color = color
        invalidate()
    }

    fun getArcColor(): Int {
        return mArcPaint.color
    }

    fun setArcColor(color: Int) {
        mArcPaint.color = color
        invalidate()
    }

    fun setTextColor(textColor: Int) {
        mTextPaint.color = textColor
        invalidate()
    }

    fun setTextSize(textSize: Float) {
        mTextSize = textSize
        mTextPaint.textSize = mTextSize
        invalidate()
    }

    fun getMax(): Int {
        return mMax
    }

    fun setMax(mMax: Int) {
        require(mMax > mMin) { "Max should not be less than min." }
        this.mMax = mMax
    }

    fun getMin(): Int {
        return mMin
    }

    fun setMin(min: Int) {
        require(mMax > mMin) { "Min should not be greater than max." }
        mMin = min
    }

    fun getStep(): Int {
        return mStep
    }

    fun setStep(step: Int) {
        mStep = step
    }

    fun setOnSwagPointsChangeListener(onSwagPointsChangeListener: OnSwagPointsChangeListener?) {
        mOnSwagPointsChangeListener = onSwagPointsChangeListener
    }
}