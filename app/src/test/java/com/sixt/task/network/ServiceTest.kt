package com.sixt.task.network

import com.sixt.task.network.di.NetworkModule
import com.sixt.task.util.MockResponseFileReader
import com.sixt.task.util.getCarsList
import io.mockk.every
import io.mockk.mockk
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.inject
import retrofit2.HttpException
import java.net.HttpURLConnection

class ServiceTest : KoinTest {

    private lateinit var server: MockWebServer
    private val routeProvider: RouteProvider = mockk(relaxed = true)
    private val connectionVerifier: ConnectionVerifier = mockk(relaxed = true)

    private val service: ServiceApi by inject()

    @Before
    fun setUp() {
        server = MockWebServer()

        val serverUrl = server.url("/")

        every { routeProvider.getBaseUrl() } returns serverUrl.url().toString()
        every { connectionVerifier.isConnectionAvailable() } returns true

        startKoin {
            modules(
                listOf(
                    NetworkModule.instance,
                    module {
                        factory(override = true) { routeProvider }
                        factory(override = true) { connectionVerifier }
                    }
                )
            )
        }
    }

    @After
    fun shutdown() {
        stopKoin()
        server.shutdown()
    }

    @Test
    fun `Given a successful response with the car list is provided, when the cars() service api is called, then the service must respond with the same car list`() {
        val response = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(MockResponseFileReader("cars.json").content)

        val expectedResponse = getCarsList()

        server.enqueue(response)

        service
            .cars()
            .test()
            .assertResult(expectedResponse)
    }

    @Test
    fun `Given there is no connectivity, when the cars() service api is called, then the service must respond with a NoConnectivityException`() {
        every { connectionVerifier.isConnectionAvailable() } returns false

        service
            .cars()
            .test()
            .assertError(NoConnectivityException::class.java)
    }

    @Test
    fun `Given an error response is provided, when the cars() service api is called, then the service must respond with a HttpException`() {
        val response = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_BAD_REQUEST)
            .setBody("")

        server.enqueue(response)

        service
            .cars()
            .test()
            .assertError(HttpException::class.java)
    }
}