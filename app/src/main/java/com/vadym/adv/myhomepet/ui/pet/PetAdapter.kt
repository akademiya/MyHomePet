package com.vadym.adv.myhomepet.ui.pet

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.vadym.adv.myhomepet.R
import com.vadym.adv.myhomepet.R.id.img_pet
import com.vadym.adv.myhomepet.R.plurals.days
import com.vadym.adv.myhomepet.domain.Owner
import kotlinx.android.synthetic.main.item_my_pet_card_list.view.*

class PetAdapter(private val context: Context,
                 private val owner: Owner,
                 options: FirestoreRecyclerOptions<PetModel>) : FirestoreRecyclerAdapter<PetModel, PetAdapter.VH>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = VH (
            LayoutInflater.from(parent.context).inflate(R.layout.item_my_pet_card_list, parent, false)
    )

    override fun onBindViewHolder(holder: VH, position: Int, model: PetModel) {
        holder.apply {
//            itemView.img_pet.setImageDrawable(model.petPhoto)
            itemView.category_pet.text = model.category
            itemView.main_action.text = model.action
            itemView.period.text = days.let { context.resources.getQuantityString(R.plurals.days, it, it) }
            itemView.country.text = owner.city
        }
    }

    class VH(view: View?) : RecyclerView.ViewHolder(view)
}









//    override fun getItemCount(): Int { return pets.size }

//    fun removeItem(position: Int) {
//        database.deletePet(pets[position].id)
//        pets.removeAt(position)
//        notifyItemRemoved(position)
//        (context as Activity).finish()
//        context.startActivity(context.intent)
//    }

//    private fun editTaskDialog(person: Person) {
//        val inflater = LayoutInflater.from(context)
//        val subView = inflater.inflate(R.layout.item_edit_list_person, null)
//
//        val nameField = subView.findViewById<EditText>(R.id.create_person_name)
//        val descriptionField = subView.findViewById<EditText>(R.id.create_person_description)
//        nameField.setText(person.personName)
//        descriptionField.setText(person.personDescription)
//
//        val builder = AlertDialog.Builder(context)
//        builder.setTitle(R.string.edit_person)
//        builder.setView(subView)
//        builder.create()
//        builder.setPositiveButton(R.string.edit_person) { _, _ ->
//            val name = nameField.text.toString()
//            val description = descriptionField.text.toString()
//            database.updatePerson(Person(person.personId, name, description))
//
//            (context as Activity).finish()
//            context.startActivity(context.intent)
//        }
//
//        builder.setNegativeButton(R.string.cancel) { _, _ -> Toast.makeText(context, R.string.task_cancelled, Toast.LENGTH_SHORT).show() }
//        builder.show()
//    }

