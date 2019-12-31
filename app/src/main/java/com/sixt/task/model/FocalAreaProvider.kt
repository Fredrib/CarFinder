package com.sixt.task.model

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds

interface FocalAreaProvider {

    fun getFocalArea(points: List<LatLng>) : LatLngBounds
}