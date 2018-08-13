package com.vadym.adv.myhomepet.di.component

import com.vadym.adv.myhomepet.AndroidApplication
import com.vadym.adv.myhomepet.ui.main.MainActivity
import com.vadym.adv.myhomepet.ui.main.MainPresenter
import com.vadym.adv.myhomepet.di.module.AppModule
import com.vadym.adv.myhomepet.di.module.NetModule
import com.vadym.adv.myhomepet.ui.info.InfoPresenter
import com.vadym.adv.myhomepet.ui.info.InfoView
import dagger.Component

@Component(modules = [AppModule::class, NetModule::class, NetModule::class])
interface ApplicationComponent {

    fun inject(modApplication: AndroidApplication)

    // VIEW
    fun inject(infoView: InfoView)
    fun inject(mainView: MainActivity)

    // PRESENTER
    fun inject(infoPresenter: InfoPresenter)
    fun inject(mainPresenter: MainPresenter)

}