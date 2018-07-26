package com.vadym.adv.myhomepet

interface IView {
    fun showLoading()
    fun hideLoading()
    fun loadError(e: Throwable)
    fun loadError(msg: String)
}