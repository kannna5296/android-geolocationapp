package com.android.example.geolocationapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import java.net.URLEncoder

class MainActivity : AppCompatActivity() {

    private var _latitude = 0.0
    private var _longitude = 0.0

    private lateinit var _fusedLocationClient : FusedLocationProviderClient
    private lateinit var _locationRequest : LocationRequest
    private lateinit var _onUpdateLocation : OnUpdateLocation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        _fusedLocationClient = LocationServices.getFusedLocationProviderClient(this@MainActivity)
        _locationRequest = LocationRequest.Builder(5000)
            .setMinUpdateIntervalMillis(1000)
            .setPriority(Priority.PRIORITY_HIGH_ACCURACY).build()
        _onUpdateLocation = OnUpdateLocation()
    }

    override fun onResume() {
        super.onResume()
        // permissionチェック
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED ) {
            val permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
            ActivityCompat.requestPermissions(this@MainActivity,permissions,1000)
            return
        }
        _fusedLocationClient.requestLocationUpdates(_locationRequest,_onUpdateLocation,mainLooper)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == 1000 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if(ActivityCompat.checkSelfPermission(this@MainActivity,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                return
        }
        _fusedLocationClient.requestLocationUpdates(_locationRequest,_onUpdateLocation,mainLooper)
    }

    override fun onPause() {
        super.onPause()
        _fusedLocationClient.removeLocationUpdates(_onUpdateLocation)
    }

    fun onMapShowCurrentButtonClick(view: View) {
        val uriStr = "geo:${_latitude},${_longitude}"

        val uri = Uri.parse(uriStr)

        val intent = Intent(Intent.ACTION_VIEW,uri)
        startActivity(intent)
    }

    fun onMapSearchButtonClick(view: View) {
        val etSearchWord = findViewById<EditText>(R.id.etSearchWord)
        val searchWord = URLEncoder.encode(etSearchWord.text.toString(),"UTF-8")

        // "geo"で地図アプリを出す。地図アプリが複数ある場合は選べる
        val uriStr = "geo:0,0%q=${searchWord}"

        val uri = Uri.parse(uriStr)

        val intent = Intent(Intent.ACTION_VIEW,uri)
        startActivity(intent)
    }

    private inner class OnUpdateLocation : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val location = locationResult.lastLocation
            location?.let {
                _latitude = location.latitude
                _longitude = location.longitude
                val tvLatitude = findViewById<TextView>(R.id.tvLatitude)
                tvLatitude.text = _latitude.toString()
                val tvLongitude = findViewById<TextView>(R.id.tvLongitude)
                tvLongitude.text = _longitude.toString()
            }
        }
    }
}