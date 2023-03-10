package com.app.func.login_demo

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.func.Location
import com.app.func.LocationAdapter
import com.app.func.R
import com.app.func.base_content.BaseFragment
import com.app.func.databinding.FragmentWeatherDemoBinding
import com.app.func.utils.Logger
import com.app.func.utils.MyToast
import com.google.android.flexbox.FlexboxLayout
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.nio.charset.Charset

class WeatherDemoFragment : BaseFragment() {

    private var binding: FragmentWeatherDemoBinding? = null
    private val locations = ArrayList<Location>()
    private lateinit var locationAdapter: LocationAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWeatherDemoBinding.inflate(inflater, container, false)
        setupRecyclerView()
        restoreSelectedPosition(savedInstanceState)
        return binding?.root
    }

    private fun setupRecyclerView() {
        binding?.listLocation?.setHasFixedSize(true)

        val layoutManager = LinearLayoutManager(activity)
        val dividerItemDecoration = DividerItemDecoration(activity, layoutManager.orientation)
        binding?.listLocation?.addItemDecoration(dividerItemDecoration)
        binding?.listLocation?.layoutManager = layoutManager

        loadData()

        locationAdapter = LocationAdapter(locations, object : LocationAdapter.OnItemClickListener {
            override fun onItemClick(location: Location) {
//                loadForecast(location.forecast)
            }
        })

        locationAdapter.currentItemClicked = {
            MyToast.showToast(activity, "Vi tri click: $it")
            loadForecast(locations[it].forecast)
        }
        binding?.listLocation?.adapter = locationAdapter
    }

    private fun loadData() {
        val json: String? = loadJsonString()
        val array: JSONArray? = loadJsonArray(json)
        loadLocations(array)
    }

    private fun loadLocations(array: JSONArray?) {
        if (array != null) {
            for (i in 0 until array.length()) {
                try {
                    val jsonObject = array[i] as JSONObject
                    val stringArray = jsonObject["forecast"] as JSONArray
                    val forecast = (0 until stringArray.length()).mapTo(ArrayList<String>()) {
                        stringArray.getString(it)
                    }
                    val location = Location(jsonObject["name"] as String, forecast)
                    locations.add(location)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun loadJsonString(): String? {
        var json: String? = null
        try {
            val inputStream =activity?.assets?.open("data.json")
            val size = inputStream?.available()
            val buffer = size?.let { ByteArray(it) }
            inputStream?.read(buffer)
            inputStream?.close()
            json = buffer?.let { String(it, Charset.forName("UTF-8")) }
        } catch (e: IOException) {
           Logger.logD("WeatherDemoFragment", e.toString())
        }
        return json
    }

    private fun loadJsonArray(json: String?): JSONArray? {
        var array: JSONArray? = null
        try {
            array = JSONArray(json)
        } catch (e: JSONException) {
            Log.e("WeatherDemoFragment", e.toString())
        }
        return array
    }

    private fun restoreSelectedPosition(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            val index = savedInstanceState.getInt(SELECTED_LOCATION_INDEX)
            if (index >= 0 && index < locations.size) {
                locationAdapter.selectedLocationIndex = index
                loadForecast(locations[index].forecast)
            }
        }
    }

    private fun loadForecast(forecast: List<String>) {
        val forecastView = binding?.layoutForecast?.forecast as FlexboxLayout
        for (i in 0 until forecastView.childCount) {
            val dayView = forecastView.getChildAt(i) as AppCompatImageView
            dayView.setImageDrawable(mapWeatherToDrawable(forecast[i]))
        }
    }

    private fun mapWeatherToDrawable(forecast: String): Drawable? {
        var drawableId = 0
        when (forecast) {
            "sun" -> drawableId = R.drawable.ic_sun
            "rain" -> drawableId = R.drawable.ic_rain
            "fog" -> drawableId = R.drawable.ic_fog
            "thunder" -> drawableId = R.drawable.ic_thunder
            "cloud" -> drawableId = R.drawable.ic_cloud
            "snow" -> drawableId = R.drawable.ic_snow
        }
        return ContextCompat.getDrawable(requireActivity(), drawableId)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(SELECTED_LOCATION_INDEX, locationAdapter.selectedLocationIndex)
    }

    companion object {

        private const val SELECTED_LOCATION_INDEX = "selectedLocationIndex"


        @JvmStatic
        fun newInstance() = WeatherDemoFragment()
    }
}