package com.example.where

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.*
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.where.databinding.ActivityMainBinding
import com.google.android.gms.location.FusedLocationProviderClient
import java.util.*


class MainActivity : AppCompatActivity() , LocationListener {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    //posizione
    private lateinit var locationManager: LocationManager
    private lateinit var tvGpsLocation: TextView
    private lateinit var nameLocation:TextView
    private val locationPermissionCode = 2


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        val button: Button = findViewById(R.id.button_get_position)
        button.setOnClickListener(View.OnClickListener {
            getLocation()
        })
    }
    private fun getLocation() {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), locationPermissionCode)
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5f, this)
    }
    override fun onLocationChanged(location: Location) {
        tvGpsLocation = findViewById(R.id.textview_lat_long)
        nameLocation = findViewById(R.id.textview_city)
        tvGpsLocation.text = "Latitude: " + location.latitude + " 7n,\n Longitude: " + location.longitude

        nameLocation.text = "Citta: "+getCityNameWithLocation(location.latitude,location.longitude)
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == locationPermissionCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }




        // declare a global variable of FusedLocationProviderClient
        lateinit var fusedLocationClient: FusedLocationProviderClient

        // in onCreate() initialize FusedLocationProviderClient
//        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context!!)

        /**
         * call this method for receive location
         * get location and give callback when successfully retrieve
         * function itself check location permission before access related methods
         *
         */
        @Override
        fun getLastKnownLocation() {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.


                return
            }
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location->
                    if (location != null) {


                        // use your location object
                        // get latitude , longitude and other info from this
                    }

                }

        }

      //  binding.fab.setOnClickListener { view ->
      //      Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
      //              .setAction("Action", null).show()
      //  }
    }

    private fun getCityNameWithLocation(lat: Double, long: Double): String {
        val geocoder = Geocoder(this, Locale.getDefault())
        val addresses = geocoder.getFromLocation(lat, long, 1)
        //val cityName = addresses!![0].getAddressLine(0)
        val address = addresses!![0].getAddressLine(0)  //via
        val comune = addresses!![0].locality   //comune
        val regione = addresses!![0].adminArea  //regione
        val country = addresses!![0].countryName // stato
        val position= "Address: $address \nComune: $comune \nRegione: $regione \nStato: $country"
        return position
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}