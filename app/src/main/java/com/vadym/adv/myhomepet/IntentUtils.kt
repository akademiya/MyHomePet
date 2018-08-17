package com.vadym.adv.myhomepet

import android.Manifest
import android.content.Context
import android.content.Intent
import android.provider.MediaStore
import android.widget.Toast
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener

enum class IntentRequests(val code: Int) {
    REQUEST_TAKE_GALLERY_IMAGE(1000),
    REQUEST_CAPTURE_CAMERA_IMAGE(2000)
}

fun Context.launchIntent(intent: Intent, code: Int): Unit {
    getActivity()?.startActivityForResult(intent, code)
}

fun Context.startTakeGalleryImageIntent() =
        Dexter.withActivity(getActivity())
                .withPermission(Manifest.permission.CAMERA)
                .withListener(object : PermissionListener {
                    override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                        launchIntent(Intent.createChooser(
                                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI),
                                getString(R.string.choose_photo)
                        ), IntentRequests.REQUEST_TAKE_GALLERY_IMAGE.code)
                    }

                    override fun onPermissionDenied(response: PermissionDeniedResponse?) {
                        Toast.makeText(this@startTakeGalleryImageIntent, "no image permission", Toast.LENGTH_LONG).show()
                    }

                    override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest?, token: PermissionToken?) {
                        token?.continuePermissionRequest()
                    }
                }).check()

fun Context.startCaptureCameraImageIntent() =
        Dexter.withActivity(getActivity())
                .withPermission(Manifest.permission.CAMERA)
                .withListener(object : PermissionListener {
                    override fun onPermissionGranted(response: PermissionGrantedResponse) =
                            launchIntent(Intent(
                                    MediaStore.ACTION_IMAGE_CAPTURE),
                                    IntentRequests.REQUEST_CAPTURE_CAMERA_IMAGE.code
                            )
                    override fun onPermissionDenied(response: PermissionDeniedResponse) =
                            Toast.makeText(this@startCaptureCameraImageIntent, "no camera permission", Toast.LENGTH_LONG).show()
                    override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest, token: PermissionToken) = token.continuePermissionRequest()
                }).check()