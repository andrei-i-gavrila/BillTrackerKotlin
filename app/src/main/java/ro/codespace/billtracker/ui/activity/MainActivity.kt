package ro.codespace.billtracker.ui.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import ro.codespace.billtracker.R
import ro.codespace.billtracker.ui.fragment.BillsFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, BillsFragment())
            .commit()
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }
}