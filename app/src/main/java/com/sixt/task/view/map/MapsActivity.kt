package com.sixt.task.view.map

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.google.android.gms.maps.CameraUpdateFactory

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.sixt.task.R
import com.sixt.task.model.Resource
import com.sixt.task.model.vo.Car
import com.sixt.task.view.list.ListActivity
import com.sixt.task.viewmodel.CarViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private lateinit var map: GoogleMap
    private val viewModel by viewModel<CarViewModel>()

    private val listButton by lazy { findViewById<FloatingActionButton>(R.id.fab) }

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        listButton.setOnClickListener { openList() }
        viewModel.loadData()
    }

    override fun onMapReady(
        googleMap: GoogleMap
    ) {
        map = googleMap

        viewModel.cars().observe(this, Observer<Resource<List<Car>>> { resource ->
            resource?.let {
                when (resource) {
                    is Resource.Loading -> Unit
                    is Resource.Error -> it.message?.let { message -> showError(message) }
                    is Resource.Success -> {
                        it.data?.let { list -> placeCars(list) }
                        enableListButton()
                    }
                }
            }
        })

        viewModel.focalArea().observe(this, Observer { point ->
            map.animateCamera(CameraUpdateFactory.newLatLngBounds(point, 0))

            viewModel.selectedCar().observe(this, Observer { car ->
                map.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        LatLng(car.latitude, car.longitude),
                        ZOOM_STREET_LEVEL
                    )
                )


            })
        })

        map.setOnMarkerClickListener(this)
    }

    override fun onMarkerClick(marker: Marker?): Boolean {
        marker?.let {
            // TODO: add the bottom sheet detail display
        }
        return false
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadSelection()
    }

    private fun placeCars(
        cars: List<Car>
    ) {
        cars.forEach { car ->
            val coords = LatLng(car.latitude, car.longitude)
            val marker = map.addMarker(
                MarkerOptions()
                    .position(coords)
                    .icon(bitmapDescriptorFromVector(this, R.drawable.ic_car))
                    .title(car.driverName)
            )
            marker.tag = car
        }
    }

    private fun enableListButton() {
        listButton.show()
    }

    private fun openList() {
        startActivity(Intent(this, ListActivity::class.java))
    }

    private fun bitmapDescriptorFromVector(
        context: Context,
        vectorResId: Int
    ): BitmapDescriptor {
        val vectorDrawable = ContextCompat.getDrawable(context, vectorResId)

        if (vectorDrawable != null) {
            vectorDrawable.setBounds(
                0,
                0,
                vectorDrawable.intrinsicWidth,
                vectorDrawable.intrinsicHeight
            )
            val bitmap = Bitmap.createBitmap(
                vectorDrawable.intrinsicWidth,
                vectorDrawable.intrinsicHeight,
                Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(bitmap)
            vectorDrawable.draw(canvas)
            return BitmapDescriptorFactory.fromBitmap(bitmap)
        }

        throw Exception("Could not find the vector resource. Check the resource id.")
    }

    private fun showError(message: String) {
        Snackbar
            .make(window.decorView.rootView, message , Snackbar.LENGTH_INDEFINITE)
            .setAction(R.string.btn_try_again_text) { viewModel.loadData() }
            .show()
    }

    companion object {
        private const val ZOOM_STREET_LEVEL = 15f
    }
}
