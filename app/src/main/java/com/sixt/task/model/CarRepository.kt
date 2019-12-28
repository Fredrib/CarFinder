package com.sixt.task.model

import com.sixt.task.model.vo.Car
import io.reactivex.Single

interface CarRepository {

    fun getCars(): Single<List<Car>>
}