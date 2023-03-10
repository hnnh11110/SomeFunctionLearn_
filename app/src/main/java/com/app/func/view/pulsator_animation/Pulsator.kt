package com.app.func.view.pulsator_animation

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.animation.*
import android.widget.RelativeLayout
import com.app.func.R
import kotlin.math.min

/**
 * Created by booncol on 04.07.2016.
 *
 */
class Pulsator @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {
    private var mCount: Int
    private var mDuration: Int
    private var mRepeat: Int
    private var mStartFromScratch: Boolean
    private var mColor: Int
    private var mInterpolator: Int
    private val mViews: MutableList<View> = ArrayList()
    private var mAnimatorSet: AnimatorSet? = null
    private var mPaint: Paint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
    }
    private var mRadius = 0f
    private var mCenterX = 0f
    private var mCenterY = 0f
    private var mIsStarted = false

    /**
     * Start pulse animation.
     */
    @Synchronized
    fun start() {
        if (mAnimatorSet == null || mIsStarted) {
            return
        }
        mAnimatorSet?.start()
        if (!mStartFromScratch) {
            val animators = mAnimatorSet!!.childAnimations
            for (animator in animators) {
                val objectAnimator = animator as ObjectAnimator
                val delay = objectAnimator.startDelay
                objectAnimator.startDelay = 0
                objectAnimator.currentPlayTime = mDuration - delay
            }
        }
    }

    /**
     * Stop pulse animation.
     */
    @Synchronized
    fun stop() {
        if (mAnimatorSet == null || !mIsStarted) {
            return
        }
        mAnimatorSet?.end()
    }

    @get:Synchronized
    val isStarted: Boolean
        get() = mAnimatorSet != null && mIsStarted

    var count: Int
        get() = mCount
        set(count) {
            require(count >= 0) { "Count cannot be negative" }
            if (count != mCount) {
                mCount = count
                reset()
                invalidate()
            }
        }

    var duration: Int
        get() = mDuration
        set(millis) {
            require(millis >= 0) { "Duration cannot be negative" }
            if (millis != mDuration) {
                mDuration = millis
                reset()
                invalidate()
            }
        }

    var color: Int
        get() = mColor
        set(color) {
            if (color != mColor) {
                mColor = color
                mPaint.color = color
            }
        }

    var interpolator: Int
        get() = mInterpolator
        set(type) {
            if (type != mInterpolator) {
                mInterpolator = type
                reset()
                invalidate()
            }
        }

    public override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = MeasureSpec.getSize(widthMeasureSpec) - paddingLeft - paddingRight
        val height = MeasureSpec.getSize(heightMeasureSpec) - paddingTop - paddingBottom
        mCenterX = width * 0.5f
        mCenterY = height * 0.5f
        mRadius = min(width, height) * 0.5f
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    /**
     * Remove all views and animators.
     */
    private fun clear() {
        // remove animators
        stop()
        // remove old views
        for (view in mViews) {
            removeView(view)
        }
        mViews.clear()
    }

    /**
     * Build pulse views and animators.
     */
    private fun build() {
        // create views and animators
        val layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        val repeatCount = if (mRepeat == INFINITE) ObjectAnimator.INFINITE else mRepeat
        val animators: MutableList<Animator> = ArrayList()
        for (index in 0 until mCount) {
            // setup view
            val pulseView = PulseView(context)
            pulseView.scaleX = 0f
            pulseView.scaleY = 0f
            pulseView.alpha = 1f
            addView(pulseView, index, layoutParams)
            mViews.add(pulseView)
            val delay = index.toLong() * mDuration / mCount

            // setup animators
            val scaleXAnimator = ObjectAnimator.ofFloat(pulseView, "ScaleX", 0f, 1f)
            scaleXAnimator.repeatCount = repeatCount
            scaleXAnimator.repeatMode = ObjectAnimator.RESTART
            scaleXAnimator.startDelay = delay
            animators.add(scaleXAnimator)
            val scaleYAnimator = ObjectAnimator.ofFloat(pulseView, "ScaleY", 0f, 1f)
            scaleYAnimator.repeatCount = repeatCount
            scaleYAnimator.repeatMode = ObjectAnimator.RESTART
            scaleYAnimator.startDelay = delay
            animators.add(scaleYAnimator)
            val alphaAnimator = ObjectAnimator.ofFloat(pulseView, "Alpha", 1f, 0f)
            alphaAnimator.repeatCount = repeatCount
            alphaAnimator.repeatMode = ObjectAnimator.RESTART
            alphaAnimator.startDelay = delay
            animators.add(alphaAnimator)
        }
        mAnimatorSet = AnimatorSet()
        mAnimatorSet?.playTogether(animators)
        mAnimatorSet?.interpolator = createInterpolator(mInterpolator)
        mAnimatorSet?.duration = mDuration.toLong()
        mAnimatorSet?.addListener(mAnimatorListener)
    }

    /**
     * Reset views and animations.
     */
    private fun reset() {
        val isStarted = isStarted
        clear()
        build()
        if (isStarted) {
            start()
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        if (mAnimatorSet != null) {
            mAnimatorSet?.cancel()
            mAnimatorSet = null
        }
    }

    private inner class PulseView(context: Context?) : View(context) {
        override fun onDraw(canvas: Canvas) {
            canvas.drawCircle(mCenterX, mCenterY, mRadius, mPaint)
        }
    }

    private val mAnimatorListener: Animator.AnimatorListener = object : Animator.AnimatorListener {
        override fun onAnimationStart(animator: Animator) {
            mIsStarted = true
        }

        override fun onAnimationEnd(animator: Animator) {
            mIsStarted = false
        }

        override fun onAnimationCancel(animator: Animator) {
            mIsStarted = false
        }

        override fun onAnimationRepeat(animator: Animator) {}
    }

    companion object {
        const val INFINITE = 0
        const val INTERP_LINEAR = 0
        const val INTERP_ACCELERATE = 1
        const val INTERP_DECELERATE = 2
        const val INTERP_ACCELERATE_DECELERATE = 3
        private const val DEFAULT_COUNT = 4
        private val DEFAULT_COLOR = Color.rgb(0, 116, 193)
        private const val DEFAULT_DURATION = 7000
        private const val DEFAULT_REPEAT = INFINITE
        private const val DEFAULT_START_FROM_SCRATCH = true
        private const val DEFAULT_INTERPOLATOR = INTERP_LINEAR

        /**
         * Create interpolator from type.
         *
         * @param type Interpolator type as int
         * @return Interpolator object of type
         */
        private fun createInterpolator(type: Int): Interpolator {
            return when (type) {
                INTERP_ACCELERATE -> AccelerateInterpolator()
                INTERP_DECELERATE -> DecelerateInterpolator()
                INTERP_ACCELERATE_DECELERATE -> AccelerateDecelerateInterpolator()
                else -> LinearInterpolator()
            }
        }
    }

    init {
        // get attributes
        val attr = context.theme.obtainStyledAttributes(
            attrs, R.styleable.Pulsator, 0, 0
        )
        mCount = DEFAULT_COUNT
        mDuration = DEFAULT_DURATION
        mRepeat = DEFAULT_REPEAT
        mStartFromScratch = DEFAULT_START_FROM_SCRATCH
        mColor = DEFAULT_COLOR
        mInterpolator = DEFAULT_INTERPOLATOR
        try {
            mCount = attr.getInteger(R.styleable.Pulsator_pulse_count, DEFAULT_COUNT)
            mDuration = attr.getInteger(R.styleable.Pulsator_pulse_duration, DEFAULT_DURATION)
            mRepeat = attr.getInteger(R.styleable.Pulsator_pulse_repeat, DEFAULT_REPEAT)
            mStartFromScratch = attr.getBoolean(
                R.styleable.Pulsator_pulse_startFromScratch,
                DEFAULT_START_FROM_SCRATCH
            )
            mColor = attr.getColor(R.styleable.Pulsator_pulse_color, DEFAULT_COLOR)
            mInterpolator = attr.getInteger(
                R.styleable.Pulsator_pulse_interpolator,
                DEFAULT_INTERPOLATOR
            )
        } finally {
            attr.recycle()
        }

        mPaint.color = mColor
        build()
    }
}