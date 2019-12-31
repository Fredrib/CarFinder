package com.sixt.task.model.vo

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

data class Car(

    @SerializedName("id")
    val id: String,

    @SerializedName("modelIdentifier")
    val identifier: String,

    @SerializedName("modelName")
    val model: String,

    @SerializedName("name")
    val driverName: String,

    @SerializedName("make")
    val make: String,

    @SerializedName("group")
    val group: String,

    @SerializedName("color")
    val color: String,

    @SerializedName("series")
    val series: String,

    @SerializedName("fuelType")
    val fuelType: String,

    @SerializedName("fuelLevel")
    val fuelLevel: Float,

    @SerializedName("transmission")
    val transmission: String,

    @SerializedName("licensePlate")
    val licensePlate: String,

    @SerializedName("latitude")
    val latitude: Double,

    @SerializedName("longitude")
    val longitude: Double,

    @SerializedName("innerCleanliness")
    val innerCleanliness: String,

    @SerializedName("carImageUrl")
    val carImageUrl: String
)