package com.vadym.adv.myhomepet.ui.pet.view

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.View
import android.widget.LinearLayout
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Query
import com.vadym.adv.myhomepet.BaseActivity
import com.vadym.adv.myhomepet.FirestoreUtils
import com.vadym.adv.myhomepet.R
import com.vadym.adv.myhomepet.toAndroidVisibility
import com.vadym.adv.myhomepet.ui.pet.PetAdapter
import com.vadym.adv.myhomepet.ui.pet.PetModel
import com.vadym.adv.myhomepet.ui.pet.SwipeToDeleteCallback
import com.vadym.adv.myhomepet.ui.pet.presenter.PetPresenter
import kotlinx.android.synthetic.main.view_my_pet_card_list.*


class PetView : BaseActivity(), IPetView {

    private lateinit var presenter: PetPresenter
    private lateinit var adapter: PetAdapter
    private val petCollection = FirestoreUtils.allPetDocRef

    override fun init(savedInstanceState: Bundle?) {
        super.setContentView(R.layout.view_my_pet_card_list)
        presenter = PetPresenter(this, application)

        /** Show or hide FAB */
        list_my_pets.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0 && fab.visibility == View.VISIBLE) {
                    fab.hide()
                } else if (dy < 0 && fab.visibility != View.VISIBLE) {
                    fab.show()
                }
            }
        })

        fab.setOnClickListener { presenter.goToAddNewPosition() }

        val sortByTime = petCollection
                .orderBy("currentDate", Query.Direction.DESCENDING)
                .whereEqualTo("pid",
                        if ("${FirebaseAuth.getInstance().uid}" == FirebaseAuth.getInstance().currentUser?.uid) {
                            FirebaseAuth.getInstance().uid
                        } else null
                )
        val options = FirestoreRecyclerOptions.Builder<PetModel>()
                .setQuery(sortByTime, PetModel::class.java)
                .build()

        list_my_pets.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        list_my_pets.setHasFixedSize(true)
        adapter = PetAdapter(this, options) { model ->
            val intent = Intent(this, EditPetView::class.java)
            intent.putExtra("model", model.pid)
            intent.putExtra("petCategory", model.category)
            intent.putExtra("action", model.action)
            intent.putExtra("isSelectedPeriod", model.isPeriodSelection)
            intent.putExtra("period", model.period)
            intent.putExtra("periodFrom", model.periodFrom)
            intent.putExtra("periodTo", model.periodTo)
            intent.putExtra("petName", model.petName)
            intent.putExtra("breed", model.breed)
            intent.putExtra("age", model.petAge.toString())
            intent.putExtra("vaccine", model.vaccine.toString())
            intent.putExtra("description", model.description)
            intent.putExtra("inventory", model.inventory)
            intent.putExtra("photo", model.petPhoto)
            startActivity(intent)
        }
        list_my_pets.adapter = adapter

        /** Empty list */
        petCollection.whereEqualTo("pid", FirebaseAuth.getInstance().uid).get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                presenter.onPetListVisibility(task.result!!.size() > 0)
            }
        }


        val swipeHandler = object : SwipeToDeleteCallback(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                adapter.removeItem(viewHolder.adapterPosition)
            }
        }

        ItemTouchHelper(swipeHandler).attachToRecyclerView(list_my_pets)

    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        presenter.bindView(this)
    }

    override fun onDetachedFromWindow() {
        presenter.unbindView(this)
        super.onDetachedFromWindow()
    }

    override fun showListPets(isVisible: Boolean) {
        list_my_pets.visibility = isVisible.toAndroidVisibility()
    }

    override fun showEmptyList(isVisible: Boolean) {
        list_empty.visibility = isVisible.toAndroidVisibility()
    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }
}