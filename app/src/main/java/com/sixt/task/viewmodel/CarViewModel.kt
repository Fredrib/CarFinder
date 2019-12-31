package com.sixt.task.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sixt.task.model.CarRepository
import com.sixt.task.model.FocalPointProvider
import com.sixt.task.model.vo.Car
import com.sixt.task.model.Resource
import com.sixt.task.model.vo.Point
import com.sixt.task.util.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable

class CarViewModel(
    private val repository: CarRepository,
    private val schedulerProvider: SchedulerProvider,
    private val focalPointProvider: FocalPointProvider
) : ViewModel() {

    private val disposables = CompositeDisposable()
    private val carsLiveData = MutableLiveData<Resource<List<Car>>>()
    private val focalPointLiveData = MutableLiveData<Point>()

    fun cars(): LiveData<Resource<List<Car>>> {
        return carsLiveData
    }

    fun loadData() {
        disposables.add(
            repository
                .getCars()
                .compose(schedulerProvider.applySchedulers())
                .doOnSubscribe { carsLiveData.value = Resource.Loading() }
                .subscribe(
                    { cars ->
                        carsLiveData.value = Resource.Success(cars)
                        focalPointLiveData.value =
                            focalPointProvider.getFocalPoint(getCarLocations(cars))
                    },
                    { error -> carsLiveData.value =
                        Resource.Error(error.message ?: DEFAULT_ERROR_MESSAGE)
                    }
                )
        )
    }

    fun focalPoint(): LiveData<Point> {
        return focalPointLiveData
    }

    private fun getCarLocations(cars : List<Car>) : List<Point> {
        return cars.map { Point(it.latitude, it.longitude) }
    }

    override fun onCleared() {
        disposables.clear()
    }

    companion object {
        const val DEFAULT_ERROR_MESSAGE = "Could not get cars location, please try again later."
    }
}