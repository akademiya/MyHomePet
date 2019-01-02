package com.vadym.adv.myhomepet.ui.pet

class PetModel {
    var currentDate: String? = null
    var pid: String? = null
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

    constructor(currentDate: String, pid: String?, category: String?, action: String?, isPeriodSelection: Boolean, period: String?, periodFrom: String?, periodTo: String?, petName: String?, breed: String?, petAge: Int, vaccine: Boolean, description: String?, inventory: String?, pet_photo: String) {
        this.currentDate = currentDate
        this.pid = pid
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

    constructor() {}

}