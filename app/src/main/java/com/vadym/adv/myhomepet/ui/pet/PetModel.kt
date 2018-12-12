package com.vadym.adv.myhomepet.ui.pet

class PetModel {
    var id: Int = 0
    var petPhoto: String? = null
    var petName: String? = null
    var petAge: Int = 0
    var breed: String? = null
    var category: String? = null
    var action: String? = null
    var period: String? = null
    var periodFrom: String? = null
    var periodTo: String? = null
    var country: String? = null
    var vaccine: Boolean = false

    constructor(pet_photo: String) {
        this.petPhoto = pet_photo
    }

    constructor(category: String?, action: String?, period: String?, periodFrom: String?, periodTo: String?, petName: String?, breed: String?, petAge: Int, vaccine: Boolean) {
        this.category = category
        this.action = action
        this.period = period
        this.periodFrom = periodFrom
        this.periodTo = periodTo
        this.petName = petName
        this.breed = breed
        this.petAge = petAge
        this.vaccine = vaccine
    }

    constructor(pet_id: Int, pet_name: String?, pet_category: String?, pet_action: String?, pet_period: String?, pet_country: String?) {
        this.id = pet_id
        this.petName = pet_name
        this.category = pet_category
        this.action = pet_action
        this.period = pet_period
        this.country = pet_country
    }
}