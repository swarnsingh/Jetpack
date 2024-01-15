package com.swarn.jetpack

import com.swarn.jetpack.data.remote.ApiService
import com.swarn.jetpack.data.remote.repository.PhotosRepository
import com.swarn.jetpack.ui.home.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

private val viewModelModules = module {
    viewModelOf(::HomeViewModel)
}

private val networkModules = module {
    single<ApiService> {
        ApiService.create()
    }
}

private val repositoryModules = module {
    factoryOf(::PhotosRepository)
}

val jetpackModules = module {
    includes(viewModelModules, networkModules, repositoryModules)
}
