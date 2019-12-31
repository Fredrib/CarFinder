package com.sixt.task.network

import com.sixt.task.model.vo.CarVO
import io.reactivex.Single
import retrofit2.http.GET

interface ServiceApi {

    @GET("codingtask/cars")
    fun cars(): Single<List<CarVO>>
}