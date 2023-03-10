package com.app.func.view.seekbarcustom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.app.func.R

class ToolTipSeekBar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    companion object {
        const val DEFAULT_TEXT_SIZE = 16F
    }

    private var strokeWidth = resources.getDimensionPixelOffset(R.dimen._1dp).toFloat()

    private var outlinePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = ContextCompat.getColor(context, R.color.color_63B128)
        this.strokeWidth = this@ToolTipSeekBar.strokeWidth
        strokeJoin = Paint.Join.ROUND
    }

    private var whitePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = ContextCompat.getColor(context, R.color.color_63B128)
        this.strokeWidth = this@ToolTipSeekBar.strokeWidth
    }

    private var textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = Color.WHITE
        textSize = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            DEFAULT_TEXT_SIZE, resources.displayMetrics
        )
        textAlign = Paint.Align.CENTER
        typeface = ResourcesCompat.getFont(context, R.font.roboto_medium)
    }

    private var path = Path()

    private var radius = resources.getDimensionPixelOffset(R.dimen._6dp).toFloat()

    private var content: String? = null

    private fun drawBubble(canvas: Canvas?) {
//        canvas?.drawColor(Color.WHITE)
        val rectF = RectF().apply {
            top = 0f + strokeWidth
            bottom = height.toFloat() - width.toFloat() / 8
            left = 0f + strokeWidth
            right = width.toFloat() - strokeWidth
        }
        canvas?.drawRoundRect(rectF, radius, radius, outlinePaint)

        canvas?.drawLine(
            width.toFloat() / 2 - width / 8, height - width.toFloat() / 8,
            width.toFloat() / 2 + width / 8, height - width.toFloat() / 8,
            whitePaint
        )

        path.moveTo(width.toFloat() / 2 - width / 8, height - width.toFloat() / 8)
        path.lineTo(width.toFloat() / 2, height.toFloat())
        path.lineTo(width.toFloat() / 2 + width / 8, height - width.toFloat() / 8)

        canvas?.drawPath(path, outlinePaint)

        content?.let {
            canvas?.drawText(
                it,
                width.toFloat() / 2,
                height.toFloat() / 2 + width.toFloat() / 8,
                textPaint
            )
        }
//        canvas?.drawColor(ContextCompat.getColor(context, R.color.color_63B128))
    }

    fun setContentString(content: String) {
        val formatContent = String.format("%02d", content.toInt())
        this.content = formatContent
        invalidate()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        drawBubble(canvas)
    }
}