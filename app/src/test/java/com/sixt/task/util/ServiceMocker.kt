package com.sixt.task.util

import com.sixt.task.model.vo.Car
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sixt.task.util.UnitTestUtils.getJson
import java.lang.reflect.Type

fun getRawCarList(): List<Car> {
    return getCarList("cars.json")
}

fun getTransformedCarList(list: List<Car>): List<Car> {
    return list.onEach { car ->
        car.fuelType = getFullTypeName(car.fuelType)
        car.transmission = getTransmissionName(car.transmission)
    }
}

fun getReducedCarsList() : List<Car> {
    return getCarList("other_cars.json")
}

fun getCar() : Car {
    return getObject("car.json", Car::class.java)
}

private fun <T> getObject(fileName: String, classType: Class<T>): T {
    val json = getJson(fileName, classType.classLoader!!)
    return Gson().fromJson(json, classType)
}

private fun getCarList(listFile: String) : List<Car> {
    val inputString = UnitTestUtils.getJson(listFile)
    return Gson().fromJson(inputString, getCarListType())
}

private fun getCarListType() : Type {
    return object : TypeToken<ArrayList<Car?>?>() {}.type
}

private fun getFullTypeName(fuelType: String) : String {
    return when (fuelType) {
        "D" -> "Diesel"
        "P" -> "Petrol"
        else -> fuelType
    }
}

private fun getTransmissionName(transmission: String) : String {
    return when (transmission) {
        "M" -> "Manual"
        "A" -> "Automatic"
        else -> transmission
    }
}