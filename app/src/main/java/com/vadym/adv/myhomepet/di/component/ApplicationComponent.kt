package com.vadym.adv.myhomepet.di.component

import com.vadym.adv.myhomepet.AndroidApplication
import com.vadym.adv.myhomepet.di.module.AppModule
import com.vadym.adv.myhomepet.di.module.NetModule
import com.vadym.adv.myhomepet.ui.info.InfoPresenter
import com.vadym.adv.myhomepet.ui.info.InfoView
import com.vadym.adv.myhomepet.ui.login.view.LoginActivity
import com.vadym.adv.myhomepet.ui.login.presenter.LoginPresenter
import com.vadym.adv.myhomepet.ui.login.view.SignupActivity
import com.vadym.adv.myhomepet.ui.login.presenter.SignupPresenter
import com.vadym.adv.myhomepet.ui.main.MainActivity
import com.vadym.adv.myhomepet.ui.main.MainPresenter
import com.vadym.adv.myhomepet.ui.pet.presenter.EditPetPresenter
import com.vadym.adv.myhomepet.ui.pet.presenter.PetPresenter
import com.vadym.adv.myhomepet.ui.pet.view.EditPetView
import com.vadym.adv.myhomepet.ui.pet.view.PetView
import dagger.Component

@Component(modules = [AppModule::class, NetModule::class, NetModule::class])
interface ApplicationComponent {

    fun inject(modApplication: AndroidApplication)

    // VIEW
    fun inject(infoView: InfoView)
    fun inject(mainView: MainActivity)
    fun inject(petView: PetView)
    fun inject(editPetView: EditPetView)
    fun inject(signupActivity: SignupActivity)
    fun inject(loginActivity: LoginActivity)

    // PRESENTER
    fun inject(infoPresenter: InfoPresenter)
    fun inject(mainPresenter: MainPresenter)
    fun inject(petPresenter: PetPresenter)
    fun inject(editPetPresenter: EditPetPresenter)
    fun inject(signupPresenter: SignupPresenter)
    fun inject(loginPresenter: LoginPresenter)

}