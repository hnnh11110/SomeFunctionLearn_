package com.app.func.view.chart.models

import android.os.Parcelable
import com.app.func.view.chart.utils.ModelType
import com.app.func.view.chart.utils.TelemetryParsingConstant
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
//import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class ReportResponse(
    @SerializedName("power")
    val power: List<Temperature>,
    @SerializedName("freezerTemperature")
    val temperature: List<Temperature>,
) : Parcelable

@Parcelize
data class ReportData(
    @SerializedName("power")
    val power: List<Temperature>?,
    @SerializedName("freezerTemperature")
    val temperature: List<Temperature>,
    @SerializedName(TelemetryParsingConstant.PURITY_WATER_IN)
    val waterIn: List<Temperature>?,
    @SerializedName(TelemetryParsingConstant.PURITY_WATER_OUT)
    val waterOut: List<Temperature>?,
    @SerializedName(TelemetryParsingConstant.FAN_TEMPERATURE)
    val temperatureFan: List<Temperature>?
) : Parcelable


@Parcelize
data class ReportWaterResponse(
    @SerializedName(TelemetryParsingConstant.PURITY_WATER_IN)
    val waterIn: List<Temperature>?,
    @SerializedName(TelemetryParsingConstant.PURITY_WATER_OUT)
    val waterOut: List<Temperature>?,
) : Parcelable

@Parcelize
data class Temperature(
    @SerializedName("ts")
    var ts: Long,
    @SerializedName("value")
    var value: String
) : Parcelable {
    companion object {
        val comparatorSortDate: Comparator<Temperature> =
            kotlin.Comparator { o1, o2 ->
                if (o1.ts == null) {
                    o1.ts = 0
                }
                if (o2.ts == null) {
                    o2.ts = 0
                }
                (o1.ts - o2.ts).toInt()
            }
    }
}

object Converter {
    fun convertToReportResponse(report: ReportWaterResponse): ReportResponse {
        return ReportResponse(
            report.waterOut ?: emptyList(),
            report.waterIn ?: emptyList()
        )
    }

    fun convertReportDataToReportResponse(report: ReportData, modelType: ModelType): ReportResponse {
        return when (modelType) {
            ModelType.FRIDGE,
            ModelType.FREEZER -> {
                ReportResponse(
                    report.temperature,
                    report.power ?: emptyList()
                )
            }
            ModelType.WATER_PURIFIER -> {
                ReportResponse(
                    report.waterOut ?: emptyList(),
                    report.waterIn ?: emptyList()
                )
            }
            ModelType.HOT_WATER_TANK -> {
                ReportResponse(
                    report.power ?: emptyList(),
                    emptyList()
                )
            }
            ModelType.SMART_FAN -> {
                ReportResponse(
                    emptyList(),
                    report.temperatureFan ?: emptyList()
                )
            }
        }
    }
}