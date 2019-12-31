package com.sixt.task.viewmodel.di

import com.sixt.task.model.CarRepository
import com.sixt.task.model.DefaultCarRepository
import com.sixt.task.model.DefaultFocalPointProvider
import com.sixt.task.model.FocalPointProvider
import com.sixt.task.util.SchedulerProvider
import com.sixt.task.viewmodel.CarViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object CarModule {

    val instance = module {

        factory<CarRepository> { DefaultCarRepository(get()) }
        factory<SchedulerProvider> { SchedulerProvider.Default }
        factory<FocalPointProvider> { DefaultFocalPointProvider() }

        viewModel {
            CarViewModel(get(),get(), get())
        }
    }
}