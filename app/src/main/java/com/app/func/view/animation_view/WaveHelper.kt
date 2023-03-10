package com.app.func.view.animation_view

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.view.animation.LinearInterpolator
import java.util.ArrayList

class WaveHelper(private val waveView: WaveWaterView?) {

    private var mAnimatorSet: AnimatorSet? = null

    fun start() {
        waveView?.isShowWave = true
        if (mAnimatorSet != null) {
            mAnimatorSet?.start()
        }
    }

    fun pause() {
        mAnimatorSet?.pause()
    }

    private fun initAnimation() {
        val animators: MutableList<Animator> = ArrayList()

        val waveShiftAnim = ObjectAnimator.ofFloat(
            waveView, "waveShiftRatio", 0f, 1f
        )
        waveShiftAnim.repeatCount = ValueAnimator.INFINITE
        waveShiftAnim.duration = DURATION_ANIMATION_WAVE
        waveShiftAnim.interpolator = LinearInterpolator()
        animators.add(waveShiftAnim)

        // amplitude animation.
        // wave grows big then grows small, repeatedly
        val amplitudeAnim = ObjectAnimator.ofFloat(
            waveView, "amplitudeRatio", 0.02f, 0.02f
        )
        amplitudeAnim.repeatCount = ValueAnimator.INFINITE
        amplitudeAnim.repeatMode = ValueAnimator.REVERSE
        amplitudeAnim.duration = DURATION_AMPLITUDE
        amplitudeAnim.interpolator = LinearInterpolator()
        animators.add(amplitudeAnim)
        mAnimatorSet = AnimatorSet()
        mAnimatorSet?.playTogether(animators)
    }

    init {
        initAnimation()
    }

    companion object {
        private const val DURATION_ANIMATION_WAVE = 2000L
        private const val DURATION_AMPLITUDE = 5000L
    }

}