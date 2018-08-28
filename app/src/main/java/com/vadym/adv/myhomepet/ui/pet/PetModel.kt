package com.vadym.adv.myhomepet.ui.pet

class PetModel {
    var id: Int = 0
    var petPhoto: Int = 0
    var petName: String? = null
    var category: String? = null
    var action: String? = null
    var period: String? = null
    var country: String? = null
    var vaccine: Boolean = false

    constructor(pet_category: String?, pet_action: String?, pet_period: String?, pet_country: String?) {
//        this.petPhoto = pet_photo
        this.category = pet_category
        this.action = pet_action
        this.period = pet_period
        this.country = pet_country
    }

    //    pet_photo: Int,
    constructor(pet_id: Int, pet_name: String?, pet_category: String?, pet_action: String?, pet_period: String?, pet_country: String?) {
        this.id = pet_id
//        this.petPhoto = pet_photo
        this.petName = pet_name
        this.category = pet_category
        this.action = pet_action
        this.period = pet_period
        this.country = pet_country
    }
}