package com.app.func.view.animations_custom.jiang_yuhang.model

import android.graphics.Paint

data class Ball(
    var radius: Int = 0,
    //Center of circle
    var cx: Float = 0f,
    //Center of circle
    var cy: Float = 0f,
    //X-axis speed
    var vx: Float = 0f,
    //Y-axis speed
    var vy: Float = 0f,
    var paint: Paint? = null
) {

    /* //Radius
     var radius = 0

     //Center of circle
     var cx = 0f

     //Center of circle
     var cy = 0f

     //X-axis speed
     var vx = 0f

     //Y-axis speed
     var vy = 0f

     var paint: Paint? = null*/

    //Move
    fun move() {
        //Move in the direction of the angle to offset the center of the circle
        cx += vx
        cy += vy
    }

    fun left(): Int {
        return (cx - radius).toInt()
    }

    fun right(): Int {
        return (cx + radius).toInt()
    }

    fun bottom(): Int {
        return (cy + radius).toInt()
    }

    fun top(): Int {
        return (cy - radius).toInt()
    }
}