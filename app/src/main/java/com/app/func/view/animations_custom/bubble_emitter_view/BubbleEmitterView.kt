package com.app.func.view.animations_custom.bubble_emitter_view

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Color.rgb
import android.graphics.Paint
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.core.animation.doOnEnd
import androidx.core.content.ContextCompat
import com.app.func.R
import java.util.*
import kotlin.math.abs
import kotlin.random.Random

class BubbleEmitterView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet?, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    companion object {
        private const val BUBBLE_LIMIT = 25
        private const val BASE_ALPHA = 255
        private const val NO_VALUE = -1F
    }

    class Bubble(
        val uuid: UUID,
        var radius: Float,
        var x: Float = NO_VALUE,
        var y: Float = NO_VALUE,
        var alpha: Int = BASE_ALPHA,
        var alive: Boolean = true,
        var animating: Boolean = false
    )

    private val pushHandler = Handler(Looper.getMainLooper())
    private var bubbles: MutableList<Bubble> = mutableListOf()

    private var emissionDelayMillis: Long = 10L * bubbles.size
    private var canExplode: Boolean = true

    private val paintStroke = Paint().apply {
        isAntiAlias = true
        color = ContextCompat.getColor(context, R.color.ghostWhite)
        strokeWidth = 2F
        style = Paint.Style.STROKE
    }

    private val paintFill = Paint().apply {
        isAntiAlias = true
        color = ContextCompat.getColor(context, R.color.whiteSmoke)
        style = Paint.Style.FILL
    }

    private val paintGloss = Paint().apply {
        isAntiAlias = true
        color = Color.WHITE
        style = Paint.Style.FILL
    }

    fun emitBubble(strength: Int) {
        if (bubbles.size >= BUBBLE_LIMIT) {
            return
        }
        val uuid: UUID = UUID.randomUUID()
        val radius: Float = abs(strength) / 4F
        val bubble = Bubble(uuid, radius)

        pushHandler.postDelayed({
            bubbles.add(bubble)
        }, emissionDelayMillis)

        invalidate()
    }

    private fun getColorFromResource(color: Int) = ContextCompat.getColor(context, color)

    fun setColors(
        @ColorInt stroke: Int = rgb(249, 249, 249),
        @ColorInt fill: Int = rgb(236, 236, 236),
        @ColorInt gloss: Int = rgb(255, 255, 255)
    ) {
        paintStroke.color = stroke
        paintFill.color = fill
        paintGloss.color = gloss
    }

    fun setColorResources(
        @ColorRes stroke: Int = R.color.ghostWhite,
        @ColorRes fill: Int = R.color.whiteSmoke,
        @ColorRes gloss: Int = android.R.color.white
    ) {
        paintStroke.color = ContextCompat.getColor(context, stroke)
        paintFill.color = ContextCompat.getColor(context, fill)
        paintGloss.color = ContextCompat.getColor(context, gloss)
    }

    fun setEmissionDelay(delayMillis: Long = 10L * bubbles.size) {
        emissionDelayMillis = delayMillis
    }

    fun canExplode(isCanExplode: Boolean = true) {
        canExplode = isCanExplode
    }

    private fun moveAnimation(uuid: UUID, radius: Float): ValueAnimator {
        val animator = ValueAnimator.ofFloat(height.toFloat(), height / 2f - radius * 10)
        with(animator) {
            addUpdateListener { valueAnimator ->
                bubbles.firstOrNull { it.uuid == uuid }?.y = valueAnimator.animatedValue as Float
            }
            duration = 2000L + 100L * radius.toLong()
            interpolator = LinearInterpolator()
        }
        return animator
    }

    private fun fadeOutAnimation(uuid: UUID, radius: Float): ValueAnimator {
        val animator: ValueAnimator = ValueAnimator.ofInt(BASE_ALPHA, 0)
        with(animator) {
            addUpdateListener { valueAnimator ->
                bubbles.firstOrNull { it.uuid == uuid }?.alpha = valueAnimator.animatedValue as Int
            }
            doOnEnd {
                bubbles.firstOrNull { it.uuid == uuid }?.alive = false
                invalidate()
            }
            duration = 200L
            startDelay = 1000L + 100L * radius.toLong()
            interpolator = LinearInterpolator()
        }
        return animator
    }

    private fun explodeAnimation(uuid: UUID, radius: Float): ValueAnimator {
        val animator: ValueAnimator = ValueAnimator.ofFloat(radius, radius * 2)
        with(animator) {
            addUpdateListener { valueAnimator ->
                bubbles.firstOrNull { it.uuid == uuid }?.radius =
                    valueAnimator.animatedValue as Float
            }
            duration = 300L
            startDelay = 1000L + 100L * radius.toLong()
            interpolator = LinearInterpolator()
        }
        return animator
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        bubbles = bubbles.filter { it.alive }.toMutableList()
        bubbles.forEach {
            val diameter = (it.radius * 2).toInt()
            if (it.x == -1f) {
                it.x = Random.nextInt(0 + diameter, width - diameter).toFloat()
            }

            paintStroke.alpha = it.alpha
            paintFill.alpha = it.alpha
            paintGloss.alpha = it.alpha

            canvas?.drawCircle(it.x, it.y, it.radius, paintStroke)
            canvas?.drawCircle(it.x, it.y, it.radius, paintFill)
            canvas?.drawCircle(it.x + it.radius / 2.5F, it.y - it.radius / 2.5F, it.radius / 4, paintGloss)

            if (!it.animating) {
                it.animating = true
                moveAnimation(it.uuid, it.radius).start()
                if (canExplode) {
                    explodeAnimation(it.uuid, it.radius).start()
                }
                fadeOutAnimation(it.uuid, it.radius).start()
            }
        }
    }
}