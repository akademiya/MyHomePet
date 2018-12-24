package com.vadym.adv.myhomepet.ui.pet

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.vadym.adv.myhomepet.FirestoreUtils
import com.vadym.adv.myhomepet.R
import com.vadym.adv.myhomepet.toAndroidVisibility
import com.vadym.adv.myhomepet.ui.settings.SettingsView.Companion.CITY_KEY
import kotlinx.android.synthetic.main.item_my_pet_card_list.view.*

//private val onClickEditItem: (PetModel, Int) -> Unit

class PetAdapter(private val context: Context,
                 options: FirestoreRecyclerOptions<PetModel>) : FirestoreRecyclerAdapter<PetModel, PetAdapter.VH>(options) {

    var isPeriodSelection = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = VH (
            LayoutInflater.from(parent.context).inflate(R.layout.item_my_pet_card_list, parent, false)
    )

    override fun onBindViewHolder(holder: VH, position: Int, model: PetModel) {
        holder.apply {
//            FirestoreUtils.currentPetDocRef.addSnapshotListener { documentSnapshot, _ ->
//                FirestoreUtils.getCurrentPet { pet ->
//                    if (documentSnapshot?.exists()!!) {
////                        isItemExist = true
//                        if (pet.petPhoto != null) {
//                            GlideApp.with(context)
//                                    .load(ImageUtils.pathToReference(pet.petPhoto ?: ""))
//                                    .error(context.resources.getDrawable(R.drawable.ic_pets))
//                                    .circleCrop()
//                                    .into(itemView.img_pet)
//                        }
//                    }
//                }
//            }
            var period: Int = 0

            try {
                period = Integer.parseInt(model.period)
            } catch (e: NumberFormatException) {
                print(e)
            }


            itemView.category_pet.text = model.category
            itemView.main_action.text = model.action
            itemView.iv_take_home.visibility = (!model.isPeriodSelection).toAndroidVisibility()
            itemView.period.visibility = model.isPeriodSelection.toAndroidVisibility()
            itemView.container_time_period.visibility = model.isPeriodSelection.toAndroidVisibility()
            itemView.period.text = context.resources.getQuantityString(R.plurals.days, period, period)
            itemView.period_from.text = model.periodFrom
            itemView.period_to.text = model.periodTo

            FirestoreUtils.currentUserDocRef.addSnapshotListener { documentSnapshot, _ ->
                if (documentSnapshot?.exists()!!) {
                    itemView.country.text = documentSnapshot.getString(CITY_KEY)
                }
            }

//            itemView.setOnClickListener { onClickEditItem(model, position) }
        }
    }

    fun removeItem(position: Int) {
        snapshots.getSnapshot(position).reference.delete()
    }

    class VH(view: View?) : RecyclerView.ViewHolder(view)
}