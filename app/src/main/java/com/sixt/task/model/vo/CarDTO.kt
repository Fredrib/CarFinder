package com.sixt.task.model.vo

import java.math.RoundingMode

class CarDTO(
    private val car: CarVO
) {

    val name: String
        get() = car.driverName

    val model: String
        get() = car.model

    val make: String
        get() = car.make

    val plate: String
        get() = car.licensePlate

    val transmission: String
        get() = getTransmissionName(car.transmission)

    val fuelType: String
        get() = getFullTypeName(car.fuelType)

    val fuelLevel: String
        get() = getFuelLevel(car.fuelLevel)

    val color: String
        get() = getColor(car.color)

    val cleanliness: String
        get() = car.innerCleanliness

    val latitude: Double
        get() = car.latitude

    val longitude: Double
        get() = car.longitude

    val carImageUrl: String
        get() = car.carImageUrl

    private fun getFullTypeName(fuelType: String): String {
        return when (fuelType) {
            "D" -> "Diesel"
            "P" -> "Petrol"
            else -> fuelType
        }
    }

    private fun getTransmissionName(transmission: String): String {
        return when (transmission) {
            "M" -> "Manual"
            "A" -> "Automatic"
            else -> transmission
        }
    }

    private fun getFuelLevel(level: Float): String {
        return "${(level * 0.01).toBigDecimal().setScale(2, RoundingMode.HALF_EVEN)}%"
    }

    private fun getColor(color: String): String {
        return color.replace("_", " ").capitalize()
    }
}