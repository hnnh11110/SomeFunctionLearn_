package com.app.func.model

data class Temperature(var time: Long, var value: Int) {

    companion object {
        val compareValue: Comparator<Temperature> =
            Comparator { o1, o2 -> (o1.time - o2.time).toInt() }
    }
}