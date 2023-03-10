package com.app.func.view.chart.utils

import org.json.JSONObject

object TelemetryParsingConstant {
    const val SUBSCRIPTION_ID = "subscriptionId"
    const val DATA = "data"
    const val RSSI = "rssi"
    const val ERROR = "error"
    const val POWER = "power"
    const val TEMPERATURE = "temperature"
    const val FREEZER_TEMPERATURE = "freezerTemperature"
    const val PROVISION_TIME = "provisionTime"
    const val ACTIVE = "active"
    const val STATUS_MEMBRANE_WASH = "statusMembraneWash"
    const val TRUE = "true"
    const val FW_VERSION = "fwVersion"
    const val IS_UPGRADE_FW = "isUpdateFw"
    const val WATER_OUT = "waterOut"
    const val TOTAL_TIME_FILTER_01 = "totalTimeFilter1"
    const val TOTAL_TIME_FILTER_02 = "totalTimeFilter2"
    const val TOTAL_TIME_FILTER_03 = "totalTimeFilter3"
    const val TOTAL_TIME_FILTER_04 = "totalTimeFilter4"
    const val TOTAL_TIME_FILTER_05 = "totalTimeFilter5"
    const val IP_LOCAL = "ipLocal"
    const val IP_PUBLIC = "ipPublic"
    const val PURITY_WATER_IN = "purityWaterIn"
    const val PURITY_WATER_OUT = "purityWaterOut"
    const val REMAIN_TIME_FILTER_01 = "remainTimeFilter1"
    const val REMAIN_TIME_FILTER_02 = "remainTimeFilter2"
    const val REMAIN_TIME_FILTER_03 = "remainTimeFilter3"
    const val REMAIN_TIME_FILTER_04 = "remainTimeFilter4"
    const val REMAIN_TIME_FILTER_05 = "remainTimeFilter5"

    // smart fan
    const val FAN_LIGHT_MODE = "lightMode"
    const val FAN_MAX_SPEED = "maxSpeed"
    const val FAN_MIN_SPEED = "minSpeed"
    const val FAN_OPERATION_MODE = "operationMode"
    const val FAN_ATTRIBUTE_SPEED = "speed"
    const val FAN_SWING_MODE = "swingMode"
    const val FAN_TEMPERATURE = "temperature"
    const val FAN_SET_DEVICE_SETTINGS = "setDeviceSettings"
    const val FAN_REMAIN_TIMER = "timer"

    const val FAN_CHILD_LOCK = "lockKey"
    const val FAN_CHILD_LOCK_ON = "0"

    // Hot water tank
    const val TANK_TEMPERATURE = "tankTemperature"
    const val INPUT_WATER_TEMPERATURE = "inputWaterTemperature"
    const val DETECT_PEOPLE = "detectPeople"
    const val MIN_TEMPERATURE = "minTemperature"
    const val MAX_TEMPERATURE = "maxTemperature"
    const val TARGET_TEMPERATURE = "targetTemperature"
    const val AUTO_OFF = "autoOff"
    const val KEEP_TEMPERATURE = "keepTemperature"
    const val RUNNING_STATUS = "runningStatus"
    const val POWER_OFF = "powerOff"
    const val OPERATION_MODE = "operationMode"
    const val TIMER_REMAIN = "timer"
    const val SET_DEVICE_SETTINGS = "setDeviceSettings"

    fun <T> getAttr(name: String, data: JSONObject): T? {
        if (data.has(name)) {
            return data.getJSONArray(name).getJSONArray(0)[1] as? T?
        }
        return null
    }
}