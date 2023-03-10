package com.app.func.utils

import android.app.ActivityManager
import android.app.ActivityManager.RunningAppProcessInfo
import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.provider.Settings
import android.text.TextUtils
import java.text.DecimalFormat
import java.text.Normalizer
import java.util.regex.Pattern

object Utils {

    val Any.TAG: String
        get() = this.javaClass.simpleName

    /**
     * Get Height of device
     */
    fun getScreenHeight(): Int {
        return Resources.getSystem().displayMetrics?.heightPixels ?: 0
    }

    /**
     * Get Width of device
     */
    fun getScreenWight(): Int {
        return Resources.getSystem().displayMetrics?.widthPixels ?: 0
    }

    /**
     * Get status bar height in pixel
     */
    fun getStatusBarHeight(context: Context): Int {
        var sttBarHeight = 0
        var resId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resId > 0) {
            sttBarHeight = context.resources.getDimensionPixelSize(resId)
        }
        return sttBarHeight
    }

    fun dipToPx(context: Context, dip: Float): Int {
        val density = context.resources.displayMetrics.density
        return (dip * density + 0.5f * if (dip >= 0) 1 else -1).toInt()
    }

    fun convertVietnameseText(content: String): String {
        val temp = Normalizer.normalize(content, Normalizer.Form.NFD)
        val pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+")
        return pattern.matcher(temp).replaceAll("").lowercase().replace(" ", "").replace("đ", "d")
    }

    fun convertVietnameseSearchProduct(content: String): String {
        val temp = Normalizer.normalize(content, Normalizer.Form.NFD)
        val pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+")
        return pattern.matcher(temp).replaceAll("").lowercase().replace("đ", "d")
    }

    fun isAboveAndroid10(): Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q

    fun isAppIsInBackground(context: Context): Boolean {
        var isInBackground = true
        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val runningProcesses = am.runningAppProcesses
        for (processInfo in runningProcesses) {
            if (processInfo.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                for (activeProcess in processInfo.pkgList) {
                    if (activeProcess == context.packageName) {
                        isInBackground = false
                    }
                }
            }
        }
        return isInBackground
    }

    fun Int.formatTemperature(): String {
        val format = DecimalFormat("00")
        return format.format(this)
    }

    fun getDeviceName(): String {
        val manufacturer = Build.MANUFACTURER
        val model = Build.MODEL
        return if (model.startsWith(manufacturer)) {
            capitalize(model)
        } else capitalize(manufacturer) + " " + model
    }

    fun getGetDeviceId(context: Context): String {
        return Settings.Secure.getString(
            context.contentResolver,
            Settings.Secure.ANDROID_ID
        )
    }

    fun getDeviceType(serial: String): ModelType {
        return when (serial) {
            "Constants_WATER" -> ModelType.WATER_PURIFIER
            "Constants_FAN" -> ModelType.SMART_FAN
            "Constants_FRIDGE" -> ModelType.FRIDGE
            "Constants_WATER_TANK" -> ModelType.HOT_WATER_TANK
            else -> ModelType.FREEZER
        }
    }

    private fun capitalize(str: String): String {
        if (TextUtils.isEmpty(str)) {
            return str
        }
        val arr = str.toCharArray()
        var capitalizeNext = true
        var phrase = ""
        for (c in arr) {
            if (capitalizeNext && Character.isLetter(c)) {
                phrase += Character.toUpperCase(c)
                capitalizeNext = false
                continue
            } else if (Character.isWhitespace(c)) {
                capitalizeNext = true
            }
            phrase += c
        }
        return phrase
    }

    enum class ModelType {
        WATER_PURIFIER,
        FREEZER,
        SMART_FAN,
        FRIDGE,
        HOT_WATER_TANK
    }

}

