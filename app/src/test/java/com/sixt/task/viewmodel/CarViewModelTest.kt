package com.sixt.task.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.google.android.gms.maps.model.LatLngBounds
import com.sixt.task.model.CarRepository
import com.sixt.task.model.vo.Car
import com.sixt.task.model.Resource
import com.sixt.task.util.SchedulerProvider
import com.sixt.task.util.UnitTestUtils
import com.sixt.task.util.getCar
import com.sixt.task.util.getRawCarList
import com.sixt.task.util.getReducedCarsList
import com.sixt.task.viewmodel.di.CarModule
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import io.mockk.verifySequence
import io.reactivex.Maybe
import io.reactivex.Single
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.inject

class CarViewModelTest : KoinTest {

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    private val repository = mockk<CarRepository>(relaxed = true)
    private val mockedResourceObserver = mockk<Observer<Resource<List<Car>>>>(relaxed = true)
    private val mockedFocalPointObserver = mockk<Observer<LatLngBounds>>(relaxed = true)
    private val mockedSelectedCarObserver = mockk<Observer<Car>>(relaxed = true)
    private val viewModel: CarViewModel by inject()

    @Before
    fun setUp() {
        startKoin {
            modules(
                listOf(
                    CarModule.instance,
                    module {
                        factory(override = true) { repository }
                        factory<SchedulerProvider>(override = true) { SchedulerProvider.Testing }
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
    fun `Given the car list is available, when loadData method is called, then the observer must be notified of data being loaded and then the successful cars list`() {
        viewModel.cars().observeForever(mockedResourceObserver)

        val response = getRawCarList()
        every { repository.getCars() } returns Single.just(response)

        viewModel.loadData()

        val slot1 = slot<Resource<List<Car>>>()
        val slot2 = slot<Resource<List<Car>>>()
        verifySequence {
            mockedResourceObserver.onChanged(capture(slot1))
            mockedResourceObserver.onChanged(capture(slot2))
        }

        assert(slot1.captured is Resource.Loading)
        assertEquals(response, slot2.captured.data)
    }

    @Test
    fun `Given an error occurred, when loadData method is called, then the observer must be notified of data being loaded and then the error`() {
        viewModel.cars().observeForever(mockedResourceObserver)

        val errorMessage = "Some error message"
        val response = Throwable(errorMessage)
        every { repository.getCars() } returns Single.error(response)

        viewModel.loadData()

        val slot1 = slot<Resource<List<Car>>>()
        val slot2 = slot<Resource<List<Car>>>()
        verifySequence {
            mockedResourceObserver.onChanged(capture(slot1))
            mockedResourceObserver.onChanged(capture(slot2))
        }

        assert(slot1.captured is Resource.Loading)
        val resource = slot2.captured
        assert(resource is Resource.Error && resource.message == errorMessage)
    }

    @Test
    fun `Given an error occurred without any custom message, when loadData method is called, then the observer must be notified of data being loaded and then the error with default message`() {
        viewModel.cars().observeForever(mockedResourceObserver)

        val response = Throwable()
        every { repository.getCars() } returns Single.error(response)

        viewModel.loadData()

        val slot = slot<Resource<List<Car>>>()
        verify {
            mockedResourceObserver.onChanged(capture(slot))
        }

        val resource = slot.captured
        assert(resource is Resource.Error && resource.message == "Could not get cars location, please try again later.")
    }

    @Test
    fun `Given the list of cars is provided by the service, when loadData is called, the observer must be notified of the current focal point of all cars positions `() {
        viewModel.focalArea().observeForever(mockedFocalPointObserver)

        val cars = getReducedCarsList()
        every { repository.getCars() } returns Single.just(cars)

        viewModel.loadData()

        val expectedBounds = UnitTestUtils.getBoundsFromCarPosition(cars)
        val slot = slot<LatLngBounds>()
        verify(exactly = 1) { mockedFocalPointObserver.onChanged(capture(slot)) }

        assert(slot.captured == expectedBounds)
    }

    @Test
    fun `Given a car is selected, the observer must be notified of the selection`() {
        viewModel.selectedCar().observeForever(mockedSelectedCarObserver)

        val car = getCar()
        every { repository.getSelectedCar() } returns Maybe.just(car)

        viewModel.loadSelection()

        val slot = slot<Car>()
        verify(exactly = 1) { mockedSelectedCarObserver.onChanged(capture(slot)) }

        assert(slot.captured == car)
    }
}