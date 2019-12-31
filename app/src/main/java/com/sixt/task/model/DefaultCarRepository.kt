package com.sixt.task.model

import com.sixt.task.model.vo.CarDTO
import com.sixt.task.network.ServiceApi
import io.reactivex.Maybe
import io.reactivex.Single

class DefaultCarRepository(
    private val serviceApi: ServiceApi
) : CarRepository {

    private var selectedCar: CarDTO? = null

    override fun getCars() : Single<List<CarDTO>> {
        return serviceApi.cars()
            .flatMap { cars ->
                val dto = cars.map { carVO -> CarDTO(carVO) }
                Single.just(dto)
            }
    }

    override fun selectCar(car: CarDTO) {
        selectedCar = car
    }

    override fun getSelectedCar(): Maybe<CarDTO> {
        return if (selectedCar != null) Maybe.just(selectedCar) else Maybe.empty()
    }
}