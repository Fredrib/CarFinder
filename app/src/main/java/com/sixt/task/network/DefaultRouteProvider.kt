package com.sixt.task.network

import com.sixt.task.BuildConfig

class DefaultRouteProvider: RouteProvider {

    override fun getBaseUrl(): String {
        return BuildConfig.BASE_URL
    }
}