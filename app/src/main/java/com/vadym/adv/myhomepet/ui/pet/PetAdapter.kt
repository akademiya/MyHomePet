package com.vadym.adv.myhomepet.ui.pet

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.vadym.adv.myhomepet.R
import com.vadym.adv.myhomepet.data.SqliteDatabase
import kotlinx.android.synthetic.main.item_my_pet_card_list.view.*

class PetAdapter(private val pets: List<PetModel>, private val context: Context, private val database: SqliteDatabase) : RecyclerView.Adapter<PetAdapter.VH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = VH (
            LayoutInflater.from(parent.context).inflate(R.layout.item_my_pet_card_list, parent, false)
    )

    override fun getItemCount(): Int { return pets.size }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val singlePet = pets[position]
        holder.bind(pets[position])
    }

    class VH(view: View?) : RecyclerView.ViewHolder(view) {
        fun bind(petModel: PetModel) {
            itemView.apply {
//                img_pet.setImageDrawable(petModel.petPhoto)
                category_pet.text = petModel.category
                main_action.text = petModel.action
                period.text = petModel.period
                country.text = petModel.country
            }
        }
    }
}