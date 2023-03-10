package com.app.func

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.app.func.databinding.ActivityViewCustomBinding
import com.app.func.utils.Logger
import com.app.func.view.chart.stock.WaterTankTemperatureView
import com.app.func.view.seekbarcustom.crollerTest.Croller
import com.app.func.view.seekbarcustom.crollerTest.OnCrollerChangeListener
import com.app.func.view.seekbarcustom.crollerTest.OnProgressChangedListener

class ViewCustomActivity : AppCompatActivity() {

    private lateinit var binding: ActivityViewCustomBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewCustomBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.customSeekBar.setValueRange(1f, 18f)
        binding.customSeekBar.setValueCurrent(12f)

        binding.customSeekBar.onValueProgress = {
            binding.toolTipSeekBar.visibility = View.VISIBLE
            binding.toolTipSeekBar.x = it
        }

        binding.customSeekBar.onValueSelected = {
            binding.toolTipSeekBar.setContentString(it.toInt().toString())
        }

        binding.customSeekBar.actionShowHideTooltip = {
            if (it) {
                binding.consToolTip.visibility = View.VISIBLE
            } else {
                binding.consToolTip.visibility = View.GONE
            }
        }

        binding.viewTempProgress.setMinMaxProgress(40, 75)
        binding.viewTempProgress.setTemp(20)
        binding.viewTempProgress.setTemperatureTitle(20.toString())
        binding.viewTempProgress.settingTemp(50)

        binding.waterViewTempProgress.setMinMaxProgress(40, 75)
        binding.waterViewTempProgress.setTemp(20)
        binding.waterViewTempProgress.setTemperatureTitle(20.toString())
        binding.waterViewTempProgress.settingTemp(50)
        binding.waterViewTempProgress.updateState(WaterTankTemperatureView.State.WORKING)

        initScrollSeekbar()

    }

    private fun initScrollSeekbar() {
//        binding.crollerSeekbar.setIndicatorWidth(10f)
//        binding.crollerSeekbar.setBackCircleColor(Color.parseColor("#EDEDED"))
//        binding.crollerSeekbar.setMainCircleColor(Color.WHITE)
//        binding.crollerSeekbar.setMax(50)
//        binding.crollerSeekbar.setStartOffset(45)
//        binding.crollerSeekbar.setIsContinuous(false)
//        binding.crollerSeekbar.setLabelColor(Color.BLACK)
//        binding.crollerSeekbar.setProgressPrimaryColor(Color.parseColor("#0B3C49"))
//        binding.crollerSeekbar.setIndicatorColor(Color.parseColor("#0B3C49"))
//        binding.crollerSeekbar.setProgressSecondaryColor(Color.parseColor("#EEEEEE"))

        binding.crollerSeekbar.setOnCrollerChangeListener(object : OnCrollerChangeListener {
            override fun onProgressChanged(croller: Croller?, progress: Int) {
                Logger.logD(
                    "croller_onProgressChanged",
                    "setOnCrollerChangeListener ----  $progress"
                )
            }

            override fun onStartTrackingTouch(croller: Croller?) {

            }

            override fun onStopTrackingTouch(croller: Croller?) {

            }
        })

        binding.crollerSeekbar.setOnProgressChangedListener(object : OnProgressChangedListener {
            override fun onProgressChanged(progress: Int) {
                // use the progress
                Logger.logD(
                    "croller_onProgressChanged",
                    "setOnProgressChangedListener ------ $progress"
                )
            }
        })
    }

    private fun main() {
        data class MotorVehicle(
            val name: String,
            val model: Int,
            val manufacturer: String
        )

        val cars = listOf(
            MotorVehicle("Swift", 2016, "Maruti"),
            MotorVehicle("Altroz", 2020, "Tata"),
            MotorVehicle("Verna", 2019, "Hyundai")
        )
        val bikes = listOf(
            MotorVehicle("R-15", 2018, "Yamaha"),
            MotorVehicle("Gixxer", 2017, "Suzuki")
        )

        val numbers = listOf(1, 2, 3, 4, 5)
        val square = numbers.forEach {
            it * it
        }
        val squaredNumbers = numbers.map {
            it * it
        }
        println(square)
        println(squaredNumbers)

        val allVehicles = mutableListOf<MotorVehicle>()
        val vehicles = listOf(cars, bikes)
        allVehicles.addAll(cars)
        allVehicles.addAll(bikes)
        val all = vehicles.map { }
        val all1 = vehicles.flatMap { it }
        val manufacturer = vehicles.flatMap { it }.map { it.manufacturer }
        println(allVehicles)
        println(vehicles)
    }
}