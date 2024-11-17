package ru.practicum.android.diploma.ui.root

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.BuildConfig
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.ActivityRootBinding
import ru.practicum.android.diploma.viewmodels.MainActivityViewModel

class RootActivity : AppCompatActivity() {

    private val vModel: MainActivityViewModel by viewModel()

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
        }

        // Пример использования access token для HeadHunter API
        networkRequestExample(accessToken = BuildConfig.HH_ACCESS_TOKEN)
    }

    private fun networkRequestExample(accessToken: String) {
        // ...
    }
}
