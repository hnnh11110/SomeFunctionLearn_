package com.app.func.view.seekbarcustom.crollerTest

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import com.app.func.R
import com.app.func.view.seekbarcustom.crollerTest.utilities.Utils.convertDpToPixel
import com.app.func.view.seekbarcustom.crollerTest.utilities.Utils.getDistance
import kotlin.math.*

class Croller @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var midx = 0f
    private var midy = 0f
    private var textPaint: Paint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        isFakeBoldText = true
        textAlign = Paint.Align.CENTER
        textSize = labelSize
    }
    private var circlePaint: Paint = Paint().apply {
        isAntiAlias = true
        strokeWidth = progressSecondaryStrokeWidth
        style = Paint.Style.FILL
    }

    private var circlePaint2: Paint = Paint().apply {
        isAntiAlias = true
        strokeWidth = progressPrimaryStrokeWidth
        style = Paint.Style.FILL
    }
    private var linePaint: Paint = Paint().apply {
        isAntiAlias = true
        strokeWidth = indicatorWidth
    }

    private var currdeg = 0f
    private var deg = 3f
    private var downdeg = 0f
    private var isContinuous = false
    private var backCircleColor = Color.parseColor("#222222")
    private var mainCircleColor = Color.parseColor("#000000")
    private var indicatorColor = Color.parseColor("#FFA036")
    private var progressPrimaryColor = Color.parseColor("#FFA036")
    private var progressSecondaryColor = Color.parseColor("#111111")
    private var backCircleDisabledColor = Color.parseColor("#82222222")
    private var mainCircleDisabledColor = Color.parseColor("#82000000")
    private var indicatorDisabledColor = Color.parseColor("#82FFA036")
    private var progressPrimaryDisabledColor = Color.parseColor("#82FFA036")
    private var progressSecondaryDisabledColor = Color.parseColor("#82111111")
    private var progressPrimaryCircleSize = -1f
    private var progressSecondaryCircleSize = -1f
    private var progressPrimaryStrokeWidth = 25f
    private var progressSecondaryStrokeWidth = 10f
    private var mainCircleRadius = -1f
    private var backCircleRadius = -1f
    private var progressRadius = -1f
    private var max = 25
    private var min = 1
    private var indicatorWidth = 7f
    private var label: String? = "Label"
    private var labelFont: String? = null
        set(labelFont) {
            field = labelFont
            generateTypeface()
            invalidate()
        }
    private var labelStyle = 0
        set(labelStyle) {
            field = labelStyle
            invalidate()
        }
    private var labelSize = 14f
    private var labelColor = Color.WHITE
    private var labelDisabledColor = Color.BLACK
    private var startOffset = 30
    private var startOffset2 = 0
    private var sweepAngle = -1
    private var isEnabled = true
    private var isAntiClockwise = false
    private var startEventSent = false
    private var oval: RectF = RectF()
    private var mProgressChangeListener: OnProgressChangedListener? = null
    private var mCrollerChangeListener: OnCrollerChangeListener? = null

    fun setOnProgressChangedListener(mProgressChangeListener: OnProgressChangedListener?) {
        this.mProgressChangeListener = mProgressChangeListener
    }

    fun setOnCrollerChangeListener(mCrollerChangeListener: OnCrollerChangeListener?) {
        this.mCrollerChangeListener = mCrollerChangeListener
    }

    init {
        initXMLAttrs(context = context, attrs = attrs)
        generateTypeface()
        if (isEnabled) {
            circlePaint2.color = progressPrimaryColor
            circlePaint.color = progressSecondaryColor
            linePaint.color = indicatorColor
            textPaint.color = labelColor
        } else {
            circlePaint2.color = progressPrimaryDisabledColor
            circlePaint.color = progressSecondaryDisabledColor
            linePaint.color = indicatorDisabledColor
            textPaint.color = labelDisabledColor
        }
    }

    private fun generateTypeface() {
        var plainLabel = Typeface.DEFAULT
        if (labelFont != null && labelFont?.isNotEmpty() == true) {
            val assetMgr = context.assets
            plainLabel = Typeface.createFromAsset(assetMgr, labelFont)
        }
        when (labelStyle) {
            0 -> textPaint.typeface = plainLabel
            1 -> textPaint.typeface = Typeface.create(plainLabel, Typeface.BOLD)
            2 -> textPaint.typeface = Typeface.create(plainLabel, Typeface.ITALIC)
            3 -> textPaint.typeface = Typeface.create(plainLabel, Typeface.BOLD_ITALIC)
        }
    }

    private fun initXMLAttrs(context: Context, attrs: AttributeSet?) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.Croller)
        setEnabled(a.getBoolean(R.styleable.Croller_enabled_c, true))
        progress = a.getInt(R.styleable.Croller_start_progress, 1)
        setLabel(a.getString(R.styleable.Croller_label))
        setBackCircleColor(a.getColor(R.styleable.Croller_back_circle_color, backCircleColor))
        setMainCircleColor(a.getColor(R.styleable.Croller_main_circle_color, mainCircleColor))
        setIndicatorColor(a.getColor(R.styleable.Croller_indicator_color, indicatorColor))
        setProgressPrimaryColor(
            a.getColor(R.styleable.Croller_progress_primary_color, progressPrimaryColor)
        )
        setProgressSecondaryColor(
            a.getColor(R.styleable.Croller_progress_secondary_color, progressSecondaryColor)
        )
        setBackCircleDisabledColor(
            a.getColor(R.styleable.Croller_back_circle_disable_color, backCircleDisabledColor)
        )
        setMainCircleDisabledColor(
            a.getColor(R.styleable.Croller_main_circle_disable_color, mainCircleDisabledColor)
        )
        setIndicatorDisabledColor(
            a.getColor(R.styleable.Croller_indicator_disable_color, indicatorDisabledColor)
        )
        setProgressPrimaryDisabledColor(
            a.getColor(
                R.styleable.Croller_progress_primary_disable_color,
                progressPrimaryDisabledColor
            )
        )
        setProgressSecondaryDisabledColor(
            a.getColor(
                R.styleable.Croller_progress_secondary_disable_color, progressSecondaryDisabledColor
            )
        )
        setLabelSize(
            a.getDimension(
                R.styleable.Croller_label_size, TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    labelSize, resources.displayMetrics
                )
            )
        )
        setLabelColor(a.getColor(R.styleable.Croller_label_color, labelColor))
        setlabelDisabledColor(
            a.getColor(R.styleable.Croller_label_disabled_color, labelDisabledColor)
        )
        labelFont = a.getString(R.styleable.Croller_label_font)
        labelStyle = a.getInt(R.styleable.Croller_label_style, 0)
        setIndicatorWidth(a.getFloat(R.styleable.Croller_indicator_width, 7f))
        setIsContinuous(a.getBoolean(R.styleable.Croller_is_continuous, false))
        setProgressPrimaryCircleSize(
            a.getFloat(R.styleable.Croller_progress_primary_circle_size, -1f)
        )
        setProgressSecondaryCircleSize(
            a.getFloat(R.styleable.Croller_progress_secondary_circle_size, -1f)
        )
        setProgressPrimaryStrokeWidth(
            a.getFloat(R.styleable.Croller_progress_primary_stroke_width, 25f)
        )
        setProgressSecondaryStrokeWidth(
            a.getFloat(R.styleable.Croller_progress_secondary_stroke_width, 10f)
        )
        setSweepAngle(a.getInt(R.styleable.Croller_sweep_angle, -1))
        setStartOffset(a.getInt(R.styleable.Croller_start_offset, 30))
        setMax(a.getInt(R.styleable.Croller_max_c, 25))
        setMin(a.getInt(R.styleable.Croller_min_c, 1))
        deg = (min + 2).toFloat()
        setBackCircleRadius(a.getFloat(R.styleable.Croller_back_circle_radius, -1f))
        setProgressRadius(a.getFloat(R.styleable.Croller_progress_radius, -1f))
        setAntiClockwise(a.getBoolean(R.styleable.Croller_anticlockwise, false))
        a.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val minWidth = convertDpToPixel(160f, context).toInt()
        val minHeight = convertDpToPixel(160f, context).toInt()
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        var width = when (widthMode) {
            MeasureSpec.EXACTLY -> {
                widthSize
            }
            MeasureSpec.AT_MOST -> {
                min(minWidth, widthSize)
            }
            else -> {
                // only in case of ScrollViews, otherwise MeasureSpec.UNSPECIFIED is never triggered
                // If width is wrap_content i.e. MeasureSpec.UNSPECIFIED, then make width equal to height
                heightSize
            }
        }
        var height = when (heightMode) {
            MeasureSpec.EXACTLY -> {
                heightSize
            }
            MeasureSpec.AT_MOST -> {
                min(minHeight, heightSize)
            }
            else -> {
                // only in case of ScrollViews, otherwise MeasureSpec.UNSPECIFIED is never triggered
                // If height is wrap_content i.e. MeasureSpec.UNSPECIFIED, then make height equal to width
                widthSize
            }
        }
        if (widthMode == MeasureSpec.UNSPECIFIED && heightMode == MeasureSpec.UNSPECIFIED) {
            width = minWidth
            height = minHeight
        }
        setMeasuredDimension(width, height)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        midx = (width / 2).toFloat()
        midy = (height / 2).toFloat()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (mProgressChangeListener != null) {
            mProgressChangeListener?.onProgressChanged((deg - 2).toInt())
        }
        if (mCrollerChangeListener != null) {
            mCrollerChangeListener?.onProgressChanged(this, (deg - 2).toInt())
        }
        if (isEnabled) {
            circlePaint2.color = progressPrimaryColor
            circlePaint.color = progressSecondaryColor
            linePaint.color = indicatorColor
            textPaint.color = labelColor
        } else {
            circlePaint2.color = progressPrimaryDisabledColor
            circlePaint.color = progressSecondaryDisabledColor
            linePaint.color = indicatorDisabledColor
            textPaint.color = labelDisabledColor
        }
        if (!isContinuous) {
            startOffset2 = startOffset - 15
            linePaint.strokeWidth = indicatorWidth
            textPaint.textSize = labelSize
            val radius = (min(midx, midy) * (14.5.toFloat() / 16)).toInt()
            if (sweepAngle == -1) {
                sweepAngle = 360 - 2 * startOffset2
            }
            if (mainCircleRadius == -1f) {
                mainCircleRadius = radius * (11.toFloat() / 15)
            }
            if (backCircleRadius == -1f) {
                backCircleRadius = radius * (13.toFloat() / 15)
            }
            if (progressRadius == -1f) {
                progressRadius = radius.toFloat()
            }
            var x: Float
            var y: Float
            val deg2 = max(3f, deg)
            val deg3 = min(deg, (max + 2).toFloat())
            for (i in deg2.toInt() until max + 3) {
                var tmp =
                    startOffset2.toFloat() / 360 + sweepAngle.toFloat() / 360 * i.toFloat() / (max + 5)
                if (isAntiClockwise) {
                    tmp = 1.0f - tmp
                }
                x = midx + (progressRadius * sin(2 * Math.PI * (1.0 - tmp))).toFloat()
                y = midy + (progressRadius * cos(2 * Math.PI * (1.0 - tmp))).toFloat()
                if (progressSecondaryCircleSize == -1f) canvas.drawCircle(
                    x,
                    y,
                    radius.toFloat() / 30 * (20.toFloat() / max) * (sweepAngle.toFloat() / 270),
                    circlePaint
                ) else canvas.drawCircle(x, y, progressSecondaryCircleSize, circlePaint)
            }
            var i = 3
            while (i <= deg3) {
                var tmp =
                    startOffset2.toFloat() / 360 + sweepAngle.toFloat() / 360 * i.toFloat() / (max + 5)
                if (isAntiClockwise) {
                    tmp = 1.0f - tmp
                }
                x = midx + (progressRadius * sin(2 * Math.PI * (1.0 - tmp))).toFloat()
                y = midy + (progressRadius * cos(2 * Math.PI * (1.0 - tmp))).toFloat()
                if (progressPrimaryCircleSize == -1f) canvas.drawCircle(
                    x,
                    y,
                    progressRadius / 15 * (20.toFloat() / max) * (sweepAngle.toFloat() / 270),
                    circlePaint2
                ) else canvas.drawCircle(x, y, progressPrimaryCircleSize, circlePaint2)
                i++
            }
            var tmp2 = startOffset2.toFloat() / 360 + sweepAngle.toFloat() / 360 * deg / (max + 5)
            if (isAntiClockwise) {
                tmp2 = 1.0f - tmp2
            }
            val x1 =
                midx + (radius * (1f / 5) * sin(2 * Math.PI * (1.0 - tmp2))).toFloat()
            val y1 =
                midy + (radius * (1f / 5) * cos(2 * Math.PI * (1.0 - tmp2))).toFloat()
            val x2 =
                midx + (radius * (4f / 5) * sin(2 * Math.PI * (1.0 - tmp2))).toFloat()
            val y2 =
                midy + (radius * (4f / 5) * cos(2 * Math.PI * (1.0 - tmp2))).toFloat()
            if (isEnabled) circlePaint.color = backCircleColor else circlePaint.color =
                backCircleDisabledColor
            canvas.drawCircle(midx, midy, backCircleRadius, circlePaint)
            if (isEnabled) circlePaint.color = mainCircleColor else circlePaint.color =
                mainCircleDisabledColor
            canvas.drawCircle(midx, midy, mainCircleRadius, circlePaint)
            label?.let {
                canvas.drawText(
                    it,
                    midx,
                    midy + (radius * 1.1).toFloat() - textPaint.fontMetrics.descent,
                    textPaint
                )
            }
            canvas.drawLine(x1, y1, x2, y2, linePaint)
        } else {
            val radius = (min(midx, midy) * (14.5.toFloat() / 16)).toInt()
            val deg3 = min(deg, (max + 2).toFloat())
            if (sweepAngle == -1) {
                sweepAngle = 360 - 2 * startOffset
            }
            if (mainCircleRadius == -1f) {
                mainCircleRadius = radius * (11.toFloat() / 15)
            }
            if (backCircleRadius == -1f) {
                backCircleRadius = radius * (13.toFloat() / 15)
            }
            if (progressRadius == -1f) {
                progressRadius = radius.toFloat()
            }
            circlePaint.strokeWidth = progressSecondaryStrokeWidth
            circlePaint.style = Paint.Style.STROKE
            circlePaint2.strokeWidth = progressPrimaryStrokeWidth
            circlePaint2.style = Paint.Style.STROKE
            linePaint.strokeWidth = indicatorWidth
            textPaint.textSize = labelSize
            oval[midx - progressRadius, midy - progressRadius, midx + progressRadius] =
                midy + progressRadius
            canvas.drawArc(
                oval,
                90.toFloat() + startOffset,
                sweepAngle.toFloat(),
                false,
                circlePaint
            )
            if (isAntiClockwise) {
                canvas.drawArc(
                    oval,
                    90.toFloat() - startOffset,
                    -1 * ((deg3 - 2) * (sweepAngle.toFloat() / max)),
                    false,
                    circlePaint2
                )
            } else {
                canvas.drawArc(
                    oval,
                    90.toFloat() + startOffset,
                    (deg3 - 2) * (sweepAngle.toFloat() / max),
                    false,
                    circlePaint2
                )
            }
            var tmp2 = startOffset.toFloat() / 360 + sweepAngle.toFloat() / 360 * ((deg - 2) / max)
            if (isAntiClockwise) {
                tmp2 = 1.0f - tmp2
            }
            val x1 =
                midx + (radius * (1f / 5) * sin(2 * Math.PI * (1.0 - tmp2))).toFloat()
            val y1 =
                midy + (radius * (1f / 5) * cos(2 * Math.PI * (1.0 - tmp2))).toFloat()
            val x2 =
                midx + (radius * (4f / 5) * sin(2 * Math.PI * (1.0 - tmp2))).toFloat()
            val y2 =
                midy + (radius * (4f / 5) * cos(2 * Math.PI * (1.0 - tmp2))).toFloat()
            circlePaint.style = Paint.Style.FILL
            if (isEnabled) circlePaint.color = backCircleColor else circlePaint.color =
                backCircleDisabledColor
            canvas.drawCircle(midx, midy, backCircleRadius, circlePaint)
            if (isEnabled) circlePaint.color = mainCircleColor else circlePaint.color =
                mainCircleDisabledColor
            canvas.drawCircle(midx, midy, mainCircleRadius, circlePaint)
            label?.let {
                canvas.drawText(
                    it,
                    midx,
                    midy + (radius * 1.1).toFloat() - textPaint.fontMetrics.descent,
                    textPaint
                )
            }
            canvas.drawLine(x1, y1, x2, y2, linePaint)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(e: MotionEvent): Boolean {
        if (!isEnabled) {
            return false
        }
        if (getDistance(e.x, e.y, midx, midy) > max(
                mainCircleRadius,
                max(backCircleRadius, progressRadius)
            )
        ) {
            if (startEventSent && mCrollerChangeListener != null) {
                mCrollerChangeListener?.onStopTrackingTouch(this)
                startEventSent = false
            }
            return super.onTouchEvent(e)
        }
        if (e.action == MotionEvent.ACTION_DOWN) {
            val dx = e.x - midx
            val dy = e.y - midy
            downdeg = (atan2(dy.toDouble(), dx.toDouble()) * 180 / Math.PI).toFloat()
            downdeg -= 90f
            if (downdeg < 0) {
                downdeg += 360f
            }
            downdeg = floor((downdeg / 360 * (max + 5)).toDouble()).toFloat()
            if (mCrollerChangeListener != null) {
                mCrollerChangeListener?.onStartTrackingTouch(this)
                startEventSent = true
            }
            return true
        }
        if (e.action == MotionEvent.ACTION_MOVE) {
            val dx = e.x - midx
            val dy = e.y - midy
            currdeg = (atan2(dy.toDouble(), dx.toDouble()) * 180 / Math.PI).toFloat()
            currdeg -= 90f
            if (currdeg < 0) {
                currdeg += 360f
            }
            currdeg = floor((currdeg / 360 * (max + 5)).toDouble()).toFloat()
            if (currdeg / (max + 4) > 0.75f && (downdeg - 0) / (max + 4) < 0.25f) {
                if (isAntiClockwise) {
                    deg++
                    if (deg > max + 2) {
                        deg = (max + 2).toFloat()
                    }
                } else {
                    deg--
                    if (deg < min + 2) {
                        deg = (min + 2).toFloat()
                    }
                }
            } else if (downdeg / (max + 4) > 0.75f && (currdeg - 0) / (max + 4) < 0.25f) {
                if (isAntiClockwise) {
                    deg--
                    if (deg < min + 2) {
                        deg = (min + 2).toFloat()
                    }
                } else {
                    deg++
                    if (deg > max + 2) {
                        deg = (max + 2).toFloat()
                    }
                }
            } else {
                if (isAntiClockwise) {
                    deg -= currdeg - downdeg
                } else {
                    deg += currdeg - downdeg
                }
                if (deg > max + 2) {
                    deg = (max + 2).toFloat()
                }
                if (deg < min + 2) {
                    deg = (min + 2).toFloat()
                }
            }
            downdeg = currdeg
            invalidate()
            return true
        }
        if (e.action == MotionEvent.ACTION_UP) {
            if (mCrollerChangeListener != null) {
                mCrollerChangeListener?.onStopTrackingTouch(this)
                startEventSent = false
            }
            return true
        }
        return super.onTouchEvent(e)
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (parent != null && event.action == MotionEvent.ACTION_DOWN) {
            parent.requestDisallowInterceptTouchEvent(true)
        }
        return super.dispatchTouchEvent(event)
    }

    override fun isEnabled(): Boolean {
        return isEnabled
    }

    override fun setEnabled(enabled: Boolean) {
        isEnabled = enabled
        invalidate()
    }

    var progress: Int
        get() = (deg - 2).toInt()
        set(x) {
            deg = (x + 2).toFloat()
            invalidate()
        }

    fun getLabel(): String? {
        return label
    }

    fun setLabel(txt: String?) {
        label = txt
        invalidate()
    }

    fun getBackCircleColor(): Int {
        return backCircleColor
    }

    fun setBackCircleColor(backCircleColor: Int) {
        this.backCircleColor = backCircleColor
        invalidate()
    }

    fun getMainCircleColor(): Int {
        return mainCircleColor
    }

    fun setMainCircleColor(mainCircleColor: Int) {
        this.mainCircleColor = mainCircleColor
        invalidate()
    }

    fun getIndicatorColor(): Int {
        return indicatorColor
    }

    fun setIndicatorColor(indicatorColor: Int) {
        this.indicatorColor = indicatorColor
        invalidate()
    }

    fun getProgressPrimaryColor(): Int {
        return progressPrimaryColor
    }

    fun setProgressPrimaryColor(progressPrimaryColor: Int) {
        this.progressPrimaryColor = progressPrimaryColor
        invalidate()
    }

    fun getProgressSecondaryColor(): Int {
        return progressSecondaryColor
    }

    fun setProgressSecondaryColor(progressSecondaryColor: Int) {
        this.progressSecondaryColor = progressSecondaryColor
        invalidate()
    }

    fun getBackCircleDisabledColor(): Int {
        return backCircleDisabledColor
    }

    fun setBackCircleDisabledColor(backCircleDisabledColor: Int) {
        this.backCircleDisabledColor = backCircleDisabledColor
        invalidate()
    }

    fun getMainCircleDisabledColor(): Int {
        return mainCircleDisabledColor
    }

    fun setMainCircleDisabledColor(mainCircleDisabledColor: Int) {
        this.mainCircleDisabledColor = mainCircleDisabledColor
        invalidate()
    }

    fun getIndicatorDisabledColor(): Int {
        return indicatorDisabledColor
    }

    fun setIndicatorDisabledColor(indicatorDisabledColor: Int) {
        this.indicatorDisabledColor = indicatorDisabledColor
        invalidate()
    }

    fun getProgressPrimaryDisabledColor(): Int {
        return progressPrimaryDisabledColor
    }

    fun setProgressPrimaryDisabledColor(progressPrimaryDisabledColor: Int) {
        this.progressPrimaryDisabledColor = progressPrimaryDisabledColor
        invalidate()
    }

    fun getProgressSecondaryDisabledColor(): Int {
        return progressSecondaryDisabledColor
    }

    fun setProgressSecondaryDisabledColor(progressSecondaryDisabledColor: Int) {
        this.progressSecondaryDisabledColor = progressSecondaryDisabledColor
        invalidate()
    }

    fun getLabelSize(): Float {
        return labelSize
    }

    fun setLabelSize(labelSize: Float) {
        this.labelSize = labelSize
        invalidate()
    }

    fun getLabelColor(): Int {
        return labelColor
    }

    fun setLabelColor(labelColor: Int) {
        this.labelColor = labelColor
        invalidate()
    }

    fun getlabelDisabledColor(): Int {
        return labelDisabledColor
    }

    fun setlabelDisabledColor(labelDisabledColor: Int) {
        this.labelDisabledColor = labelDisabledColor
        invalidate()
    }

    fun getIndicatorWidth(): Float {
        return indicatorWidth
    }

    fun setIndicatorWidth(indicatorWidth: Float) {
        this.indicatorWidth = indicatorWidth
        invalidate()
    }

    fun isContinuous(): Boolean {
        return isContinuous
    }

    fun setIsContinuous(isContinuous: Boolean) {
        this.isContinuous = isContinuous
        invalidate()
    }

    fun getProgressPrimaryCircleSize(): Float {
        return progressPrimaryCircleSize
    }

    fun setProgressPrimaryCircleSize(progressPrimaryCircleSize: Float) {
        this.progressPrimaryCircleSize = progressPrimaryCircleSize
        invalidate()
    }

    fun getProgressSecondaryCircleSize(): Float {
        return progressSecondaryCircleSize
    }

    fun setProgressSecondaryCircleSize(progressSecondaryCircleSize: Float) {
        this.progressSecondaryCircleSize = progressSecondaryCircleSize
        invalidate()
    }

    fun getProgressPrimaryStrokeWidth(): Float {
        return progressPrimaryStrokeWidth
    }

    fun setProgressPrimaryStrokeWidth(progressPrimaryStrokeWidth: Float) {
        this.progressPrimaryStrokeWidth = progressPrimaryStrokeWidth
        invalidate()
    }

    fun getProgressSecondaryStrokeWidth(): Float {
        return progressSecondaryStrokeWidth
    }

    fun setProgressSecondaryStrokeWidth(progressSecondaryStrokeWidth: Float) {
        this.progressSecondaryStrokeWidth = progressSecondaryStrokeWidth
        invalidate()
    }

    fun getSweepAngle(): Int {
        return sweepAngle
    }

    fun setSweepAngle(sweepAngle: Int) {
        this.sweepAngle = sweepAngle
        invalidate()
    }

    fun getStartOffset(): Int {
        return startOffset
    }

    fun setStartOffset(startOffset: Int) {
        this.startOffset = startOffset
        invalidate()
    }

    fun getMax(): Int {
        return max
    }

    fun setMax(max: Int) {
        if (max < min) {
            this.max = min
        } else {
            this.max = max
        }
        invalidate()
    }

    fun getMin(): Int {
        return min
    }

    fun setMin(min: Int) {
        when {
            min < 0 -> {
                this.min = 0
            }
            min > max -> {
                this.min = max
            }
            else -> {
                this.min = min
            }
        }
        invalidate()
    }

    fun getMainCircleRadius(): Float {
        return mainCircleRadius
    }

    fun setMainCircleRadius(mainCircleRadius: Float) {
        this.mainCircleRadius = mainCircleRadius
        invalidate()
    }

    fun getBackCircleRadius(): Float {
        return backCircleRadius
    }

    fun setBackCircleRadius(backCircleRadius: Float) {
        this.backCircleRadius = backCircleRadius
        invalidate()
    }

    fun getProgressRadius(): Float {
        return progressRadius
    }

    fun setProgressRadius(progressRadius: Float) {
        this.progressRadius = progressRadius
        invalidate()
    }

    fun isAntiClockwise(): Boolean {
        return isAntiClockwise
    }

    fun setAntiClockwise(antiClockwise: Boolean) {
        isAntiClockwise = antiClockwise
        invalidate()
    }
}