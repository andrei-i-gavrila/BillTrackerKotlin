package ro.codespace.billtracker.ui.activity

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import ro.codespace.billtracker.R
import ro.codespace.billtracker.synchronizer.SyncService
import ro.codespace.billtracker.ui.fragment.BillsFragment
import java.util.concurrent.ScheduledThreadPoolExecutor
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private lateinit var scheduledThreadPoolExecutor: ScheduledThreadPoolExecutor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, BillsFragment())
            .commit()


        val cm = applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        cm.registerNetworkCallback(NetworkRequest.Builder().addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET).build(), object : ConnectivityManager.NetworkCallback() {
            var running = false

            init {
                scheduledThreadPoolExecutor = ScheduledThreadPoolExecutor(1)
                scheduledThreadPoolExecutor.scheduleWithFixedDelay({
                    if (running) {
                        SyncService.run()
                    }
                }, 0, 15, TimeUnit.SECONDS)
            }

            override fun onAvailable(network: Network?) {
                super.onAvailable(network)
                running = true
                Log.i("NETWORKCALLBACK", "connection received")
            }

            override fun onLost(network: Network?) {
                super.onLost(network)
                runOnUiThread {
                    Toast.makeText(applicationContext, "Network currently not available. All server side actions will be resumed when connection is available.", Toast.LENGTH_LONG).show()
                }
                Log.i("NETWORKCALLBACK", "connection lost")
                running = true
            }
        })
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
        scheduledThreadPoolExecutor.shutdown()
    }
}