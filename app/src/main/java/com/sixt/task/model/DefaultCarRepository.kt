package com.sixt.task.model

import com.sixt.task.webservice.ServiceApi
import io.reactivex.Single

class DefaultCarRepository(
    private val serviceApi: ServiceApi
) : CarRepository {

    override fun getCars() : Single<List<CarVO>> {
        // TODO: add logic here
        return serviceApi.fetchData()
    }
}