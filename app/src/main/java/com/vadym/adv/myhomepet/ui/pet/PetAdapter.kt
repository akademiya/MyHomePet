package com.vadym.adv.myhomepet.ui.pet

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.vadym.adv.myhomepet.R
import com.vadym.adv.myhomepet.R.string.delete_item
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
        holder.deleteItem?.setOnClickListener {
            database.deletePet(singlePet.id)
            (context as Activity).finish()
            context.startActivity(context.intent)
        }
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

    class VH(view: View?) : RecyclerView.ViewHolder(view) {
        @SuppressLint("ResourceType")
        val deleteItem = view?.findViewById<ImageView>(delete_item)
        fun bind(petModel: PetModel) {
            itemView.apply {
                listView.setOnLongClickListener {
                    listReview?.visibility = View.VISIBLE
                    false
                }

                listView.setOnClickListener {  }

//                delete_item.setOnClickListener {
//
//                }

                go_back.setOnClickListener {
                    listReview?.visibility = View.GONE
                }

//                img_pet.setImageDrawable(petModel.petPhoto)
                category_pet.text = petModel.category
                main_action.text = petModel.action
                period.text = petModel.period
                country.text = petModel.country
            }
        }
    }
}