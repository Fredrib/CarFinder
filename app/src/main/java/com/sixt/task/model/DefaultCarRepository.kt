package com.sixt.task.model

import com.sixt.task.model.vo.Car
import com.sixt.task.network.ServiceApi
import io.reactivex.Single

class DefaultCarRepository(
    private val serviceApi: ServiceApi
) : CarRepository {

    override fun getCars() : Single<List<Car>> {
        // TODO: add logic here
        return serviceApi.cars()
    }
}