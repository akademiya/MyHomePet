package com.vadym.adv.myhomepet.ui.pet

import android.app.Activity
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
import com.vadym.adv.myhomepet.ui.settings.SettingsView.Companion.CITY_KEY
import kotlinx.android.synthetic.main.item_my_pet_card_list.view.*


class PetAdapter(private val context: Context,
                 options: FirestoreRecyclerOptions<PetModel>,
                 private val onClickEditItem: (PetModel) -> Unit) : FirestoreRecyclerAdapter<PetModel, PetAdapter.VH>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = VH (
            LayoutInflater.from(parent.context).inflate(R.layout.item_my_pet_card_list, parent, false)
    )

    override fun onBindViewHolder(holder: VH, position: Int, model: PetModel) {
        holder.apply {
            var period = 0
            try {
                period = Integer.parseInt(model.period)
            } catch (e: NumberFormatException) { print(e) }

            itemView.category_pet.text = model.category
            itemView.action.text = model.action
            itemView.iv_take_home.visibility = (!model.isPeriodSelection).toAndroidVisibility()
            itemView.period.visibility = model.isPeriodSelection.toAndroidVisibility()
            itemView.container_time_period.visibility = model.isPeriodSelection.toAndroidVisibility()
            itemView.period.text = context.resources.getQuantityString(R.plurals.days, period, period)
            itemView.period_from.text = model.periodFrom
            itemView.period_to.text = model.periodTo
            if (!model.petPhoto.isNullOrBlank()) {
                GlideApp.with(context)
                        .load(model.petPhoto?.let { ImageUtils.pathToReference(it) })
                        .circleCrop()
                        .into(itemView.img_pet)
            }

            FirestoreUtils.currentUserDocRef.addSnapshotListener { documentSnapshot, _ ->
                if (documentSnapshot?.exists()!!) {
                    itemView.country.text = documentSnapshot.getString(CITY_KEY)
                }
            }

            itemView.setOnClickListener { onClickEditItem(model) }
        }
    }

    fun removeItem(position: Int) {
        snapshots.getSnapshot(position).reference.delete()
        (context as Activity).finish()
        context.startActivity(context.intent)
    }

    class VH(view: View?) : RecyclerView.ViewHolder(view)
}