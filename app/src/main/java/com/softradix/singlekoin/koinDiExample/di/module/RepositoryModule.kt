package com.softradix.singlekoin.koinDiExample.di.module

import com.softradix.singlekoin.koinDiExample.repository.MainRepository
import org.koin.dsl.module

val repoModule = module {
    single {
        MainRepository(get())
    }
}