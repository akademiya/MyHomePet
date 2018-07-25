package com.vadym.adv.myhomepet

import android.app.Application
import com.vadym.adv.myhomepet.di.component.ApplicationComponent
import com.vadym.adv.myhomepet.di.component.DaggerApplicationComponent
import com.vadym.adv.myhomepet.di.module.NetModule

class AndroidApplication : Application() {

    lateinit var applicationComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()

        applicationComponent = DaggerApplicationComponent.builder()
                .netModule(NetModule())
                .build()

        applicationComponent.inject(this)
    }
}