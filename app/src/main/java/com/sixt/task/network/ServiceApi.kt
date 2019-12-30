package com.sixt.task.network

import com.sixt.task.model.vo.Car
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface ServiceApi {

    @GET("codingtask/cars")
    fun cars(): Single<List<Car>>
}