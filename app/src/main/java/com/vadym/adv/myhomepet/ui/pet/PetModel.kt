package com.vadym.adv.myhomepet.ui.pet

import com.vadym.adv.myhomepet.R.string.*

class PetModel {
    var petPhoto: String? = null
    var petName: String? = null
    var petAge: Int = 0
    var breed: String? = null
    var category: String? = null
    var action: String? = null
    var isPeriodSelection: Boolean = false
    var period: String? = null
    var periodFrom: String? = null
    var periodTo: String? = null
    var vaccine: Boolean = false
    var description: String? = null
    var inventory: String? = null

    constructor(category: String?, action: String?, isPeriodSelection: Boolean, period: String?, periodFrom: String?, periodTo: String?, petName: String?, breed: String?, petAge: Int, vaccine: Boolean, description: String?, inventory: String?, pet_photo: String) {
        this.category = category
        this.action = action
        this.isPeriodSelection = isPeriodSelection
        this.period = period
        this.periodFrom = periodFrom
        this.periodTo = periodTo
        this.petName = petName
        this.breed = breed
        this.petAge = petAge
        this.vaccine = vaccine
        this.description = description
        this.inventory = inventory
        this.petPhoto = pet_photo
    }

    companion object {
        val PET_CATEGORY_KEY = "category"
        val PET_ACTION_KEY = "action"
        val PET_PERIOD_KEY = "period"
        val PET_PERIOD_FROM_KEY = "periodFrom"
        val PET_PERIOD_TO_KEY = "periodTo"
        val PET_NAME_KEY = "name"
        val PET_BREED_KEY = "breed"
        val PET_AGE_KEY = "age"
        val PET_VACCINE_KEY = "vaccine"
        val PET_DESCRIPTION_KEY = "description"
        val PET_INVENTORY_KEY = "inventory"
    }

    constructor() {}

}