package com.vadym.adv.myhomepet.ui.pet

import android.app.Activity
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.vadym.adv.myhomepet.R
import com.vadym.adv.myhomepet.data.SqliteDatabase
import kotlinx.android.synthetic.main.item_my_pet_card_list.view.*

class PetAdapter(private val pets: MutableList<PetModel>,
                 private val context: Context,
                 private val database: SqliteDatabase,
                 private val onEditItem: (PetModel) -> Unit) : RecyclerView.Adapter<PetAdapter.VH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = VH (
            LayoutInflater.from(parent.context).inflate(R.layout.item_my_pet_card_list, parent, false)
    )

    override fun getItemCount(): Int { return pets.size }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val singlePet = pets[position]
        val days = singlePet.period?.toInt()

        holder.apply {
//                img_pet.setImageDrawable(petModel.petPhoto)
            itemView.category_pet.text = singlePet.category
            itemView.main_action.text = singlePet.action
            itemView.period.text = days?.let { context.resources.getQuantityString(R.plurals.days, it, it) }
            itemView.country.text = singlePet.country

            itemView.setOnClickListener { onEditItem(singlePet) }

        }

//        holder.listView?.setOnClickListener {
//              database.updatePet(pets[id])
//        }
    }

    fun removeItem(position: Int) {
        database.deletePet(pets[position].id)
        pets.removeAt(position)
        notifyItemRemoved(position)
        (context as Activity).finish()
        context.startActivity(context.intent)
    }

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

    class VH(view: View?) : RecyclerView.ViewHolder(view)
}