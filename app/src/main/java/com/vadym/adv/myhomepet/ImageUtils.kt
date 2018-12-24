package com.vadym.adv.myhomepet

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.lang.NullPointerException
import java.util.*

object ImageUtils {
    private val storageInstance: FirebaseStorage by lazy { FirebaseStorage.getInstance() }

    private val currentUserRef: StorageReference
        get() = storageInstance.reference
                .child(FirebaseAuth.getInstance().currentUser?.uid
                        ?: throw NullPointerException("UID is null"))

    fun uploadOwnerPhoto(imageBytes: ByteArray, onSuccess: (imagePath: String) -> Unit) {
        val reference = currentUserRef.child("ownerPhoto/${UUID.nameUUIDFromBytes(imageBytes)}")
        reference
                .putBytes(imageBytes)
                .addOnSuccessListener { onSuccess(reference.path) }
    }

    fun uploadPetPhoto(imageBytes: ByteArray, onSuccess: (imagePath: String) -> Unit) {
        val reference = currentUserRef.child("petPhoto/${UUID.nameUUIDFromBytes(imageBytes)}")
        reference
                .putBytes(imageBytes)
                .addOnSuccessListener { onSuccess(reference.path) }
    }

    fun pathToReference(path: String) = storageInstance.getReference(path)
}