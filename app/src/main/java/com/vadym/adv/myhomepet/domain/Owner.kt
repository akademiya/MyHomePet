package com.vadym.adv.myhomepet.domain

data class Owner(
        var name: String,
        var pin: String,
        var email: String,
        var phone: String,
        var city: String,
        var photo: String?,
        var registrationTokens: MutableList<String>) {

    constructor() : this("", "", "", "", "", null, mutableListOf())

//    constructor(name: String, pin: String, email: String, phone: String, city: String, mutableListOf()) {
//        this.name = name
//        this.pin = pin
//        this.email = email
//        this.phone = phone
//        this.city = city
//    }
}