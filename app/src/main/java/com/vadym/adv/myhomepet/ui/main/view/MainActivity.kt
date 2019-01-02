package com.vadym.adv.myhomepet.ui.main.view

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.LinearLayout
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.Query
import com.vadym.adv.myhomepet.BaseActivity
import com.vadym.adv.myhomepet.FirestoreUtils
import com.vadym.adv.myhomepet.R
import com.vadym.adv.myhomepet.ui.main.MainAdapter
import com.vadym.adv.myhomepet.ui.main.presenter.MainPresenter
import com.vadym.adv.myhomepet.ui.pet.PetModel
import kotlinx.android.synthetic.main.view_main_card_list.*

class MainActivity : BaseActivity() {

    private lateinit var presenter: MainPresenter
    private lateinit var adapter: MainAdapter
    private val petCollection = FirestoreUtils.currentUserDocRef.collection("PetCollection")

    override fun init(savedInstanceState: Bundle?) {
        super.setContentView(R.layout.view_main_card_list)
        presenter = MainPresenter(this, application)

        val sortByTime = petCollection.orderBy("currentDate", Query.Direction.DESCENDING)
        val options = FirestoreRecyclerOptions.Builder<PetModel>()
                .setQuery(sortByTime, PetModel::class.java)
                .build()

        adapter = MainAdapter(this, options) { documentSnapshot, model, owner ->
            val petModel = documentSnapshot.toObject(PetModel::class.java)
            val docId = documentSnapshot.id
            val path = documentSnapshot.reference.path
            val intent = Intent(this, CardInfoView::class.java)
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
            intent.putExtra("ownerEmail", owner.email)
            startActivity(intent)
        }
        main_pet_list.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        main_pet_list.setHasFixedSize(true)
        main_pet_list.adapter = adapter
        list_empty.visibility = View.GONE

    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        presenter.bindView(this)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        presenter.unbindView(this)
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