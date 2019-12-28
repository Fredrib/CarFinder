package com.sixt.task.util

import com.sixt.task.model.vo.Car
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

fun getCarsList(): List<Car> {
    val inputString = UnitTestUtils.getJson("cars.json")
    val groupListType: Type = object : TypeToken<ArrayList<Car?>?>() {}.type

    return Gson().fromJson(inputString, groupListType)
}