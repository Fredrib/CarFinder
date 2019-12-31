package com.sixt.task.repository

import com.sixt.task.model.CarRepository
import com.sixt.task.model.DefaultCarRepository
import com.sixt.task.util.RxImmediateSchedulerRule
import com.sixt.task.util.getCarsList
import com.sixt.task.network.ServiceApi
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
    fun `Given a sucessful response with the car list, when is requested a list of cars, then the repository should provide the cars list`() {
        val response = getCarsList()
        every { serviceApi.cars() } returns Single.just(response)

        carRepository
            .getCars()
            .test()
            .assertResult(response)
    }
}