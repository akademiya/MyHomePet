package com.vadym.adv.myhomepet

import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.Toast
import com.vadym.adv.myhomepet.ui.info.InfoView

class MainActivity : AppCompatActivity() {

    private var drawerLayout: DrawerLayout? = null
    private var toolbar: Toolbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        initNavigationDrawer()
    }

    fun initNavigationDrawer() {

        // refer the navigation view of the xml
        val navigationView: NavigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener { menuItem ->
            val id = menuItem.itemId
            when (id) {
                R.id.nav_my_pet -> {
                    Toast.makeText(applicationContext, "You Clicked Options A", Toast.LENGTH_SHORT).show()
                    drawerLayout!!.closeDrawers()
                }
                R.id.nav_settings -> {
                    Toast.makeText(applicationContext, "You Clicked Options B", Toast.LENGTH_SHORT).show()
                    drawerLayout!!.closeDrawers()
                }
                R.id.nav_share -> {
                    val sharingIntent = Intent(Intent.ACTION_SEND)
                    val shareBody = getString(R.string.share_body)
                    sharingIntent.apply {
                        type = "text/plain"
                        putExtra(android.content.Intent.EXTRA_SUBJECT, "True Father Prayers")
                        putExtra(android.content.Intent.EXTRA_TEXT, shareBody)
                    }
                    startActivity(Intent.createChooser(sharingIntent, getString(R.string.share_by)))
                }

                R.id.nav_send -> {
                    val uri = Uri.parse("mailto:vadym.adv@gmail.com")
                    val sendIntent = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"))
                    sendIntent.setData(uri)
                    startActivity(Intent.createChooser(sendIntent, "True Father Prayers"))
                }

                R.id.nav_info -> startActivity(Intent(this, InfoView::class.java))
            }
            true
        }
        val header = navigationView.getHeaderView(0)
        drawerLayout = findViewById(R.id.drawer_layout)

        val actionBarDrawerToggle = object : ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            override fun onDrawerClosed(v: View) {
                super.onDrawerClosed(v)
            }

            override fun onDrawerOpened(v: View) {
                super.onDrawerOpened(v)
            }
        }
        drawerLayout!!.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
    }
}
