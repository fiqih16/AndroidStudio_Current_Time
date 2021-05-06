package com.fiqih.latgooglemapscurrenttime

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.*

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_itemhistory.*
import kotlinx.android.synthetic.main.history_activity.*
import java.util.*
import kotlin.collections.ArrayList

class MapsActivity : AppCompatActivity() {

    private lateinit var mMap: GoogleMap
    private var fusedLocationClient: FusedLocationProviderClient? = null
    private lateinit var currentAddress: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        setupListOfDataIntoRecyclerView()
        getCurrentLocation()
        addRecord()
    }
    @SuppressLint("RestrictedApi")
    fun getCurrentLocation() {
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment

        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        val locationRequest = LocationRequest().setInterval(3000)
            .setFastestInterval(3000).setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this@MapsActivity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),1)
            return
        }
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            object : LocationCallback(){
                override fun onLocationResult(p0: LocationResult) {
                    super.onLocationResult(p0)
                    for (location in p0.locations){
                        mapFragment.getMapAsync( OnMapReadyCallback {
                            mMap = it
                            if (ActivityCompat.checkSelfPermission(this@MapsActivity,
                                    Manifest.permission.ACCESS_FINE_LOCATION
                                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                                    this@MapsActivity,
                                    Manifest.permission.ACCESS_COARSE_LOCATION
                                ) != PackageManager.PERMISSION_GRANTED) {
//                                    ActivityCompat.requestPermissions(this@MapsActivity,
//                                            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),1)
                            }
                            mMap.isMyLocationEnabled = true
                            mMap.uiSettings.isZoomControlsEnabled = true
                            val locationResult = LocationServices
                                .getFusedLocationProviderClient(this@MapsActivity).lastLocation

                            locationResult.addOnCompleteListener(this@MapsActivity) {
                                if (it.isSuccessful && it.result != null){
                                    var currentLocation = it.result
                                    var currentLatitude = currentLocation.latitude
                                    var currentLongitude = currentLocation.longitude
                                    mMap.clear()
                                    val geocoder = Geocoder(this@MapsActivity)
                                    val geoCoderResult  = geocoder.getFromLocation(currentLocation.latitude,
                                        currentLocation.longitude, 1)


                                    var myLocation = LatLng(currentLatitude, currentLongitude)
                                    mMap.addMarker(MarkerOptions().position(myLocation)
                                        .title(geoCoderResult[0].getAddressLine(0))).showInfoWindow()
                                    mMap.moveCamera(CameraUpdateFactory.newLatLng(myLocation))
                                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 15f))
                                }
                            }
                        })
                    }
                }
            },
            Looper.myLooper()
        )

    }


    // Panggil menu toolbar infiate ke Main Activity
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.drawer_menu, menu)
        return true
    }

    // Toolbar item yang diklik
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when(id) {
            R.id.toolbar_upright_Favorite -> {
                val intent = Intent(applicationContext, itemhistory::class.java)
                startActivity(intent)
                true
            }
            else -> false
        }
        return super.onOptionsItemSelected(item)
    }

    private fun addRecord(){

        val namakeg = Tv_kegia.text.toString()
//        val waktu = Tv_Waktu.text.toString()
//
//        val lok = Tv_Lok.text.toString()

        val databaseHandler: DatabaseHandler = DatabaseHandler(this)
        if (!namakeg.isEmpty()){
            val status = databaseHandler.addActivities(MpModel(0, namakeg))
            if (status > -1){
                Toast.makeText( this, "Berhasil Menambahkan Record", Toast.LENGTH_SHORT).show()
                Tv_kegia.text
//                Tv_Waktu.text.clear()
//
//                Tv_Lok.text.clear()

            }
        }else{
            Toast.makeText( this,"Masukkan Nama dan email anda", Toast.LENGTH_SHORT).show()
        }
    }


    // method untuk mendapatkan jumlah record
    private fun getItemList(): ArrayList<MpModel>{
        val databaseHandler: DatabaseHandler = DatabaseHandler(this)
        val empList: ArrayList<MpModel> = databaseHandler.viewActivities()
        return empList
    }


    // method untuk menampilkan emplist ke recycler view
    private fun  setupListOfDataIntoRecyclerView(){
        if (getItemList().size > 0){
            rv_history.visibility = View.VISIBLE

            rv_history.layoutManager = LinearLayoutManager(this)
            rv_history.adapter =  itemAdapter(this,getItemList())
        }
    }


}