package com.sixt.task.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sixt.task.model.CarRepository
import com.sixt.task.model.CarVO
import com.sixt.task.model.Resource
import com.sixt.task.util.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable

class CarViewModel(
    private val repository: CarRepository,
    private val schedulerProvider: SchedulerProvider
) : ViewModel() {

    private val carsLiveData = MutableLiveData<Resource<List<CarVO>>>()
    private val disposables = CompositeDisposable()

    fun getCars(): LiveData<Resource<List<CarVO>>> {
        return carsLiveData
    }

    fun loadData() {
        disposables.add(
            repository
                .getCars()
                .compose(schedulerProvider.applySchedulers())
                .doOnSubscribe { carsLiveData.value = Resource.Loading() }
                .subscribe(
                    { cars -> carsLiveData.value = Resource.Success(cars) },
                    { error -> carsLiveData.value =
                        Resource.Error(error.message ?: DEFAULT_ERROR_MESSAGE)
                    }
                )
        )
    }

    override fun onCleared() {
        disposables.clear()
    }

    companion object {
        const val DEFAULT_ERROR_MESSAGE = "Could not get cars location, please try again later."
    }
}