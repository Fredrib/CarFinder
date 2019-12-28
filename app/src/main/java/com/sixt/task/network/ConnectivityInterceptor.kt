package com.sixt.task.network

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class ConnectivityInterceptor(
    private val connectionVerifier: ConnectionVerifier
) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        return if (!connectionVerifier.isConnectionAvailable()) {
            throw NoConnectivityException()
        } else {
            chain.proceed(chain.request())
        }
    }
}