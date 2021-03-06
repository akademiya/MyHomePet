package com.vadym.adv.myhomepet

import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.design.widget.Snackbar
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import com.vadym.adv.myhomepet.network.InternetConnectionReceiver
import com.vadym.adv.myhomepet.ui.info.InfoView
import com.vadym.adv.myhomepet.ui.login.view.LoginActivity
import com.vadym.adv.myhomepet.ui.main.view.MainActivity
import com.vadym.adv.myhomepet.ui.pet.view.EditPetView
import com.vadym.adv.myhomepet.ui.pet.view.PetView
import com.vadym.adv.myhomepet.ui.settings.SettingsView
import com.vadym.adv.myhomepet.ui.settings.SettingsView.Companion.CITY_KEY
import com.vadym.adv.myhomepet.ui.settings.SettingsView.Companion.NAME_KEY
import kotlinx.android.synthetic.main.nav_header_main.view.*

abstract class BaseActivity : AppCompatActivity(), IView, NavigationView.OnNavigationItemSelectedListener, InternetConnectionReceiver.NetworkReceiverListener {

    internal lateinit var toolbar: Toolbar
    private lateinit var drawer: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var toggle: ActionBarDrawerToggle

    override fun setContentView(layoutResID: Int) {
        val fullView = layoutInflater.inflate(R.layout.activity_nav_drawer, null) as DrawerLayout
        val activityContainer = fullView.findViewById<View>(R.id.content_base) as FrameLayout

        layoutInflater.inflate(layoutResID, activityContainer, true)
        super.setContentView(fullView)

        toolbar = findViewById<View>(R.id.toolbar) as Toolbar

        drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        toggle = ActionBarDrawerToggle(this, drawer, toolbar, R.string.drawer_open, R.string.drawer_close)
        drawer.addDrawerListener(toggle)
        toggle.drawerArrowDrawable.color = resources.getColor(R.color.white)
        toggle.syncState()

        navigationView = findViewById<View>(R.id.nav_view) as NavigationView
        navigationView.setNavigationItemSelectedListener(this)

        FirestoreUtils.currentUserDocRef.addSnapshotListener { documentSnapshot, _ ->
            if (documentSnapshot?.exists()!!) {
                navigationView.getHeaderView(0).owner_name.text = documentSnapshot.getString(NAME_KEY)
                navigationView.getHeaderView(0).owner_country.text = documentSnapshot.getString(CITY_KEY)
            }
        }
    }

    override fun onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    /** Right menu */
//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        menuInflater.inflate(R.menu.activity_main_drawer, menu)
//        return true
//    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        val id = item.itemId
//        return if (id == R.id.action_settings) {
//            true
//        } else super.onOptionsItemSelected(item)
//        return true
//    }
    /** End right menu */

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        val id = menuItem.itemId
        when (id) {
            R.id.nav_main -> startActivity(Intent(this, MainActivity::class.java))
            R.id.nav_my_pet -> startActivity(Intent(this, PetView::class.java))
            R.id.nav_settings -> startActivity(Intent(this, SettingsView::class.java))
            R.id.nav_facebook -> {
                val isAppInstalled: Boolean = doesAppInstalled("com.facebook.katana")
                if (isAppInstalled) {
                    startActivity(packageManager.getLaunchIntentForPackage("com.facebook.katana"))
                } else {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/LovelyHomeAnimals")))
                }
            }
            R.id.nav_share -> {
                val sharingIntent = Intent(Intent.ACTION_SEND)
                val shareBody = getString(R.string.share_body)
                sharingIntent.apply {
                    type = "text/plain"
                    putExtra(android.content.Intent.EXTRA_SUBJECT, "Lovely Home Animals")
                    putExtra(android.content.Intent.EXTRA_TEXT, shareBody)
                }
                startActivity(Intent.createChooser(sharingIntent, getString(R.string.share_by)))
            }

            R.id.nav_send -> {
                val uri = Uri.parse("mailto:vadym.adv@gmail.com")
                val sendIntent = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"))
                sendIntent.data = uri
                startActivity(Intent.createChooser(sendIntent, "Lovely Home Animals"))
            }

            R.id.nav_info -> startActivity(Intent(this, InfoView::class.java))
        }

        drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    private fun doesAppInstalled(uri: String) : Boolean {
        val pm: PackageManager = packageManager
        return try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }

    fun setNavigationItemClicked(position: Int) {
        navigationView.menu.getItem(position).isChecked = true
    }

    private var mProgressDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initProgressDialog()
        init(savedInstanceState)
        registerReceiver(InternetConnectionReceiver(), IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }

    private fun showMessageNoInternet(isConnected: Boolean) { //TODO show message no internet on any activity
        var mSnackBar: Snackbar? = null
        if (!isConnected) {
            mSnackBar = Snackbar.make(findViewById(R.id.content_base), R.string.no_internet, Snackbar.LENGTH_LONG)
            mSnackBar.duration = Snackbar.LENGTH_INDEFINITE
            mSnackBar.show()
        } else
            mSnackBar?.dismiss()
    }

    private fun initProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = ProgressDialog(this)
            mProgressDialog!!.isIndeterminate = true
            mProgressDialog!!.setCancelable(false)
        }
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        showMessageNoInternet(isConnected)
    }

    override fun onDestroy() {
        super.onDestroy()
        System.gc()
        System.runFinalization()
        dismissProgress()
        mProgressDialog = null
    }

    abstract fun init(savedInstanceState: Bundle?)

    fun onCreateCardPet() {
        startActivity(Intent(this, EditPetView::class.java))
    }

    fun goTo(flowActivity: FlowActivity) {
        proceedToActivity(flowActivity)
    }

    fun signOut() {
        startActivity(Intent(this, LoginActivity::class.java))
    }

    private fun proceedToActivity(flowActivity: FlowActivity) {
        val myActivity: FlowActivity? = null
        if (flowActivity != myActivity) {
            when(flowActivity) {
                FlowActivity.HOME -> startActivity(Intent(this, MainActivity::class.java))
                FlowActivity.MY_PET -> startActivity(Intent(this, PetView::class.java))
                FlowActivity.SETTINGS -> TODO()
                FlowActivity.INFO -> startActivity(Intent(this, InfoView::class.java))
            }
        }
    }

    private fun showProgress(msgResId: Int, keyListener: DialogInterface.OnKeyListener?) {
        if (isFinishing) return

        if (mProgressDialog!!.isShowing) return

        if (msgResId != 0) {
            mProgressDialog?.setMessage(resources.getString(msgResId))
        }

        if (keyListener != null) {
            mProgressDialog?.setOnKeyListener(keyListener)
        } else {
            mProgressDialog?.setCancelable(false)
        }
        mProgressDialog?.show()
    }

    /**
     * @param isCancel
     */
    fun setCancelableProgress(isCancel: Boolean) {
        if (mProgressDialog != null) {
            mProgressDialog?.setCancelable(true)
        }
    }

    /**
     * cancel progress dialog.
     */
    private fun dismissProgress() {
        if (mProgressDialog != null && mProgressDialog!!.isShowing) {
            mProgressDialog?.dismiss()
        }
    }


    override fun showLoading() {
        showProgress(R.string.loading, null)
    }

    override fun hideLoading() {
        dismissProgress()
    }

    override fun loadError(e: Throwable) {
        showHttpError(e)
    }

    override fun loadError(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    private fun showHttpError(e: Throwable) {
        loadError(e.localizedMessage)
    }

//    override fun onStop() {
//        super.onStop()
//        stopScreen()
//    }
}