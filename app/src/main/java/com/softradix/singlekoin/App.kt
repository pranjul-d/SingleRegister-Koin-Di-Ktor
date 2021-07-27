package com.softradix.singlekoin

import android.app.Application
import com.softradix.singlekoin.koinDiExample.di.module.appModule
import com.softradix.singlekoin.koinDiExample.di.module.repoModule
import com.softradix.singlekoin.koinDiExample.di.module.viewModelModule
import io.ktor.util.*
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

@KtorExperimentalAPI
class App : Application() {

    init {
        startKoin {
            androidContext(this@App)
//       modules(module {
//           single { initKtorClient() }
//       })

            modules(
                listOf(
                    appModule,
                    repoModule,
                    viewModelModule,
//                    module { single { initKtorClient() } }
                    )
            )

        }
    }
}