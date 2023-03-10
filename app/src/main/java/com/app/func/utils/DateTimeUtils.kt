package com.app.func.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*
import java.util.Locale


@Suppress("StringLiteralDuplication")
object DateTimeUtils {

    private const val TIME_FORMAT_NORMAL = "dd/MM/yyyy"

    enum class StatusCheckTime {
        ERROR,
        EQUAL,
        NORMAL
    }

    const val STRING_WARNING_HEADER_FORMAT = "THÁNG %s"

    @SuppressLint("SimpleDateFormat")
    fun compareTime(startTime: String, endTime: String): Boolean {
        return try {
            val sdf = SimpleDateFormat("HH:mm")
            val d1: Date = sdf.parse(startTime) ?: Date()
            val d2: Date = sdf.parse(endTime) ?: Date()
            (d2.time - d1.time) > 0
        } catch (ex: Exception) {
            ex.message?.let { DebugLog.e(it) }
            false
        }
    }

    @SuppressLint("SimpleDateFormat")
    fun checkTimeStatus(startTime: String, endTime: String): StatusCheckTime {
        return try {
            val sdf = SimpleDateFormat("HH:mm")
            val d1: Date = sdf.parse(startTime) ?: Date()
            val d2: Date = sdf.parse(endTime) ?: Date()
            when {
                d1.time > d2.time -> StatusCheckTime.ERROR
                d1.time == d2.time -> StatusCheckTime.EQUAL
                else -> StatusCheckTime.NORMAL
            }
        } catch (ex: Exception) {
            DebugLog.e("${ex.printStackTrace()}")
            StatusCheckTime.NORMAL
        }
    }

    @SuppressLint("SimpleDateFormat")
    fun compareMode(
        startTimeOne: String,
        endTimeOne: String,
        startTimeTwo: String,
        endTimeTwo: String
    ): Boolean {
        return try {
            val sdf = SimpleDateFormat("HH:mm")
            val d1: Date = sdf.parse(startTimeOne) ?: Date()
            val d2: Date = sdf.parse(endTimeOne) ?: Date()
            val d3: Date = sdf.parse(startTimeTwo) ?: Date()
            val d4: Date = sdf.parse(endTimeTwo) ?: Date()
            when {
                d1 < d2 && d2 < d3 && d3 < d4 -> {
                    false
                }
                d3 < d4 && d4 < d1 && d1 < d2 -> {
                    false
                }
                else -> true
            }
        } catch (ex: Exception) {
            DebugLog.e("${ex.printStackTrace()}")
            true
        }
    }


    fun getCurrentDay(date: Date): String {
        val sdf = SimpleDateFormat(TIME_FORMAT_NORMAL)
        return sdf.format(date.time)
    }

    fun getCurrentDay2(date: Date): String {
        val sdf = SimpleDateFormat("dd/MM/yy")
        return sdf.format(date.time)
    }

    fun formatDateString(date: Long): String {
        val sdf = SimpleDateFormat(TIME_FORMAT_NORMAL)
        return sdf.format(date)
    }

    fun formatDateString2(date: Long): String {
        val sdf = SimpleDateFormat("dd/MM/yy")
        return sdf.format(date)
    }

    fun formatDateToTimeString(date: Long): String {
        return try {
            val sdf = SimpleDateFormat("HH:mm")
            sdf.format(date)
        } catch (ex: Exception) {
            ex.printStackTrace()
            ""
        }


    }

    fun formatDateToTimeReport(date: Long): String {
        return try {
            val sdf = SimpleDateFormat("HH:00", Locale.getDefault())
            sdf.format(date)
        } catch (ex: Exception) {
            ex.printStackTrace()
            ""
        }
    }

    fun formatDateWarning(date: Long): String {
        val sdf = SimpleDateFormat("HH:mm - dd/MM/yyyy")
        return sdf.format(date)
    }

    fun getDay(currentDay: Date, numberMinus: Int): Date {
        val cal: Calendar = GregorianCalendar.getInstance()
        cal.time = currentDay
        cal.add(Calendar.DATE, numberMinus)
        return cal.time
    }

    fun getCurrentDateTime(): Date {
        return Calendar.getInstance().time
    }

    fun parseUTCToDefaultTimezone(utcHour: String): String {
        val utcSdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'")
        utcSdf.timeZone = TimeZone.getTimeZone("UTC")
        val utcDate = utcSdf.parse(utcHour)
        val sdf = SimpleDateFormat("HH:mm - dd/MM/yyyy ")
        sdf.timeZone = TimeZone.getDefault()
        return sdf.format(utcDate)
    }

    fun convertUtcToGtm(gmtTime: String): String {
        return try {
            val year = Calendar.getInstance().get(Calendar.YEAR)
            val month = Calendar.getInstance().get(Calendar.MONTH)
            val day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
            val currentTime = "$year/$month/$day $gmtTime"
            val sdfMyTime = SimpleDateFormat("yyyy/MM/dd HH:mm")
            val format1 = "HH:mm"
            sdfMyTime.timeZone = TimeZone.getTimeZone("UTC")
            val date = sdfMyTime.parse(currentTime)
            val localTime = Date(date.time)
            val format = "yyyy/MM/dd HH:mm:ss"
            val sdf = SimpleDateFormat(format)
            sdf.timeZone = TimeZone.getDefault()
            val timeConvert = SimpleDateFormat(format1).format(Date(sdf.format(localTime)))
            timeConvert
        } catch (ex: Exception) {
            ex.printStackTrace()
            ""
        }
    }

    fun convertGmtToUtc(gmtTime: String): String {
        val year = Calendar.getInstance().get(Calendar.YEAR)
        val month = Calendar.getInstance().get(Calendar.MONTH)
        val day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        val currentTime = "$year/$month/$day $gmtTime"
        val sdfMyTime = SimpleDateFormat("yyyy/MM/dd HH:mm")
        val format1 = "HH:mm"
        val date = sdfMyTime.parse(currentTime)
        val localTime = Date(date.time)
        val format = "yyyy/MM/dd HH:mm:ss"
        val sdf = SimpleDateFormat(format)
        sdf.timeZone = TimeZone.getTimeZone("UTC")
        return SimpleDateFormat(format1).format(Date(sdf.format(localTime)))
    }

    fun formatToMonth(time: Long): String {
        return try {
            val sdf = SimpleDateFormat("MM/yyyy", Locale.getDefault())
            val date = Date(time)
            sdf.format(date)
        } catch (ex: Exception) {
            Constants.STRING_EMPTY
        }
    }

    fun formatToSeconds(time: Long): String {
        return try {
            val sdf = SimpleDateFormat("HH:mm - dd/MM/yyyy ", Locale.getDefault())
            val date = Date(time)
            sdf.format(date)
        } catch (ex: Exception) {
            Constants.STRING_EMPTY
        }
    }

    fun formatDateToLong(time: String): Long {
        return try {
            val sdf = SimpleDateFormat("MM/yyyy", Locale.getDefault())
            sdf.parse(time)?.time ?: 0
        } catch (ex: Exception) {
            0
        }
    }

    fun getTimeStamp(time: String): Long {
        return try {
            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", Locale.getDefault())
            sdf.timeZone = TimeZone.getDefault()
            sdf.parse(time)?.time ?: 0
        } catch (ex: Exception) {
            0
        }
    }

    fun greaterThan(startTime: String, endTime: String): Boolean {
        return try {
            val sdf = SimpleDateFormat(TIME_FORMAT_NORMAL, Locale.getDefault())
            val d1: Date = sdf.parse(startTime) ?: Date()
            val d2: Date = sdf.parse(endTime) ?: Date()
            (d2.time - d1.time) > 0
        } catch (ex: Exception) {
            ex.message?.let { DebugLog.e(it) }
            false
        }
    }

}