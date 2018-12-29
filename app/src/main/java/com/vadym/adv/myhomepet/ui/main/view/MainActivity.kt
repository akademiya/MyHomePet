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

        val sortByTime = petCollection.orderBy("action", Query.Direction.DESCENDING)
        val options = FirestoreRecyclerOptions.Builder<PetModel>()
                .setQuery(sortByTime, PetModel::class.java)
                .build()

        adapter = MainAdapter(this, options) { model, position ->
            val intent = Intent(this, CardInfoView::class.java)
            intent.putExtra("model", model.pid)
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