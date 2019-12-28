package com.sixt.task.network

import io.mockk.every
import io.mockk.mockk
import okhttp3.Interceptor
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException

class ConnectivityInterceptorTest {

    private lateinit var interceptor: ConnectivityInterceptor
    private val connectionVerifier: ConnectionVerifier = mockk(relaxed = true)
    private val chain: Interceptor.Chain = mockk(relaxed = true)

    @get:Rule
    val thrown: ExpectedException = ExpectedException.none()

    @Before
    fun setUp() {
        interceptor = ConnectivityInterceptor(connectionVerifier)
    }

    @Test(expected = NoConnectivityException::class)
    fun `interceptor should throw NoConnectivityException when connection not available`() {
        every { connectionVerifier.isConnectionAvailable() } returns false

        interceptor.intercept(chain)
    }
}