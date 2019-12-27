package com.sixt.task.util

import com.sixt.task.model.CarVO

fun getCarsList(): List<CarVO> {
    return UnitTestUtils.getListObjectFromArray("cars.json", CarVO::class.java)
}