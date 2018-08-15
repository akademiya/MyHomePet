package com.vadym.adv.myhomepet.ui.pet.view

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.LinearLayout
import com.vadym.adv.myhomepet.BaseActivity
import com.vadym.adv.myhomepet.R
import com.vadym.adv.myhomepet.data.SqliteDatabase
import com.vadym.adv.myhomepet.toAndroidVisibility
import com.vadym.adv.myhomepet.ui.pet.PetAdapter
import com.vadym.adv.myhomepet.ui.pet.PetModel
import com.vadym.adv.myhomepet.ui.pet.presenter.PetPresenter
import kotlinx.android.synthetic.main.view_my_pet_card_list.*

class PetView : BaseActivity(), IPetView {

    private lateinit var presenter: PetPresenter
    private lateinit var database: SqliteDatabase
    private lateinit var allpets: List<PetModel>
    private lateinit var adapter: PetAdapter

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

        list_my_pets.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        list_my_pets.setHasFixedSize(true)

        database = SqliteDatabase.getInstance(this)
        allpets = database.listPets()

        if (allpets.isNotEmpty()) {
            adapter = PetAdapter(allpets, this, database)
            list_my_pets.adapter = adapter
        }
        presenter.onEmptyListVisibility(allpets.isNotEmpty())
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
}