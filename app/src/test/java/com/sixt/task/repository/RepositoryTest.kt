package com.sixt.task.repository

import com.sixt.task.model.DefaultCarRepository
import com.sixt.task.util.RxImmediateSchedulerRule
import com.sixt.task.util.getCarsList
import com.sixt.task.network.ServiceApi
import io.mockk.every
import io.mockk.mockk
import io.reactivex.Single
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class RepositoryTest {

    @Rule
    @JvmField
    var testSchedulerRule = RxImmediateSchedulerRule()

    private val serviceApi = mockk<ServiceApi>(relaxed = true)
    private lateinit var defaultCarRepository: DefaultCarRepository

    @Before
    fun setUp() {
        defaultCarRepository = DefaultCarRepository(serviceApi)
    }

    @Test
    fun `Given a sucessful response with the car list, when is requested a list of cars, then the repository should provide the cars list`() {
        val response = getCarsList()
        every { serviceApi.cars() } returns Single.just(response)

        defaultCarRepository
            .getCars()
            .test()
            .assertResult(response)
    }
}