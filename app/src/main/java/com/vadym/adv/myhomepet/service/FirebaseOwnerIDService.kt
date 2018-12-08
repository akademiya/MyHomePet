package com.vadym.adv.myhomepet.service

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService
import com.vadym.adv.myhomepet.FirestoreUtils

class FirebaseOwnerIDService : FirebaseInstanceIdService() {
    override fun onTokenRefresh() {
        val newRegistrationToken = FirebaseInstanceId.getInstance().token

        if (FirebaseAuth.getInstance().currentUser != null) {
            addTokenToFirestore(newRegistrationToken)
        }
    }

    companion object {
        fun addTokenToFirestore(newRegistrationToken: String?) {
            if (newRegistrationToken == null) throw NullPointerException("FCM token is null")
            FirestoreUtils.getFCMRegistrationToken { tokens ->
                if (tokens.contains(newRegistrationToken)) {
                    return@getFCMRegistrationToken
                } else {
                    tokens.add(newRegistrationToken)
                    FirestoreUtils.setFCMRegistrationToken(tokens)
                }
            }
        }
    }
}