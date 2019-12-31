package com.sixt.task.model

import com.sixt.task.model.vo.Car
import io.reactivex.Maybe
import io.reactivex.Single

interface CarRepository {

    fun getCars(): Single<List<Car>>
    fun selectCar(car: Car)
    fun getSelectedCar(): Maybe<Car>
}