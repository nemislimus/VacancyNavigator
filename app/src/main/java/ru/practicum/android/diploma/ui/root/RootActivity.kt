package ru.practicum.android.diploma.ui.root

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.ActivityRootBinding
import ru.practicum.android.diploma.ui.root.viewmodels.RootActivityViewModel

class RootActivity : AppCompatActivity() {

    private val vModel: RootActivityViewModel by viewModel()

    // private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val binding: ActivityRootBinding by lazy {
        ActivityRootBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNavigationPanel.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.filtrationFragment -> {
                    binding.bottomNavigationPanel.isVisible = false
                    binding.flDivider.isVisible = false
                }

                R.id.vacancyFragment -> {
                    binding.bottomNavigationPanel.isVisible = false
                    binding.flDivider.isVisible = false
                }

                else -> {
                    binding.bottomNavigationPanel.isVisible = true
                    binding.flDivider.isVisible = true
                }
            }

            vModel.sendVIewScreenEventToStat(
                screenName = destination.label.toString()
            )
        }
    }
    /* это задел для поиска по геолокации
        private fun getCurrentLocation() {
            val locationClient = LocationServices.getFusedLocationProviderClient(this)

            val result = if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                 //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }

            lifecycleScope.launch(Dispatchers.IO) {

                val result = locationClient.lastLocation.await()

                locationClient.lastLocation.await()
                val locationInfo = if (result == null) {
                    "No last known location. Try fetching the current location first"
                } else {
                    "Current location is \n" + "lat : ${result.latitude}\n" +
                        "long : ${result.longitude}\n" + "fetched at ${System.currentTimeMillis()}"
                }
            }
        }

        private fun preciseLocation() {
            val locationClient = LocationServices.getFusedLocationProviderClient(this)

            lifecycleScope.launch(Dispatchers.IO) {

                val priority = if (true) {
                    Priority.PRIORITY_HIGH_ACCURACY
                } else {
                    Priority.PRIORITY_BALANCED_POWER_ACCURACY
                }

                try {
                    val result = locationClient.getCurrentLocation(
                        priority,
                        CancellationTokenSource().token,
                    ).await()

                    result?.let { fetchedLocation ->
                        val locationInfo =
                            "Current location is \n" + "lat : ${fetchedLocation.latitude}\n" +
                                "long : ${fetchedLocation.longitude}\n" + "fetched at ${System.currentTimeMillis()}"
                    }
                } catch (er: SecurityException) {

                }
            }
        }

        private fun showLocationPermissionRequest() {

            shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)

            val locationPermissionRequest = registerForActivityResult(
                ActivityResultContracts.RequestMultiplePermissions()
            ) { permissions ->
                when {
                    permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                        // Precise location access granted.

                    }

                    permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                        // Only approximate location access granted.
                    }

                    else -> {
                        // No location access granted.
                    }
                }
            }

            locationPermissionRequest.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
     */
}
