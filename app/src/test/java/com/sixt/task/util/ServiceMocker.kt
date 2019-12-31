package com.sixt.task.util

import com.sixt.task.model.vo.Car
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

fun getCarsList(): List<Car> {
    return getCarList("cars.json")
}

fun getReducedCarsList() : List<Car> {
    return getCarList("other_cars.json")
}

private fun getCarList(listFile: String) : List<Car> {
    val inputString = UnitTestUtils.getJson(listFile)
    return Gson().fromJson(inputString, getCarListType())
}

private fun getCarListType() : Type {
    return object : TypeToken<ArrayList<Car?>?>() {}.type
}