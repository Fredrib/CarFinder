package com.sixt.task.model

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.sixt.task.model.vo.Point

class DefaultFocalPointProvider: FocalPointProvider {

    override fun getFocalPoint(points: List<Point>): Point {
        val latLngBounds = LatLngBounds.builder()
        points.onEach { point ->
            latLngBounds.include(LatLng(point.latitude, point.longitude))
        }
        val center = latLngBounds.build().center

        return Point(center.latitude, center.longitude)
    }
}