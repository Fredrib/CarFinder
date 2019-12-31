package com.sixt.task.model

import com.sixt.task.model.vo.Car
import com.sixt.task.network.ServiceApi
import io.reactivex.Maybe
import io.reactivex.Single

class DefaultCarRepository(
    private val serviceApi: ServiceApi
) : CarRepository {

    private var selectedCar: Car? = null

    override fun getCars() : Single<List<Car>> {
        return serviceApi.cars()
            .map { cars ->
                cars.onEach { car ->
                    car.fuelType = getFullTypeName(car.fuelType)
                    car.transmission = getTransmissionName(car.transmission)
                }
            }
    }

    override fun selectCar(car: Car) {
        selectedCar = car
    }

    override fun getSelectedCar(): Maybe<Car> {
        return if (selectedCar != null) Maybe.just(selectedCar) else Maybe.empty()
    }

    private fun getFullTypeName(fuelType: String) : String {
        return when (fuelType) {
            "D" -> "Diesel"
            "P" -> "Petrol"
            else -> fuelType
        }
    }

    private fun getTransmissionName(transmission: String) : String {
        return when (transmission) {
            "M" -> "Manual"
            "A" -> "Automatic"
            else -> transmission
        }
    }
}