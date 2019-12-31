package com.sixt.task.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.sixt.task.model.CarRepository
import com.sixt.task.model.FocalAreaProvider
import com.sixt.task.model.vo.CarVO
import com.sixt.task.model.Resource
import com.sixt.task.model.vo.CarDTO
import com.sixt.task.util.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable

class CarViewModel(
    private val repository: CarRepository,
    private val schedulerProvider: SchedulerProvider,
    private val focalAreaProvider: FocalAreaProvider
) : ViewModel() {

    private val disposables = CompositeDisposable()
    private val carsLiveData = MutableLiveData<Resource<List<CarDTO>>>()
    private val selectedCarLiveData = MutableLiveData<CarDTO>()
    private val focalPointLiveData = MutableLiveData<LatLngBounds>()

    fun loadData() {
        disposables.add(
            repository
                .getCars()
                .compose(schedulerProvider.applySingleSchedulers())
                .doOnSubscribe { carsLiveData.value = Resource.Loading() }
                .subscribe(
                    { cars ->
                        carsLiveData.value = Resource.Success(cars)
                        focalPointLiveData.value =
                            focalAreaProvider.getFocalArea(getCarLocations(cars))
                    },
                    { error -> carsLiveData.value =
                        Resource.Error(error.message ?: DEFAULT_ERROR_MESSAGE)
                    }
                )
        )
    }

    fun loadSelection() {
        disposables.add(
            repository
                .getSelectedCar()
                .compose(schedulerProvider.applyMaybeSchedulers())
                .subscribe { car -> selectedCarLiveData.value = car }
        )
    }

    fun cars(): LiveData<Resource<List<CarDTO>>> {
        return carsLiveData
    }

    fun focalArea(): LiveData<LatLngBounds> {
        return focalPointLiveData
    }

    fun selectedCar() : LiveData<CarDTO> {
        return selectedCarLiveData
    }

    fun selectCar(car: CarDTO) {
        repository.selectCar(car)
    }

    private fun getCarLocations(cars : List<CarDTO>) : List<LatLng> {
        return cars.map { LatLng(it.latitude, it.longitude) }
    }

    override fun onCleared() {
        disposables.clear()
        super.onCleared()
    }

    companion object {
        const val DEFAULT_ERROR_MESSAGE = "Could not get cars location, please try again later."
    }
}