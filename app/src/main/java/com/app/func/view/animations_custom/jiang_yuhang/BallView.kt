package com.app.func.view.animations_custom.jiang_yuhang

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.app.func.view.animations_custom.jiang_yuhang.model.Ball
import java.util.*
import kotlin.math.abs

/*
https://developpaper.com/android-custom-view-for-bubble-animation/
 */
class BallView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var mRandom: Random = Random()
    private val mCount = 5 //  Number of small balls
    private val minSpeed = 5 //  Minimum moving speed of small ball
    private val maxSpeed = 20 //  Maximum movement speed of small ball
    private var mBalls: Array<Ball?> = arrayOfNulls(mCount)
    //  Maximum radius of small ball
    private var maxRadius = 0
    //  Minimum radius of small ball
    private var minRadius = 0
    private var mWidth = 200
    private var mHeight = 200

    init {
        initViews(attrs)
    }

    private fun randomColor(): Int {
        return Color.argb(
            255,
            Random().nextInt(256),
            Random().nextInt(256),
            Random().nextInt(256)
        )
    }

    private fun initViews(attrs: AttributeSet?) {
        //Initialize all balls (set colors and brushes, initialize the angle of movement)
        val randomColor = randomColor()
        //mBalls = arrayOf(mCount)
        for (i in 0 until mCount) {
            mBalls[i] = Ball()
            //Set brush
            val paint = Paint(Paint.ANTI_ALIAS_FLAG)
            paint.color = randomColor
            paint.style = Paint.Style.FILL
            paint.alpha = 180
            paint.strokeWidth = 0f
            //Set speed
            val speedX = (mRandom.nextInt(maxSpeed - minSpeed + 1) + 5) / 10f
            val speedY = (mRandom.nextInt(maxSpeed - minSpeed + 1) + 5) / 10f
            mBalls[i]?.paint = paint
            mBalls[i]?.vx = if (mRandom.nextBoolean()) speedX else -speedX
            mBalls[i]?.vy = if (mRandom.nextBoolean()) speedY else -speedY
        }
    }

    private fun setRandomBall(ball: Ball) {
        //Set brush
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.color = randomColor()
        paint.style = Paint.Style.FILL
        paint.alpha = 180
        paint.strokeWidth = 0f
        ball.paint = paint

        //Set speed
        val speedX = (mRandom.nextInt(maxSpeed - minSpeed + 1) + 5) / 10f
        val speedY = (mRandom.nextInt(maxSpeed - minSpeed + 1) + 5) / 10f
        ball.vx = if (mRandom.nextBoolean()) speedX else -speedX
        ball.vy = -speedY
        ball.radius = mRandom.nextInt(maxRadius + 1 - minRadius) + minRadius
        ball.cx = (mRandom.nextInt(mWidth - ball.radius) + ball.radius).toFloat()
        ball.cy = (mHeight - ball.radius).toFloat()
    }

    private fun getRandomBall(): Ball {
        val mBall = Ball()
        //Set brush
        setRandomBall(mBall)
        return mBall
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mWidth = resolveSize(mWidth, widthMeasureSpec)
        mHeight = resolveSize(mHeight, heightMeasureSpec)
        setMeasuredDimension(mWidth, mHeight)
        maxRadius = mWidth / 12
        minRadius = maxRadius / 2

        //Initializes the radius and center of a circle
/*        for (mBall in mBalls) {
            mBall!!.radius = mRandom.nextInt(maxRadius + 1 - minRadius) + minRadius
            //Initialize the position of the center of the circle. The minimum x is radius and the maximum is mwidth radius
            mBall.cx = (mRandom.nextInt(mWidth - mBall.radius) + mBall.radius).toFloat()
            mBall.cy = (mRandom.nextInt(mHeight - mBall.radius) + mBall.radius).toFloat()
        }*/

        //Initialize all balls (set colors and brushes, initialize the angle of movement)
        for (i in mBalls.indices) {
            mBalls[i] = getRandomBall()
        }

    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val startTime = System.currentTimeMillis()
        //Draw all the circles first
        for (i in 0 until mCount) {
            val (radius, cx, cy, _, _, paint) = mBalls[i]!!
            if (paint != null) {
                canvas?.drawCircle(cx, cy, radius.toFloat(), paint)
            }
        }
        //Ball collision boundary
        for (i in 0 until mCount) {
            val ball = mBalls[i]
            ball?.let { collisionDetectingAndChangeSpeed(it) } //  Calculation of collision boundary
            ball?.move() //  move
        }

        val stopTime = System.currentTimeMillis()
        val runTime = stopTime - startTime
        //Once every 16 milliseconds
        this.postInvalidateDelayed(abs(runTime - 16))
    }

    //Judge whether the ball collides with the collision boundary
    private fun collisionDetectingAndChangeSpeed(ball: Ball) {
        val left = 0
        val top = 0
        val right = mWidth
        val bottom = mHeight
        val speedX = ball.vx
        val speedY = ball.vy

        //The velocity of X is reversed around the collision. The judgment of speed is to prevent repeated collision detection, and then stick it to the wall ==
        if (ball.left() <= left && speedX < 0) {
            ball.vx = -ball.vx
        } else if (ball.top() <= top && speedY < 0) {
            ball.vy = -ball.vy
        } else if (ball.right() >= right && speedX > 0) {
            ball.vx = -ball.vx
        } else if (ball.bottom() >= bottom && speedY > 0) {
            ball.vy = -ball.vy
        }
    }
}