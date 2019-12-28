package com.sixt.task.network

import java.lang.Exception

abstract class NetworkException(
    override val message: String,
    override val cause: Throwable?
): Exception() {
    companion object {
        const val NETWORK_CONNECTIVITY_ERROR_MESSAGE =
            "No internet connection."
        const val NETWORK_GENERIC_ERROR_MESSAGE =
            "Something went wrong, please try again later."
        const val TIMEOUT_ERROR_MESSAGE =
            "Could not connect with our service, please verify your internet connection."
    }
}


class NoConnectivityException(cause: Throwable? = null) :
    NetworkException (NETWORK_CONNECTIVITY_ERROR_MESSAGE, cause)

