package com.app.func.view.chart.stock

import android.animation.Animator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.RadialGradient
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.Shader
import android.graphics.SweepGradient
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.app.func.R
import com.app.func.utils.Utils.formatTemperature
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

class WaterTankTemperatureView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    private lateinit var textViewTemperature: TextView
    private lateinit var textViewHint: TextView
    private val rect: Rect = Rect()
    private val paintBorderBackground = Paint()
    private val paintBorder = Paint()
    private val paintText = Paint()
    private val paintCircle = Paint()
    private val paintCircleOutline = Paint()
    private val paintPointCurrent = Paint()
    private var colorFrom = Color.parseColor("#FEFEFE")

    //    private var colorMid = Color.parseColor("#FF7373")
//    private var colorTo = Color.parseColor("#D62020")
    private val colorCircleOutline = Color.parseColor("#33FFFFFF")
    private val colorCircleProgress = Color.parseColor("#F26522")
    private var colortext = Color.BLACK

    //    private var colorBackgroundBorder = Color.parseColor("#F26522")
//    private var gradientColors = intArrayOf(colorFrom, colorTo)
//    private var gradientPositions = floatArrayOf(0 / 360f, 360f / 360f)
    private var start = 0f
    private var angle = 0f
    private var oval: RectF = RectF()
    private var centerX: Float = 0f
    private var centerY: Float = 0f
    private var radius: Float = 0f
    private val progress = 66f
    private var textSizeTempOnProgressCurve = 0f
    private var mListValue = arrayListOf<String>()

    private var _currentAngle = 0f
    private var _newSweepAngle = 0f
    private var _targetSettingAngle = 0f

    private var minValue = 0
    private var maxValue = 0

    private var _state: State = State.STOP_WORKING
    private var _currentTemp: Int? = null
    private var _targetTemp: Int? = _currentTemp
    private var _inProgressSetting = false
    private var _animator: ValueAnimator? = null
    private var _runningState: RunningMode = RunningMode.IDLE
    private var strokeWidthBorder = 0f
    private var paddingTop = 0f
    private var paddingBottom = 0f
    private var paddingLeft = 0f
    private var paddingRight = 0f
    private var _radiusDotWhite = 0f
    private var _radiusShaderDotWhite = 0f
    private var widthScreen = 0
    private var _progressGradientColors = intArrayOf(
        Color.parseColor("#C7C7C7"),
        Color.parseColor("#B7B7B7"),
        Color.parseColor("#C7C7C7")
    )
    private var _progressGradientPositions = floatArrayOf(0f / 360f, 180f / 360f, 360f / 360f)

    init {
        widthScreen = Resources.getSystem().displayMetrics.widthPixels
        _radiusShaderDotWhite = context.resources.getDimension(R.dimen._10dp)
        initColorByState()
        initPaint()
        initText()
        setWillNotDraw(false)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val viewWidth = MeasureSpec.getSize(widthMeasureSpec)
        paddingLeft = viewWidth * RATIO_PADDING_LEFT
        paddingRight = viewWidth * RATIO_PADDING_RIGHT
        val viewHeight = viewWidth * RATIO_HEIGHT_WIDTH
        paddingTop = viewHeight * RATIO_PADDING_TOP
        paddingBottom = viewHeight * RATIO_PADDING_BOTTOM
        radius = (viewHeight - paddingTop - paddingBottom) / 2f
        strokeWidthBorder = viewHeight * RATIO_STROKE_WIDTH_HEIGHT
        _radiusDotWhite = viewHeight * RATIO_DOT_RADIUS_HEIGHT
        super.setMeasuredDimension(viewWidth, viewHeight.roundToInt())
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        initPaint()
        initShader()
        textViewTemperature.setTextSize(
            TypedValue.COMPLEX_UNIT_PX,
            height * RATIO_TEXT_SIZE_TEMP_CENTER_HEIGHT
        )
        textViewHint.setTextSize(
            TypedValue.COMPLEX_UNIT_PX,
            height * RATIO_TEXT_SIZE_HEIGHT
        )
        repositionTextViewsXCoordinates()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val width = width.toFloat()
        val height = height.toFloat()
        paintBorder.style = Paint.Style.STROKE
        paintBorder.isAntiAlias = true
        centerX = width / 2
        centerY = (height - paddingBottom) / 2 + paddingTop

        oval.set(centerX - radius, centerY - radius, centerX + radius, centerY + radius)

        val heightOfTextViews = textViewTemperature.height + textViewHint.height
        if (textViewHint.visibility == View.GONE) {
            textViewTemperature.y = (centerY - radius * RATIO_CIRCLE_IN) +
                    (radius * RATIO_CIRCLE_IN * 2 - textViewTemperature.height) / 2
        } else {
            textViewTemperature.y = (centerY - radius * RATIO_CIRCLE_IN) +
                    (radius * RATIO_CIRCLE_IN * 2 - heightOfTextViews) / 2

            textViewHint.y = textViewTemperature.y + textViewTemperature.height
        }
        canvas?.let {
            paintCircle.shader = null
            paintCircle.setShadowLayer(
                radius * 0.1f,
                radius * 0.05f,
                radius * 0.05f,
                ContextCompat.getColor(context, R.color.color_cac9c9)
            )
            setLayerType(LAYER_TYPE_NONE, paintCircle)
            drawCircleCenterOut(it)
            paintCircle.clearShadowLayer()
            initShader()
            drawCircleCenterOut(it)
            drawBorderBackground(it)
            drawNumeral(it)

            if (_runningState == RunningMode.REDUCE) {
                drawTextSettingTemp(it)
                drawDotCircle(formatStringTemp(_targetTemp!!), canvas)
            }
            if (_runningState == RunningMode.INCREASE) {
                drawBorder(it)
                drawTextSettingTemp(it)
                drawDotCircle(formatStringTemp(_targetTemp!!), canvas)
            }
        }
    }

    private fun repositionTextViewsXCoordinates() {
        if (width == 0) return
        textViewTemperature.x = width.toFloat() / 2 - textViewTemperature.width / 2
        textViewHint.x = width.toFloat() / 2 - textViewHint.width / 2
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        initShader()
        initValues()
    }

    fun getRunningState(): RunningMode {
        return _runningState
    }

    private fun initPaint() {
        paintCircleOutline.strokeWidth = strokeWidthBorder
        paintCircleOutline.style = Paint.Style.FILL_AND_STROKE
        paintCircleOutline.strokeCap = Paint.Cap.ROUND
        paintCircleOutline.color = colorCircleOutline
        paintCircleOutline.isAntiAlias = true

        paintCircle.strokeWidth = strokeWidthBorder
        paintCircle.style = Paint.Style.FILL_AND_STROKE
        paintCircle.strokeCap = Paint.Cap.ROUND
        paintCircle.color = colorFrom
        paintCircle.isAntiAlias = true

        paintBorder.strokeWidth = strokeWidthBorder
        paintBorder.style = Paint.Style.STROKE
        paintBorder.strokeCap = Paint.Cap.ROUND
        paintBorder.color = colorCircleProgress
        paintBorder.isAntiAlias = true

        paintBorderBackground.strokeWidth = strokeWidthBorder
        paintBorderBackground.shader = SweepGradient(
            500 / 2f, 500 / 2f, _progressGradientColors,
            _progressGradientPositions
        ).apply {
            val rotate = 90f
            val gradientMatrix = Matrix()
            gradientMatrix.preRotate(rotate, width / 2F, height / 2F)
            setLocalMatrix(gradientMatrix)
        }
        paintBorderBackground.style = Paint.Style.STROKE
        paintBorderBackground.strokeCap = Paint.Cap.ROUND
        paintBorderBackground.isAntiAlias = true

        textSizeTempOnProgressCurve = height * RATIO_TEXT_SIZE_HEIGHT
        val paintTypeFace = ResourcesCompat.getFont(context, R.font.roboto_medium)
        paintText.typeface = paintTypeFace
        paintText.strokeWidth = strokeWidthText
        paintText.color = colortext // default
        paintText.style = Paint.Style.FILL
        paintText.isAntiAlias = true
        paintText.textSize = textSizeTempOnProgressCurve

        paintPointCurrent.color = Color.WHITE
        paintPointCurrent.style = Paint.Style.FILL_AND_STROKE
        paintPointCurrent.strokeCap = Paint.Cap.ROUND
        paintPointCurrent.isAntiAlias = true
        val _shadowColor = Color.parseColor("#664F5979")
        paintPointCurrent.setShadowLayer(_radiusShaderDotWhite, 0f, 0f, _shadowColor)
        setLayerType(LAYER_TYPE_NONE, paintPointCurrent)
    }

    private fun initColorByState() {
        colortext = when (_state) {
            State.STOP_WORKING -> {
                ContextCompat.getColor(context, R.color.color_7A868D)
            }
            State.WORKING -> {
                Color.BLACK
            }
        }
    }

    private fun drawCircleCenterOut(canvas: Canvas) {
        canvas.drawCircle(centerX, centerY, radius * 0.75f, paintCircle)
    }

    private fun drawBorder(canvas: Canvas) {
        canvas.drawArc(oval, _currentAngle, _newSweepAngle, false, paintBorder)
    }

    private fun drawBorderBackground(canvas: Canvas) {
        val gradient: Shader = LinearGradient(
            width / 2f,
            0f,
            width / 2f,
            height.toFloat(),
            Color.parseColor("#20B7B7B7"),
            Color.parseColor("#C7C7C7"),
            Shader.TileMode.MIRROR
        )
        paintBorderBackground.shader = gradient
        canvas.drawArc(oval, start, angle, false, paintBorderBackground)
    }

    private fun drawNumeral(canvas: Canvas) {
        mListValue.clear()
        for (i in minValue..maxValue) {
            mListValue.add("${i.formatTemperature()}°C")
        }
        if (mListValue.isNotEmpty()) {
            drawMaxMinValue(canvas)
        }
    }

    private fun drawTextSettingTemp(canvas: Canvas) {
        drawTextTempSelected(
            temp = _targetTemp!!,
            canvas = canvas,
            Color.RED
        )
    }

    private fun drawMaxMinValue(canvas: Canvas) {
        drawTextTempSelected(maxValue, canvas, Color.BLACK)
        drawTextTempSelected(minValue, canvas, Color.BLACK)
    }

    private fun drawTextTempSelected(
        temp: Int,
        canvas: Canvas,
        color: Int
    ) {
        val item = formatStringTemp(temp)
        val position = mListValue.indexOf(item) // getPosition item
        val total = 240f // 2/3 circle
        val edge = ((total / (mListValue.size - 1))) * (position) // edge pos
        val angle = edge + 150f // angle now
        val radians = angle * (Math.PI / 180) // calc radians
        val radiusX = radius * 1.3
        val radiusY = radius * 1.15
        paintText.getTextBounds(item, 0, item.length, rect)
        val xText = width.toFloat() / 2 + cos(radians) * radiusX - rect.width().toFloat() / 2
        val yTEXT = if (angle in 210f..330f) {
            centerY + sin(radians) * radiusY - rect.height().toFloat() / 2
        } else {
            centerY + sin(radians) * radius + rect.height().toFloat() / 2
        }
        paintText.color = color
        canvas.drawText(item, xText.toFloat(), yTEXT.toFloat(), paintText)
    }

    private fun drawDotCircle(item: String, canvas: Canvas) {
        val position = mListValue.indexOf(item) // getPosition item
        val total = progress * 360 / 100 // 66f == 2/3 circle
        val edge = ((total / (mListValue.size)) * 1.05) * position // edge pos
        val angle = edge + 150f // angle now
        val radians = angle * (Math.PI / 180) // calc radians

        val xCircle = (width / 2 + cos(radians) * radius).toInt()
        val yCircle = (centerY + sin(radians) * radius).toInt()

        canvas.drawCircle(xCircle.toFloat(), yCircle.toFloat(), _radiusDotWhite, paintPointCurrent)
    }

    private fun calculateAngle(temp: Int): Float {
        val values = formatStringTemp(temp)
        val position = mListValue.indexOf(values) // getPosition item
        val total = progress * 360 / 100 // 66f == 2/3 circle
        val edge = ((total / (mListValue.size)) * 1.05) * position // edge pos
        val angle = edge + 150f // angle now
        return angle.toFloat()
    }

    fun setMinMaxProgress(minValue: Int, maxValue: Int) {
        this.minValue = minValue
        this.maxValue = maxValue
        mListValue.clear()
        for (i in minValue..maxValue) {
            mListValue.add("${i.formatTemperature()}°C")
        }
        invalidate()
    }

    private fun initValues() {
        angle = progress * 360 / 100 // total
        val remain = 360 - angle
        val startAngle = remain / 2
        start = 90f + startAngle
    }

    private fun initShader() {
        if (width <= 0 || height <= 0) return
        val shaderCircle = RadialGradient(
            (width * 0.56).toFloat(), (height * 0.64).toFloat(), (height * 0.55).toFloat(),
            intArrayOf(
                ContextCompat.getColor(context, R.color.white),
                ContextCompat.getColor(context, R.color.white),
                ContextCompat.getColor(context, R.color.white),
                ContextCompat.getColor(context, R.color.white90),
                ContextCompat.getColor(context, R.color.white80),
                ContextCompat.getColor(context, R.color.color_cac9c9),
            ),
            floatArrayOf(0.0f, 0.2f, 0.4f, 0.5f, 0.55f, 1f),
            Shader.TileMode.CLAMP
        )
        paintCircle.shader = shaderCircle
    }

    @SuppressLint("ResourceType")
    private fun initText() {
        textViewTemperature = TextView(context)
        val layoutParamsTitle = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        val layoutParamsDescription =
            LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        layoutParamsTitle.addRule(CENTER_HORIZONTAL)
        textViewTemperature.layoutParams = layoutParamsTitle
        val typeface = ResourcesCompat.getFont(context, R.font.roboto_bold)
        textViewTemperature.typeface = typeface
        textViewTemperature.id = 1
        textViewTemperature.textSize = height * RATIO_TEXT_SIZE_TEMP_CENTER_HEIGHT
        textViewTemperature.includeFontPadding = false
        textViewTemperature.setTextColor(ContextCompat.getColor(context, R.color.color_7A868D))
        textViewTemperature.gravity = Gravity.CENTER
        val tempString = if (_currentTemp == null) {
            "--°C"
        } else formatStringTemp(_currentTemp!!)
        textViewTemperature.text = tempString

        textViewHint = TextView(context)
        layoutParamsDescription.addRule(CENTER_HORIZONTAL)
        layoutParamsDescription.addRule(BELOW, textViewTemperature.id)
        textViewHint.layoutParams = layoutParamsDescription
        val typefaceTextViewHint = ResourcesCompat.getFont(context, R.font.roboto_medium)
        textViewHint.typeface = typefaceTextViewHint
        textViewHint.includeFontPadding = false
        removeAllViews()
        textViewHint.textSize = height * RATIO_TEXT_SIZE_HEIGHT
        textViewHint.setTextColor(ContextCompat.getColor(context, R.color.color_7A868D))
        textViewHint.gravity = Gravity.CENTER
        textViewHint.text = context.resources.getString(R.string.current_temperature)
        addView(textViewTemperature)
        addView(textViewHint)
    }

    fun updateState(state: State) {
        if (_state == state) return
        _state = state
        if (_state == State.STOP_WORKING) {
            textViewTemperature.text = "--°C"
        } else {
            textViewHint.visibility = View.VISIBLE
        }
        initColorByState()
        initPaint()
        initShader()
        initValues()
        invalidate()
    }

    fun settingTemp(temp: Int) {
        if (_currentTemp == null) return
        _targetTemp = temp
        _inProgressSetting = true
        _runningState = when {
            _targetTemp == _currentTemp -> RunningMode.IDLE
            _targetTemp!! > _currentTemp!! -> RunningMode.INCREASE
            _targetTemp!! < _currentTemp!! -> RunningMode.REDUCE
            else -> RunningMode.IDLE
        }
        _currentAngle = if (_currentTemp!! > maxValue) {
            calculateAngle(maxValue)
        } else {
            calculateAngle(_currentTemp!!)
        }
        _targetSettingAngle = calculateAngle(_targetTemp!!)
        _newSweepAngle = abs(_targetSettingAngle - _currentAngle)
        if (_runningState == RunningMode.REDUCE) {
            invalidate()
            return
        }
        if (_runningState == RunningMode.INCREASE) {
            val updateListener: (Float) -> Unit = { animatedValue ->
                _newSweepAngle = animatedValue
                invalidate()
            }
            val endListener: () -> Unit = {
            }
            startAnimation(0f, _newSweepAngle, updateListener, endListener)
        }
    }

    fun setTemp(value: Int) {
        if (!_inProgressSetting) {
            _currentTemp = value
            _currentAngle = calculateAngle(value)
        }
        setTemperatureTitle(formatStringTemp(value))
    }

    private fun startAnimation(
        start: Float,
        end: Float,
        updateListener: (Float) -> Unit,
        endListener: () -> Unit
    ) {
        _animator = ValueAnimator.ofFloat(start, end)
        _animator?.cancel()
        _animator?.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator?) {}

            override fun onAnimationEnd(animation: Animator?) {
                endListener.invoke()
            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationRepeat(animation: Animator?) {
            }
        })
        _animator?.addUpdateListener {
            updateListener.invoke(it.animatedValue as Float)
        }
        _animator?.repeatMode = ValueAnimator.REVERSE
        _animator?.repeatCount = ValueAnimator.INFINITE
        _animator?.duration = 1000L
        _animator?.start()
    }

    fun cancelSettingTemp() {
        _runningState = RunningMode.IDLE
        _inProgressSetting = false
        _animator?.cancel()
        _animator = null
        invalidate()
    }

    fun resetItemInView() {
        _currentAngle = 0f
        _newSweepAngle = 0f
    }

    fun setTemperatureTitle(title: String) {
        textViewTemperature.text = title
    }

    private fun formatStringTemp(temp: Int): String {
        return String.format(
            context.resources.getString(R.string.temp_format), temp.formatTemperature()
        )
    }

    enum class RunningMode {
        REDUCE,
        INCREASE,
        IDLE
    }

    enum class State {
        STOP_WORKING,
        WORKING
    }

    companion object {
        private const val strokeWidthLine = 5f
        private const val strokeWidthText = 3f
        private const val RATIO_CIRCLE_IN = 180f / 237f
        private const val RATIO_CIRCLE_PROGRESS = 237f / 375f
        private const val RATIO_PADDING_TOP = 24f / 210f
        private const val RATIO_PADDING_BOTTOM = 12f / 210f
        private const val RATIO_PADDING_LEFT = 12f / 300f
        private const val RATIO_PADDING_RIGHT = 12f / 300f
        private const val RATIO_HEIGHT_WIDTH = 0.8f
        private const val RATIO_STROKE_WIDTH_HEIGHT = 14f / 266f
        private const val RATIO_TEXT_SIZE_HEIGHT = 14f / 266f
        private const val RATIO_DOT_RADIUS_HEIGHT = 12f / 266f
        private const val RATIO_TEXT_SIZE_TEMP_CENTER_HEIGHT = 52f / 266f
    }
}
