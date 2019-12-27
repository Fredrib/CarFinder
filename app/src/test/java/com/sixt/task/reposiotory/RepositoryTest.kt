package com.sixt.task.reposiotory

import com.sixt.task.model.DefaultCarRepository
import com.sixt.task.util.RxImmediateSchedulerRule
import com.sixt.task.util.getCarsList
import com.sixt.task.webservice.ServiceApi
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
    fun `when is requested a list of cars, then repository should provide the cars list`() {
        val response = getCarsList()
        every { serviceApi.fetchData() } returns Single.just(response)

        defaultCarRepository
            .getCars()
            .test()
            .assertResult(response)
    }
}