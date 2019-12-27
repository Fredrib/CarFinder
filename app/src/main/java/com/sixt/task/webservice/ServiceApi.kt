package com.sixt.task.webservice

import com.sixt.task.model.CarVO
import io.reactivex.Single

interface ServiceApi {

    fun fetchData(): Single<List<CarVO>>
}