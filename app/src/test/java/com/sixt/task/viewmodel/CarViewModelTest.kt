package com.sixt.task.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.sixt.task.model.CarRepository
import com.sixt.task.model.CarVO
import com.sixt.task.model.Resource
import com.sixt.task.util.SchedulerProvider
import com.sixt.task.util.getCarsList
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import io.mockk.verifySequence
import io.reactivex.Single
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CarViewModelTest {

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    private val repository = mockk<CarRepository>(relaxed = true)
    private val mockedObserver = mockk<Observer<Resource<List<CarVO>>>>(relaxed = true)
    private lateinit var viewModel: CarViewModel

    @Before
    fun setUp() {
        viewModel = CarViewModel(repository, SchedulerProvider.Testing)
    }

    @Test
    fun `Given the car list is available, when loadData method is called, then the observer must be notified of data being loaded and then the successful cars list`() {
        viewModel.getCars().observeForever(mockedObserver)

        val response = getCarsList()
        every { repository.getCars() } returns Single.just(response)

        viewModel.loadData()

        val slot1 = slot<Resource<List<CarVO>>>()
        val slot2 = slot<Resource<List<CarVO>>>()
        verifySequence {
            mockedObserver.onChanged(capture(slot1))
            mockedObserver.onChanged(capture(slot2))
        }

        assert(slot1.captured is Resource.Loading)
        assertEquals(response, slot2.captured.data)
    }

    @Test
    fun `Given an error occurred, when loadData method is called, then the observer must be notified of data being loaded and then the error`() {
        viewModel.getCars().observeForever(mockedObserver)

        val errorMessage = "Some error message"
        val response = Throwable(errorMessage)
        every { repository.getCars() } returns Single.error(response)

        viewModel.loadData()

        val slot1 = slot<Resource<List<CarVO>>>()
        val slot2 = slot<Resource<List<CarVO>>>()
        verifySequence {
            mockedObserver.onChanged(capture(slot1))
            mockedObserver.onChanged(capture(slot2))
        }

        assert(slot1.captured is Resource.Loading)
        val resource = slot2.captured
        assert(resource is Resource.Error && resource.message == errorMessage)
    }

    @Test
    fun `Given an error occurred but without any custom message, when loadData method is called, then the observer must be notified of data being loaded and then the error with default message`() {
        viewModel.getCars().observeForever(mockedObserver)

        val response = Throwable()
        every { repository.getCars() } returns Single.error(response)

        viewModel.loadData()

        val slot = slot<Resource<List<CarVO>>>()
        verify {
            mockedObserver.onChanged(capture(slot))
        }

        val resource = slot.captured
        assert(resource is Resource.Error && resource.message == "Could not get cars location, please try again later.")
    }
}