package com.sixt.task.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.google.android.gms.maps.CameraUpdateFactory

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.sixt.task.R
import com.sixt.task.model.Resource
import com.sixt.task.model.vo.Car
import com.sixt.task.viewmodel.CarViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var map: GoogleMap
    private val viewModel by viewModel<CarViewModel>()

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        viewModel.loadData()
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(
        googleMap: GoogleMap
    ) {
        map = googleMap

        viewModel.cars().observe(this, Observer<Resource<List<Car>>> { resource ->
            resource?.let {
                when (resource) {
                    is Resource.Loading -> Log.e("ListFragment", "showing loading")
                    is Resource.Error -> Log.e("ListFragment", "showing error")
                    is Resource.Success -> it.data?.let { list -> placeCars(list) }
                }
            }
        })

        viewModel.focalPoint().observe(this, Observer { point ->
            map.moveCamera(CameraUpdateFactory.newLatLng(LatLng(point.latitude, point.longitude)))
        })
    }

    private fun placeCars(
        cars: List<Car>
    ) {
        cars.forEach {car ->
            val coords = LatLng(car.latitude, car.longitude)
            map.addMarker(
                MarkerOptions()
                    .position(coords)
                    .icon(bitmapDescriptorFromVector(this, R.drawable.ic_car))
                    .title(car.driverName)
            )
        }
    }

    private fun bitmapDescriptorFromVector(
        context: Context,
        vectorResId:Int
    ): BitmapDescriptor {
        val vectorDrawable = ContextCompat.getDrawable(context, vectorResId)

        if (vectorDrawable != null) {
            vectorDrawable.setBounds(0, 0, vectorDrawable.intrinsicWidth, vectorDrawable.intrinsicHeight)
            val bitmap = Bitmap.createBitmap(
                vectorDrawable.intrinsicWidth,
                vectorDrawable.intrinsicHeight,
                Bitmap.Config.ARGB_8888
            )
            val canvas =  Canvas(bitmap)
            vectorDrawable.draw(canvas)
            return BitmapDescriptorFactory.fromBitmap(bitmap)
        }

        throw Exception("Could not find the vector resource. Check the resource id.")
    }
}
