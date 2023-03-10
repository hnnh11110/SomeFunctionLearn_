package com.app.func.view.animation_view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.annotation.RequiresApi
import androidx.collection.lruCache
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.withTranslation
import com.app.func.R

class WaveWaterView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    var isShowWave = false
    private var content = NON_VALUE
    private var paint = Paint()
    private var mWaveShader: BitmapShader? = null
    private var mShaderMatrix: Matrix? = null
    private var mViewPaint: Paint? = null
//    private var rectF: RectF = RectF().apply {
//        left = 0f
//        top = 0f
//        right = width.toFloat()
//        bottom = height.toFloat()
//    }

    private var mDefaultAmplitude = 0f
    private var mDefaultWaterLevel = 0f
    private var mDefaultWaveLength = 0f
    private var mDefaultAngularFrequency = 0.0
    private var mAmplitudeRatio = DEFAULT_AMPLITUDE_RATIO

    var waveLengthRatio = DEFAULT_WAVE_LENGTH_RATIO
    private var mWaterLevelRatio = DEFAULT_WATER_LEVEL_RATIO
    private var mWaveShiftRatio = DEFAULT_WAVE_SHIFT_RATIO
    private var _behindWaveColor = DEFAULT_BEHIND_WAVE_COLOR
    private var _frontWaveColor = DEFAULT_FRONT_WAVE_COLOR

    private val textPaint: Paint = Paint()
    private val titlePaint: Paint = Paint()

    var onAnimationUp: () -> Unit = {

    }
    var onAnimationDown: () -> Unit = {

    }
    private var bitmapWater1: Bitmap? = null

    private var currentWaterDrawableBackground2: Int = R.drawable.water_blue

    private val titleTextPaint = TextPaint().apply {
        isAntiAlias = true
        textAlign = Paint.Align.CENTER
        color = Color.WHITE
        textSize = resources.getDimension(R.dimen._12sp)
        typeface = ResourcesCompat.getFont(context, R.font.roboto_medium)
    }


    init {
        mShaderMatrix = Matrix()
        mViewPaint = Paint()
        mViewPaint?.isAntiAlias = true

        textPaint.apply {
            isAntiAlias = true
            textAlign = Paint.Align.CENTER
            color = Color.WHITE
            textSize = resources.getDimension(R.dimen._88sp)
            typeface = ResourcesCompat.getFont(context, R.font.inter_extra_bold)
        }

        titlePaint.apply {
            isAntiAlias = true
            textAlign = Paint.Align.CENTER
            color = Color.WHITE
            textSize = resources.getDimension(R.dimen._14sp)
            typeface = ResourcesCompat.getFont(context, R.font.roboto_medium)
        }

        val d1 = ContextCompat.getDrawable(context, currentWaterDrawableBackground2)
        bitmapWater1 = d1?.let { drawableToBitmap(it) }

        mAmplitudeRatio = DEFAULT_AMPLITUDE_RATIO
        mWaterLevelRatio = DEFAULT_WATER_LEVEL_RATIO
        waveLengthRatio = DEFAULT_WAVE_LENGTH_RATIO
        mWaveShiftRatio = DEFAULT_WAVE_SHIFT_RATIO
        _frontWaveColor = DEFAULT_FRONT_WAVE_COLOR
        _behindWaveColor = DEFAULT_BEHIND_WAVE_COLOR
    }

    var waveShiftRatio: Float
        get() = mWaveShiftRatio
        set(waveShiftRatio) {
            if (mWaveShiftRatio != waveShiftRatio) {
                mWaveShiftRatio = waveShiftRatio
                invalidate()
            }
        }

    var waterLevelRatio: Float
        get() = mWaterLevelRatio
        set(waterLevelRatio) {
            if (mWaterLevelRatio != waterLevelRatio) {
                mWaterLevelRatio = waterLevelRatio
                invalidate()
            }
        }

    var amplitudeRatio: Float
        get() = mAmplitudeRatio
        set(amplitudeRatio) {
            if (mAmplitudeRatio != amplitudeRatio) {
                mAmplitudeRatio = amplitudeRatio
                invalidate()
            }
        }

    fun setWaveColor(behindWaveColor: Int, frontWaveColor: Int) {
        _behindWaveColor = behindWaveColor
        _frontWaveColor = frontWaveColor
        if (width > 0 && height > 0) {
            mWaveShader = null
            createShader()
            invalidate()
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        createShader()
    }

    /**
     * Create the shader with default waves which repeat horizontally, and clamp vertically
     */
    private fun createShader() {
        mDefaultAngularFrequency = 2.0f * Math.PI / DEFAULT_WAVE_LENGTH_RATIO / width
        mDefaultAmplitude = height * DEFAULT_AMPLITUDE_RATIO
        mDefaultWaterLevel = height * DEFAULT_WATER_LEVEL_RATIO
        mDefaultWaveLength = width.toFloat()
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val wavePaint = Paint()
        wavePaint.strokeWidth = 2f
        wavePaint.isAntiAlias = true

        val endX = width + 1
        val endY = height + 1
        val waveY = FloatArray(endX)
        wavePaint.color = _behindWaveColor
        for (beginX in 0 until endX) {
            val wx = beginX * mDefaultAngularFrequency
            val beginY = (mDefaultWaterLevel + mDefaultAmplitude * Math.sin(wx)).toFloat()
            canvas.drawLine(beginX.toFloat(), beginY, beginX.toFloat(), endY.toFloat(), wavePaint)
            waveY[beginX] = beginY
        }
        wavePaint.color = _frontWaveColor
        val wave2Shift = (mDefaultWaveLength / 4).toInt()
        for (beginX in 0 until endX) {
            canvas.drawLine(
                beginX.toFloat(),
                waveY[(beginX + wave2Shift) % endX], beginX.toFloat(), endY.toFloat(), wavePaint
            )
        }

        mWaveShader = BitmapShader(bitmap, Shader.TileMode.REPEAT, Shader.TileMode.CLAMP)
        mViewPaint?.shader = mWaveShader
    }

    override fun onDraw(canvas: Canvas) {
        val rectF = RectF(0f, 0f, width.toFloat(), height.toFloat())
        if (isShowWave && mWaveShader != null) {
            if (mViewPaint?.shader == null) {
                mViewPaint?.shader = mWaveShader
            }
            mShaderMatrix?.setScale(
                waveLengthRatio / DEFAULT_WAVE_LENGTH_RATIO,
                mAmplitudeRatio / DEFAULT_AMPLITUDE_RATIO, 0f,
                mDefaultWaterLevel
            )
            mShaderMatrix?.postTranslate(
                mWaveShiftRatio * width,
                (DEFAULT_WATER_LEVEL_RATIO - mWaterLevelRatio) * height
            )
            mWaveShader?.setLocalMatrix(mShaderMatrix)
            val radius = width / 2f
            mViewPaint?.let { canvas.drawCircle(width / 2f, height - radius, radius, it) }
            bitmapWater1?.let {
                canvas.drawBitmap(it, null, rectF, paint)
            }
        } else {
            mViewPaint?.shader = null
        }
        drawText(canvas)
    }

    private fun drawableToBitmap(drawable: Drawable): Bitmap? {
        if (drawable is BitmapDrawable) {
            return drawable.bitmap
        }
        val bitmap =
            Bitmap.createBitmap(
                drawable.intrinsicWidth,
                drawable.intrinsicHeight,
                Bitmap.Config.ARGB_8888
            )
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }

    fun setContent(content: String) {
        this.content = content
        invalidate()
    }

    fun changeWaterColor(state: StateColor) {
        when (state) {
            StateColor.BLUE -> {
                _frontWaveColor = WaterColor.frontBlueColor
                _behindWaveColor = WaterColor.backBlueColor
                currentWaterDrawableBackground2 = R.drawable.water_blue
            }
            StateColor.YELLOW -> {
                _frontWaveColor = WaterColor.frontYellowColor
                _behindWaveColor = WaterColor.backYellowColor
                currentWaterDrawableBackground2 = R.drawable.water_yellow
            }
            StateColor.RED -> {
                _frontWaveColor = WaterColor.frontRedColor
                _behindWaveColor = WaterColor.backRedColor
                currentWaterDrawableBackground2 = R.drawable.water_red
            }
        }
        setWaveColor(_frontWaveColor, _behindWaveColor)
        val d1 = ContextCompat.getDrawable(context, currentWaterDrawableBackground2)
        bitmapWater1 = d1?.let { drawableToBitmap(it) }
        invalidate()
    }

    fun changeToLostConnection() {
        _frontWaveColor = WaterColor.frontLostConnectionColors
        _behindWaveColor = WaterColor.backLostConnectionColors2
        setWaveColor(_frontWaveColor, _behindWaveColor)
        currentWaterDrawableBackground2 = R.drawable.water_gray
        content = context.getString(R.string.non_data)
        val d1 = ContextCompat.getDrawable(context, currentWaterDrawableBackground2)
        bitmapWater1 = d1?.let { drawableToBitmap(it) }
        invalidate()
    }

    private fun drawText(canvas: Canvas) {
        val title = context.getString(R.string.title_value)
        val rect = Rect()
        val rectTitle = Rect()
        if (content == NON_VALUE) {
            textPaint.getTextBounds(NON_VALUE_BOUND, 0, NON_VALUE_BOUND.length, rect)
        } else {
            textPaint.getTextBounds(content, 0, content.length, rect)
        }
        titlePaint.getTextBounds(title, 0, title.length, rectTitle)

        if (rectTitle.width() >= width * MAX_TEXT_WIDTH_RATIO && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // on small screen
            canvas.drawText(
                content,
                (width / 2).toFloat(),
                height * RATIO_TEXT_H + rect.height(),
                textPaint.apply {
                    textSize = resources.getDimensionPixelSize(R.dimen._60sp).toFloat()
                }
            )
            canvas.drawMultilineText(
                title,
                titleTextPaint,
                (width * MAX_TEXT_WIDTH_RATIO).toInt(),
                (width / 2).toFloat(),
                height * RATIO_TEXT_H + rect.height() + titleTextPaint.textSize / 2
            )
        } else {
            canvas.drawText(
                content,
                (width / 2).toFloat(),
                height * RATIO_TEXT_H + rect.height(),
                textPaint
            )
            canvas.drawText(
                title,
                (width / 2).toFloat(),
                height * RATIO_TEXT_H + rect.height() + rectTitle.height() * 2,
                titlePaint
            )
        }
    }


    private fun StaticLayout.draw(canvas: Canvas, x: Float, y: Float) {
        canvas.withTranslation(x, y) {
            draw(this)
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun Canvas.drawMultilineText(
        text: CharSequence,
        textPaint: TextPaint,
        width: Int,
        x: Float,
        y: Float,
        start: Int = 0,
        end: Int = text.length,
        alignment: Layout.Alignment = Layout.Alignment.ALIGN_NORMAL,
        textDir: TextDirectionHeuristic = TextDirectionHeuristics.LTR,
        spacingMult: Float = 1f,
        spacingAdd: Float = 0f,
//        hyphenationFrequency: Int = Layout.HYPHENATION_FREQUENCY_NONE,
//        justificationMode: Int = Layout.JUSTIFICATION_MODE_NONE
    ) {

        val cacheKey =
            "$text-$start-$end-$textPaint-$width-$alignment-$textDir-$spacingMult-$spacingAdd"

        val staticLayout = StaticLayoutCache[cacheKey] ?: StaticLayout.Builder
            .obtain(text, start, end, textPaint, width)
            .setAlignment(alignment)
            .setTextDirection(textDir)
            .setLineSpacing(spacingAdd, spacingMult)
//            .setBreakStrategy(LineBreaker.BREAK_STRATEGY_HIGH_QUALITY)
//            .setJustificationMode(justificationMode)
            .build()

        staticLayout.draw(this, x, y)
    }

    private object StaticLayoutCache {

        private const val MAX_SIZE = 50 // Max number of cached items
        private val cache = lruCache<String, StaticLayout>(MAX_SIZE)

        operator fun set(key: String, staticLayout: StaticLayout) {
            cache.put(key, staticLayout)
        }

        operator fun get(key: String): StaticLayout? {
            return cache[key]
        }
    }

    private var initialY = 0f

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                initialY = event.y
            }
            MotionEvent.ACTION_MOVE, MotionEvent.ACTION_CANCEL -> {
                val finalY = event.y
                if (initialY + AT_LEAST_DISTANCE_MOVE < finalY) {
                    onAnimationDown.invoke()
                }
                if (initialY > finalY + AT_LEAST_DISTANCE_MOVE) {
                    onAnimationUp.invoke()
                }
            }
            MotionEvent.ACTION_UP -> {
            }
            MotionEvent.ACTION_OUTSIDE -> {
            }
        }
        return super.onTouchEvent(event)
    }

    enum class StateColor {
        BLUE, YELLOW, RED
    }

    companion object {
        private const val DEFAULT_AMPLITUDE_RATIO = 0.05f
        private const val DEFAULT_WATER_LEVEL_RATIO = 0.5f
        private const val DEFAULT_WAVE_LENGTH_RATIO = 1.0f
        private const val DEFAULT_WAVE_SHIFT_RATIO = 0.0f
        val DEFAULT_BEHIND_WAVE_COLOR = WaterColor.backBlueColor
        val DEFAULT_FRONT_WAVE_COLOR = WaterColor.frontBlueColor

        private const val NON_VALUE_BOUND = "00"
        private const val NON_VALUE = "--"
        private const val AT_LEAST_DISTANCE_MOVE = 10f
        private const val RATIO_TEXT_H = 214f / 366f
        private const val MAX_TEXT_WIDTH_RATIO = 2 / 3F
    }
}