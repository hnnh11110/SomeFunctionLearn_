package com.app.func.view.seekbarcustom

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.app.func.R
import com.app.func.databinding.LayoutSeekbarCommonBinding
import com.app.func.utils.Constants
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

class CustomSeekBar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    var onValueSelected: ((Float) -> Unit)? = null
    var onValueProgress: ((Float) -> Unit)? = null
    var actionShowHideTooltip: ((Boolean) -> Unit)? = null
    private var binding =
        LayoutSeekbarCommonBinding.inflate(LayoutInflater.from(context), this, true)
    private var maxValue = 100f
    private var minValue = 0f
    private var currentValue: Float = DEFAULT_CURRENT_VALUE
    private val marginLeftProgress = resources.getDimensionPixelOffset(R.dimen._4dp)
    private val marginRightProgress = resources.getDimensionPixelOffset(R.dimen._4dp)
    private val paddingHorizontal = resources.getDimensionPixelOffset(R.dimen._10dp)
    private val thumbWidth = resources.getDimensionPixelOffset(R.dimen._46dp)
    private var currentState = State.WORKING
    private var counterTouchPoint = 0
    private var formatter = DecimalFormat("0.0", DecimalFormatSymbols(Locale.US))
    private var formatterMinTextview = DecimalFormat("##.#", DecimalFormatSymbols(Locale.US))
    private var floatFormatter = DecimalFormat("##.#", DecimalFormatSymbols(Locale.US))

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        parent.requestDisallowInterceptTouchEvent(true)
        if (currentState == State.WORKING) {
            return when (event?.action) {
                MotionEvent.ACTION_DOWN -> {
                    counterTouchPoint = 0
                    actionShowHideTooltip?.invoke(true)
                    return true
                }
                MotionEvent.ACTION_MOVE -> {
                    counterTouchPoint++
                    // if counter point less than TOUCH_POINT_COUNT that means action as action DOWN
                    // and then we not update UI
                    if (counterTouchPoint < TOUCH_POINT_COUNT) {
                        return true
                    }
//                    binding.bubbleView.isVisible = true
                    updateProgress(event.x)
                    onValueSelected?.invoke(
                        formatFloatValue(currentValue)
                    )
                    true
                }
                MotionEvent.ACTION_UP -> {
                    // if counter point less than TOUCH_POINT_COUNT that means action as action DOWN
                    // and then we not update UI
                    if (counterTouchPoint < TOUCH_POINT_COUNT) {
                        return true
                    }
                    onValueSelected?.invoke(
                        formatFloatValue(currentValue)
                    )
                    actionShowHideTooltip?.invoke(false)
//                    binding.bubbleView.isInvisible = true
                    true
                }
                MotionEvent.ACTION_CANCEL -> {
//                    binding.bubbleView.isInvisible = true
                    true
                }
                else -> super.onTouchEvent(event)
            }
        } else {
            return super.onTouchEvent(event)
        }
    }

    private fun updateProgress(x: Float) {
        Log.d("CustomSeekBar ", "Current x  ---- width  -----   x / width->  $x    ----  $width   ----  ${x/width}")
        when {
            x <= marginLeftProgress + binding.tvMinValue.width + paddingHorizontal + thumbWidth / 2 -> {
                currentValue = minValue
                onValueProgress?.invoke((marginLeftProgress + binding.tvMinValue.width + paddingHorizontal + thumbWidth / 2).toFloat())
                binding.guideline.setGuidelinePercent((marginLeftProgress + binding.tvMinValue.width + paddingHorizontal + thumbWidth / 2).toFloat() / width)
            }
            x >= width - (marginRightProgress + binding.tvMaxValue.width + paddingHorizontal + thumbWidth / 2) -> {
                onValueProgress?.invoke((width - (marginRightProgress + binding.tvMaxValue.width + paddingHorizontal + thumbWidth / 2)).toFloat())
                currentValue = maxValue
                binding.guideline.setGuidelinePercent((width - marginRightProgress - paddingHorizontal - binding.tvMaxValue.width - thumbWidth / 2).toFloat() / width)
            }
            else -> {
                val value =
                    (maxValue - minValue) * (x - marginLeftProgress - binding.tvMinValue.width - paddingHorizontal - thumbWidth / 2) / (width - marginLeftProgress - marginRightProgress - paddingHorizontal * 2 - binding.tvMinValue.width - binding.tvMaxValue.width - thumbWidth / 2 * 2)
                currentValue = value
                onValueProgress?.invoke(x)
//                currentValue = formatFloatValue(value)
                binding.guideline.setGuidelinePercent(x / width)
            }
        }
//        binding.tvCurrentValue.text = formatWaterAmount(currentValue)
//        binding.bubbleView.setContentString(formatWaterAmount(currentValue))
        binding.tvCurrentValue.text = formatNumber("%02d", currentValue)
        binding.bubbleView.setContentString(formatNumber("%02d", currentValue))


    }

    private fun formatWaterAmount(amount: Float, isMinTextView: Boolean = false) =
        if (isMinTextView) {
            formatterMinTextview.format(amount)
        } else {
            formatter.format(amount)
        }

    private fun formatNumber(pattern: String, value : Number) = String.format(pattern, value.toInt())

    private fun formatFloatValue(value: Float) = floatFormatter.format(value).toFloat()

    fun setValueRange(min: Float, max: Float) {
        val minString: String = formatNumber("%02d", min.toInt())
        val maxString: String = formatNumber("%02d", max.toInt())
        minValue = min
        maxValue = max
        binding.tvMinValue.text = minString
        binding.tvMaxValue.text = maxString
//        binding.tvMinValue.text = formatWaterAmount(min, true) + "L"
//        binding.tvMaxValue.text = formatWaterAmount(max) + "L"
        currentValue = DEFAULT_CURRENT_VALUE
    }

    fun setValueCurrent(value: Float) {
        if (currentValue == value || value < 0f) return
        currentValue = value
        binding.tvCurrentValue.text = value.toInt().toString()
//        binding.tvCurrentValue.text = formatWaterAmount(value)
        this.post {
            val currentRatio = currentValue / (maxValue - minValue)
            binding.guideline.setGuidelinePercent(
                ((binding.viewBackground.width - thumbWidth / 2 * 2) * currentRatio + paddingHorizontal + marginLeftProgress + binding.tvMinValue.width + thumbWidth / 2) / width
            )
        }
    }

    fun setState(state: State) {
        currentState = state
        when (state) {
            State.STOPPED_WORKING -> {
                binding.apply {
                    tvMinValue.setTextColor(ContextCompat.getColor(context, R.color.color_BBC5CB))
                    tvMaxValue.setTextColor(ContextCompat.getColor(context, R.color.color_BBC5CB))
                    tvCurrentValue.setTextColor(
                        ContextCompat.getColor(context, R.color.color_BBC5CB)
                    )
                    currentValue = Constants.FLOAT_ZERO
                    tvCurrentValue.text = Constants.INT_ZERO.toString()
                    tvCurrentValue.background =
                        ContextCompat.getDrawable(context, R.drawable.seekbar_thumb_common)
                }
            }
            State.WORKING -> {
                binding.apply {
                    tvMinValue.setTextColor(ContextCompat.getColor(context, R.color.color_484646))
                    tvMaxValue.setTextColor(ContextCompat.getColor(context, R.color.color_484646))
                    tvCurrentValue.setTextColor(
                        ContextCompat.getColor(context, R.color.color_63B128)
                    )
                    tvCurrentValue.background =
                        ContextCompat.getDrawable(context, R.drawable.seekbar_thumb_common)
                }
            }
        }
    }

    companion object {
        private const val TOUCH_POINT_COUNT = 10
        private const val DEFAULT_CURRENT_VALUE = -1F

    }

    enum class State {
        WORKING,
        STOPPED_WORKING
    }


}