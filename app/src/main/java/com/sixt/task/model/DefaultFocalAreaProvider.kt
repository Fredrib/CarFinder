package com.sixt.task.model

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds

class DefaultFocalAreaProvider: FocalAreaProvider {

    override fun getFocalArea(points: List<LatLng>): LatLngBounds {
        val latLngBounds = LatLngBounds.builder()
        points.onEach { point ->
            latLngBounds.include(point)
        }
        return latLngBounds.build()
    }
}