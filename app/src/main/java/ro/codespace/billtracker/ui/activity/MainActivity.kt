package ro.codespace.billtracker.ui.activity

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.view.MenuItem
import android.widget.TextView
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_main.*
import ro.codespace.billtracker.R
import ro.codespace.billtracker.ui.fragment.BillsFragment
import ro.codespace.billtracker.ui.fragment.CreateBillFragment
import ro.codespace.billtracker.ui.fragment.LoginFragment
import ro.codespace.billtracker.utils.replace

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {


    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp)
        }

        nav_drawer.inflateHeaderView(R.layout.nav_header)

        FirebaseAuth.getInstance().addAuthStateListener {
            runOnUiThread {
                if (it.currentUser == null) {
                    nav_drawer.getHeaderView(0).findViewById<TextView>(R.id.nav_header_text).text = getString(R.string.header_guest_title)
                    supportFragmentManager.replace(R.id.content_frame, LoginFragment())
                    nav_drawer.menu.findItem(R.id.nav_log_out).isVisible = false
                    nav_drawer.menu.findItem(R.id.nav_bills_list).isEnabled = false
                    nav_drawer.menu.findItem(R.id.nav_bills_add).isEnabled = false
                    nav_drawer.menu.findItem(R.id.nav_log_in).isVisible = true
                } else {
                    nav_drawer.getHeaderView(0).findViewById<TextView>(R.id.nav_header_text).text = it.currentUser!!.displayName!!
                    supportFragmentManager.replace(R.id.content_frame, BillsFragment())
                    nav_drawer.menu.findItem(R.id.nav_log_out).isVisible = true
                    nav_drawer.menu.findItem(R.id.nav_bills_list).isEnabled = true
                    nav_drawer.menu.findItem(R.id.nav_bills_add).isEnabled = true
                    nav_drawer.menu.findItem(R.id.nav_log_in).isVisible = false
                }
            }
        }

        nav_drawer.setNavigationItemSelectedListener(this)
    }


    override fun onOptionsItemSelected(item: MenuItem?) = when (item?.itemId) {
        android.R.id.home -> {
            drawer_layout.openDrawer(GravityCompat.START)
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_bills_list -> {
                item.isChecked = true
                supportFragmentManager.replace(R.id.content_frame, BillsFragment(), addToBackStack = true)
            }
            R.id.nav_bills_add -> {
                item.isChecked = true
                supportFragmentManager.replace(R.id.content_frame, CreateBillFragment(), addToBackStack = true)
            }
            R.id.nav_log_in -> {
                item.isChecked = true
                supportFragmentManager.replace(R.id.content_frame, LoginFragment(), addToBackStack = true)
            }
            R.id.nav_log_out -> AuthUI.getInstance().signOut(applicationContext)
        }
        drawer_layout.closeDrawer(Gravity.START)
        return true
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }
}
