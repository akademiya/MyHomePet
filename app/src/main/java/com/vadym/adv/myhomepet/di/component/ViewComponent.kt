package com.vadym.adv.myhomepet.di.component

import com.vadym.adv.myhomepet.AndroidApplication
import com.vadym.adv.myhomepet.ui.info.InfoView
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(dependencies = [(ApplicationComponent::class)])
internal interface ViewComponent {


    fun inject(application: AndroidApplication)

    // ACTIVITY


    // VIEW
    fun inject(infoView: InfoView)
}