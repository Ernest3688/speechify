package com.example.speechify.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.example.speechify.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import org.json.JSONObject

class WeatherFragment : Fragment() {

    var weatherUrl = "https://api.openweathermap.org/data/2.5/onecall"
    var apiKey = "0c9514931617fe40bd59e435bc854ba2"
    private lateinit var tempTV: TextView
    private lateinit var timeZoneTV: TextView
    private lateinit var descTV: TextView
    private lateinit var weatherImg: ImageView
    private lateinit var locationBtn: Button
    private lateinit var cardView: CardView
    private var REQUEST_LOCATION_PERMISSION = 1;
    private lateinit var fusedLocationClient: FusedLocationProviderClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_weather_page, container, false)
        tempTV = view.findViewById(R.id.tempTV)
        cardView = view.findViewById(R.id.weatherDetailsCardV)
        descTV = view.findViewById(R.id.descTV)
        timeZoneTV = view.findViewById(R.id.timeZoneTV)
        weatherImg = view.findViewById(R.id.weatherImg)
        locationBtn = view.findViewById(R.id.locationBtn)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        requireActivity().title = "Current Weather"


        locationBtn.setOnClickListener {

            obtainLocation()
        }

        return view

    }

    @SuppressLint("MissingPermission")
    private fun obtainLocation() {
        Log.d("weather url", weatherUrl)

        if (ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                REQUEST_LOCATION_PERMISSION
            )
            return
        } else {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    // get the latitude and longitude
                    // and create the http URL
                    weatherUrl =
                        "$weatherUrl?" + "lat=" + location?.latitude + "&lon=" + location?.longitude + "&appid=" + apiKey + "&units=metric"
                    Log.d("weather url final", weatherUrl)

                    // this function will
                    // fetch data from URL
                    getTemp()
                }.addOnFailureListener {
                    Log.d("LOCATION FAILED", it.toString())
                }
        }


    }

    private fun getTemp() {
        val queue = Volley.newRequestQueue(requireActivity())

        val stringReq = StringRequest(
            Request.Method.GET, weatherUrl,
            { response ->

                val jsonObj = JSONObject(response)

                val timeZone = jsonObj.getString("timezone")


                val current = jsonObj.getJSONObject("current")
                val weather = current.getJSONArray("weather")
                val icon = weather.getJSONObject(0).getString("icon")


                tempTV.text = "${current.getString("temp")}°C"
                descTV.text = weather.getJSONObject(0).getString("description")
                timeZoneTV.text = timeZone
                Glide.with(requireActivity()).load("https://openweathermap.org/img/wn/$icon@4x.png")
                    .into(weatherImg);
                cardView.visibility = View.VISIBLE
            },
            {
                Toast.makeText(requireActivity(), "An error occurred", Toast.LENGTH_SHORT).show()
            })
        queue.add(stringReq)
    }


    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.isNotEmpty() &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                grantResults[1] == PackageManager.PERMISSION_GRANTED
            ) {
                if (ActivityCompat.checkSelfPermission(
                        requireActivity(),
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        requireActivity(),
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    fusedLocationClient.lastLocation
                        .addOnSuccessListener { location: Location? ->

                            weatherUrl =
                                "$weatherUrl?" + "lat=" + location?.latitude + "&lon=" + location?.longitude + "&appid=" + apiKey + "&units=metric"
                            Log.d("weather url final permission", weatherUrl)


                            getTemp()
                        }.addOnFailureListener {
                            Log.d("LOCATION FAILED", it.toString())
                        }
                }


            } else {

                Toast.makeText(requireActivity(), "Enable location permission through phone settings", Toast.LENGTH_LONG).show()
            }
        }
    }


    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            WeatherFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}