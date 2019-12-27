package com.sixt.task.model

import io.reactivex.Single

interface CarRepository {

    fun getCars(): Single<List<CarVO>>
}