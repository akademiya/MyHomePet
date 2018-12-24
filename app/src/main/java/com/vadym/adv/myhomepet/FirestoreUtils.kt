package com.vadym.adv.myhomepet

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.vadym.adv.myhomepet.domain.Owner
import com.vadym.adv.myhomepet.ui.pet.PetModel
import com.vadym.adv.myhomepet.ui.settings.SettingsView.Companion.CITY_KEY
import com.vadym.adv.myhomepet.ui.settings.SettingsView.Companion.NAME_KEY
import com.vadym.adv.myhomepet.ui.settings.SettingsView.Companion.PHONE_KEY
import com.vadym.adv.myhomepet.ui.settings.SettingsView.Companion.PIN_KEY

object FirestoreUtils {
    private val firestoreInstance: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }
    val currentUserDocRef: DocumentReference
        get() = firestoreInstance.document("Owners/${FirebaseAuth.getInstance().uid
                ?: throw NullPointerException("UID Owner is null")}")

    val path = currentUserDocRef.collection("PetCollection").document().path
    val currentPetDocRef: DocumentReference
        get() = firestoreInstance.document(path)
//        get() = currentUserDocRef
//                .collection("PetCollection")
//                .document()


    fun initCurrentUserIfFirstTime(onComplete: () -> Unit) {
        currentUserDocRef.get().addOnSuccessListener { documentSnapshot ->
            if (!documentSnapshot.exists()) {
                val newOwner = Owner(FirebaseAuth.getInstance().currentUser?.displayName
                        ?: "", "", "", "", "", null, mutableListOf())
                currentUserDocRef.set(newOwner).addOnSuccessListener { onComplete }
            } else onComplete()
        }
    }

    fun getCurrentUser(onComplate: (Owner) -> Unit) {
        currentUserDocRef.get().addOnSuccessListener {
            onComplate(it.toObject(Owner::class.java)!!)
        }
    }

    fun getCurrentPet(onComplate: (PetModel) -> Unit) {
        currentPetDocRef.get().addOnSuccessListener {
            onComplate(it.toObject(PetModel::class.java)!!)
        }
    }

    fun updateCurrentUser(name: String = "",
                          pin: String = "",
                          phone: String = "",
                          city: String = "",
                          photo: String? = null) {
        val userFieldMap = mutableMapOf<String, Any>()
        if (name.isNotBlank()) userFieldMap[NAME_KEY] = name
        if (pin.isNotBlank()) userFieldMap[PIN_KEY] = pin
        if (phone.isNotBlank()) userFieldMap[PHONE_KEY] = phone
        if (city.isNotBlank()) userFieldMap[CITY_KEY] = city
        if (photo != null) userFieldMap["photo"] = photo
        currentUserDocRef.update(userFieldMap)
    }


    /** for notification */
    fun getFCMRegistrationToken(onComplete: (tokens: MutableList<String>) -> Unit) {
        currentUserDocRef.get().addOnSuccessListener {
            val owner = it.toObject(Owner::class.java)!!
            onComplete(owner.registrationTokens)
        }
    }

    fun setFCMRegistrationToken(registrationTokens: MutableList<String>) {
        currentUserDocRef.update(mapOf("registrationTokens" to registrationTokens))
    }
}