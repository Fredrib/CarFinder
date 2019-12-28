package com.sixt.task.network.di

import com.sixt.task.network.ConnectionVerifier
import com.sixt.task.network.ConnectivityInterceptor
import com.sixt.task.network.DefaultRouteProvider
import com.sixt.task.network.RouteProvider
import com.sixt.task.network.ServiceApi
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object NetworkModule {

    private fun okHttp(
        connectivityInterceptor: ConnectivityInterceptor
    ) =
        OkHttpClient.Builder()
            .addInterceptor(connectivityInterceptor)
            .build()

    private fun retrofit(
        client: OkHttpClient,
        routeProvider: RouteProvider
    ) =
        Retrofit.Builder()
            .baseUrl(routeProvider.getBaseUrl())
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(client)
            .build()

    val instance = module {
        factory { ConnectionVerifier(get()) }
        single { ConnectivityInterceptor(get()) }

        single { okHttp(get()) }

        factory<RouteProvider> { DefaultRouteProvider() }
        single { retrofit(get(), get()) }
        single { get<Retrofit>().create(ServiceApi::class.java) }
    }
}