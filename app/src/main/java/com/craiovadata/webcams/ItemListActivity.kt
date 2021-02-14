package com.craiovadata.webcams

import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.RecyclerView
import com.craiovadata.webcams.LocationUtil.Companion.PERMISSIONS_REQUEST
import com.craiovadata.webcams.LocationUtil.Companion.getLocation
import com.google.android.gms.ads.*
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import timber.log.Timber
import java.util.*


/**
 * An activity representing a list of Pings. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a [ItemDetailActivity] representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
class ItemListActivity : AppCompatActivity() {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private var twoPane: Boolean = false
    private lateinit var adapter: ItemRecyclerViewAdapter
    private var fusedLocationClient: FusedLocationProviderClient? = null
//    private var location: Location? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_list)
        Timber.plant()

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.title = title

        if (findViewById<NestedScrollView>(R.id.item_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            twoPane = true
        }

        setupRecyclerView(findViewById(R.id.item_list))
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        initAd()

    }

    private fun initAd() {
        MobileAds.initialize(this) {}

        val testDeviceIds = listOf("9EDAF80E0B8DCB545330A07BD675095F")  // Moto G7 play
        val configuration = RequestConfiguration.Builder().setTestDeviceIds(testDeviceIds).build()
        MobileAds.setRequestConfiguration(configuration)

        val mAdView = findViewById<AdView>(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)


        mAdView.adListener = object: AdListener() {
            override fun onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                findViewById<AdView>(R.id.adView).visibility = View.VISIBLE
            }
        }
    }

    override fun onResume() {
        super.onResume()
        initFlow()
    }

    private fun initFlow() {
        findViewById<ProgressBar>(R.id.progressBar).visibility = View.VISIBLE
        getLocation(this, fusedLocationClient) { location ->
//        TODO    updateUI(). Show my location on screen
            if (location == null) {
                refreshMenuItem?.isVisible = true
                return@getLocation
            }
            refreshMenuItem?.isVisible = false
            triggerFetchWebcams(location)
        }
    }

    private fun triggerFetchWebcams(location: Location) {
        NetworkDataSource(this).fetchWebcams(
            location.latitude,
            location.longitude
        ) { items ->
            findViewById<ProgressBar>(R.id.progressBar).visibility = View.GONE
            if (items.isNotEmpty()) {
                showListWebcams()
                adapter.values = items.toList()
                adapter.notifyDataSetChanged()
            } else {
                hideListWebcams()
                refreshMenuItem?.isVisible = true
                Toast.makeText(
                    this@ItemListActivity,
                    getString(R.string.toast_no_webcams_found),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun hideListWebcams() {
        findViewById<RecyclerView>(R.id.item_list).visibility = View.GONE
    }

    private fun showListWebcams() {
        findViewById<RecyclerView>(R.id.item_list).visibility = View.VISIBLE
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSIONS_REQUEST) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                getLocation(this, fusedLocationClient) { location ->
                    if (location == null) {
                        findViewById<ProgressBar>(R.id.progressBar).visibility = View.GONE
                        hideListWebcams()
                    } else
                        triggerFetchWebcams(location)
                }
        }
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {
        adapter = ItemRecyclerViewAdapter(this, twoPane)
        recyclerView.adapter = adapter
    }

    var refreshMenuItem: MenuItem? = null
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        refreshMenuItem = menu.findItem(R.id.action_refresh)
        refreshMenuItem?.isVisible = false
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_refresh -> {
                initFlow()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}


