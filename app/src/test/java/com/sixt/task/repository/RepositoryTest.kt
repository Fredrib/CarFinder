package com.sixt.task.repository

import com.sixt.task.model.CarRepository
import com.sixt.task.util.RxImmediateSchedulerRule
import com.sixt.task.util.getRawCarList
import com.sixt.task.network.ServiceApi
import com.sixt.task.util.getCar
import com.sixt.task.util.getTransformedCarList
import com.sixt.task.viewmodel.di.CarModule
import io.mockk.every
import io.mockk.mockk
import io.reactivex.Single
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.inject

class RepositoryTest : KoinTest {

    @Rule
    @JvmField
    var testSchedulerRule = RxImmediateSchedulerRule()

    private val serviceApi = mockk<ServiceApi>(relaxed = true)
    private val carRepository: CarRepository by inject()

    @Before
    fun setUp() {
        startKoin {
            modules(
                listOf(
                    CarModule.instance,
                    module {
                        factory(override = true) { serviceApi }
                    }
                )
            )
        }
    }

    @After
    fun shutdown() {
        stopKoin()
    }

    @Test
    fun `Given a successful response with the car list, when is requested the list of cars, then the repository must provide the cars list single`() {
        val response = getRawCarList()
        every { serviceApi.cars() } returns Single.just(response)

        val expectedResult = getTransformedCarList(response)

        carRepository
            .getCars()
            .test()
            .assertValue { it.forEachIndexed( (index, car) -> )
            }}
    }

    @Test
    fun `Given an error response, when is required the list of cars, then an exception must be thrown`() {
        val response = Throwable()
        every { serviceApi.cars() } returns Single.error(response)

        carRepository
            .getCars()
            .test()
            .assertError(response)
    }

    @Test
    fun `Given a car is selected, when is required to fetch the selected car, the car selected must be returned`() {
        val car = getCar()

        carRepository.selectCar(car)

        carRepository
            .getSelectedCar()
            .test()
            .assertResult(car)
    }

    @Test
    fun `Given a car is not selected, when is required to fetch the selected car, nothing must be returned`() {
        carRepository
            .getSelectedCar()
            .test()
            .assertResult()
    }
}