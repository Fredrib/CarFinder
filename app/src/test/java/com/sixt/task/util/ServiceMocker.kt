package com.sixt.task.util

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sixt.task.model.vo.CarDTO
import com.sixt.task.model.vo.CarVO
import com.sixt.task.util.UnitTestUtils.getJson
import java.lang.reflect.Type

fun getRawCarList(): List<CarVO> {
    return getCarList("cars.json")
}

fun getCarList() : List<CarDTO> {
    return getTransformedCarList(getRawCarList())
}

fun getTransformedCarList(list: List<CarVO>): List<CarDTO> {
    return list.map { CarDTO(it) }
}

fun getReducedCarsList() : List<CarDTO> {
    return getTransformedCarList(getCarList("other_cars.json"))
}

fun getCar() : CarDTO {
    return getObject("car.json", CarDTO::class.java)
}

private fun <T> getObject(fileName: String, classType: Class<T>): T {
    val json = getJson(fileName, classType.classLoader!!)
    return Gson().fromJson(json, classType)
}

private fun getCarList(listFile: String) : List<CarVO> {
    val inputString = getJson(listFile)
    return Gson().fromJson(inputString, getCarListType())
}

private fun getCarListType() : Type {
    return object : TypeToken<ArrayList<CarVO?>?>() {}.type
}