package com.vadym.adv.myhomepet.ui.pet.view

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.View
import android.widget.LinearLayout
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.vadym.adv.myhomepet.BaseActivity
import com.vadym.adv.myhomepet.R
import com.vadym.adv.myhomepet.domain.Owner
import com.vadym.adv.myhomepet.toAndroidVisibility
import com.vadym.adv.myhomepet.ui.pet.PetAdapter
import com.vadym.adv.myhomepet.ui.pet.PetModel
import com.vadym.adv.myhomepet.ui.pet.SwipeToDeleteCallback
import com.vadym.adv.myhomepet.ui.pet.presenter.PetPresenter
import kotlinx.android.synthetic.main.view_my_pet_card_list.*

class PetView : BaseActivity(), IPetView {

    private lateinit var presenter: PetPresenter
    private val database = FirebaseFirestore.getInstance()
    private val petCollection = database.collection("PetCollection")
    private lateinit var allpets: MutableList<PetModel>
    private lateinit var adapter: PetAdapter
    private var owner = Owner()

    override fun init(savedInstanceState: Bundle?) {
        super.setContentView(R.layout.view_my_pet_card_list)
        presenter = PetPresenter(this, application)

        searchQuery.setOnQueryTextFocusChangeListener { _, hasFocus -> presenter.hideTextTitle(hasFocus) }

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

        val sortByTime = petCollection.orderBy("time", Query.Direction.DESCENDING)
        val options = FirestoreRecyclerOptions.Builder<PetModel>()
                .setQuery(sortByTime, PetModel::class.java)
                .build()

        adapter = PetAdapter(this, owner, options)
        list_my_pets.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        list_my_pets.setHasFixedSize(true)
        list_my_pets.adapter = adapter

//        if (petCollection.isNotEmpty) { FIXME
//            adapter = PetAdapter(this, owner, options)
//            list_my_pets.adapter = adapter
//        }
        presenter.onEmptyListVisibility(true) //allpets.isNotEmpty()

        val swipeHandler = object : SwipeToDeleteCallback(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder?, direction: Int) {
//                adapter.removeItem(viewHolder!!.adapterPosition) TODO delete by swipe
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(list_my_pets)

    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        presenter.bindView(this)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        presenter.unbindView(this)
    }

    override fun onEditCardPet(param: Int?) {
        val intent = Intent(this, EditPetView::class.java)
        startActivity(intent.putExtra(EditPetView.ID_CARD, param))
    }

    override fun setTitleVisibility(isVisible: Boolean) {
        title_list_pets.visibility = isVisible.toAndroidVisibility()
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