package com.sixt.task.util

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.sixt.task.model.vo.Car
import java.io.IOException
import java.nio.charset.StandardCharsets

object UnitTestUtils {

    fun getJson(fileName: String): String? {
        val json: String?
        json = try {
            val classLoader = ClassLoader.getSystemClassLoader()
            val inputStream = classLoader.getResourceAsStream(fileName)
            val size = inputStream.available()
            val buffer = ByteArray(size)

            inputStream.read(buffer)
            inputStream.close()

            String(buffer, StandardCharsets.UTF_8)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }

        return json
    }

    fun getBoundsFromCarPosition(cars: List<Car>) : LatLngBounds {
        val builder = LatLngBounds.builder()
        cars.forEach { car -> builder.include(LatLng(car.latitude, car.longitude)) }
        return builder.build()
    }
}