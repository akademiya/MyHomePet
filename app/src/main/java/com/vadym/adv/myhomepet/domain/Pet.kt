package com.vadym.adv.myhomepet.domain

data class Pet (
        var category: String,
        var action: String,
        var period: String,
        var periodFrom: String,
        var periodTo: String,
        var petName: String,
        var breed: String,
        var petAge: Int,
        var vaccine: Boolean,
        var description: String,
        var inventory: String,
        var petPhoto: String?) {

    constructor() : this(
            category = "",
            action = "",
            period = "",
            periodFrom = "",
            periodTo = "",
            petName = "",
            breed = "",
            petAge = 0,
            vaccine = false,
            description = "",
            inventory = "",
            petPhoto = null
    )
}