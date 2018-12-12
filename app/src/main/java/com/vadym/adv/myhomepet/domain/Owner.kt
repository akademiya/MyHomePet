package com.vadym.adv.myhomepet.domain

data class Owner(
        var name: String,
        var pin: String,
        var email: String,
        var phone: String,
        var city: String,
        val photo: String?,
        var registrationTokens: MutableList<String>) {

    constructor() : this("", "", "", "", "", null, mutableListOf())

}