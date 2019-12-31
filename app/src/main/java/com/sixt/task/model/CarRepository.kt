package com.sixt.task.model

import com.sixt.task.model.vo.CarDTO
import com.sixt.task.model.vo.CarVO
import io.reactivex.Maybe
import io.reactivex.Single

interface CarRepository {

    fun getCars(): Single<List<CarDTO>>
    fun selectCar(car: CarDTO)
    fun getSelectedCar(): Maybe<CarDTO>
}