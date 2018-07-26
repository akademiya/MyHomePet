package com.vadym.adv.myhomepet.di.component

import com.vadym.adv.myhomepet.AndroidApplication
import com.vadym.adv.myhomepet.di.module.AppModule
import com.vadym.adv.myhomepet.di.module.NetModule
import com.vadym.adv.myhomepet.ui.info.InfoPresenter
import com.vadym.adv.myhomepet.ui.info.InfoView
import dagger.Component

@Component(modules = [AppModule::class, NetModule::class])
interface ApplicationComponent {

    fun inject(modApplication: AndroidApplication)

    // VIEW
    fun inject(infoView: InfoView)

    // PRESENTER
    fun inject(infoPresenter: InfoPresenter)

}