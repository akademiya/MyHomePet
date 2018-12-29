package com.vadym.adv.myhomepet.ui.main

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.vadym.adv.myhomepet.FirestoreUtils
import com.vadym.adv.myhomepet.ImageUtils
import com.vadym.adv.myhomepet.R
import com.vadym.adv.myhomepet.di.module.GlideApp
import com.vadym.adv.myhomepet.toAndroidVisibility
import com.vadym.adv.myhomepet.ui.pet.PetModel
import com.vadym.adv.myhomepet.ui.settings.SettingsView
import kotlinx.android.synthetic.main.item_main_card_list.view.*


class MainAdapter(private val context: Context,
                  options: FirestoreRecyclerOptions<PetModel>,
                  private val onClickItemMoreInformation: (PetModel, Int) -> Unit) : FirestoreRecyclerAdapter<PetModel, MainAdapter.VH>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = VH(
            LayoutInflater.from(parent.context).inflate(R.layout.item_main_card_list, parent, false)
    )

    override fun onBindViewHolder(holder: VH, position: Int, model: PetModel) {
        holder.apply {
            var period = 0
            try {
                period = Integer.parseInt(model.period)
            } catch (e: NumberFormatException) { print(e) }

            itemView.main_category_pet.text = model.category
            itemView.main_action.text = model.action
            itemView.main_iv_take_home.visibility = (!model.isPeriodSelection).toAndroidVisibility()
            itemView.main_period.visibility = model.isPeriodSelection.toAndroidVisibility()
            itemView.main_container_time_period.visibility = model.isPeriodSelection.toAndroidVisibility()
            itemView.main_period.text = context.resources.getQuantityString(R.plurals.days, period, period)
            itemView.main_period_from.text = model.periodFrom
            itemView.main_period_to.text = model.periodTo
            if (!model.petPhoto.isNullOrBlank()) {
                GlideApp.with(context)
                        .load(ImageUtils.pathToReference(model.petPhoto.toString()))
                        .circleCrop()
                        .into(itemView.main_img_pet)
            }
            itemView.button_more.setOnClickListener { onClickItemMoreInformation(model, position) }

            FirestoreUtils.currentUserDocRef.addSnapshotListener { documentSnapshot, _ ->
                FirestoreUtils.getCurrentUser { owner ->
                    if (documentSnapshot?.exists()!!) {
                        itemView.owner_country.text = documentSnapshot.getString(SettingsView.CITY_KEY)
                        itemView.owner_name.text = documentSnapshot.getString(SettingsView.NAME_KEY)
                        itemView.owner_email.text = documentSnapshot.getString(SettingsView.EMAIL_KEY)
                        if (owner.photo != null) {
                            GlideApp.with(context)
                                    .load(ImageUtils.pathToReference(owner.photo))
                                    .circleCrop()
                                    .into(itemView.img_owner)
                        }
                    }
                }
            }
        }
    }

    class VH(view: View?) : RecyclerView.ViewHolder(view)
}