package com.alvaroquintana.calculadoraperruna.ui

import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import com.alvaroquintana.calculadoraperruna.R
import androidx.navigation.findNavController
import com.alvaroquintana.calculadoraperruna.base.BaseActivity
import com.alvaroquintana.calculadoraperruna.common.viewBinding
import com.alvaroquintana.calculadoraperruna.databinding.MainActivityBinding
import org.koin.android.scope.lifecycleScope
import org.koin.android.viewmodel.scope.viewModel

class MainActivity : BaseActivity() {
    private val binding by viewBinding(MainActivityBinding::inflate)
    private lateinit var navController : NavController
    private val mainViewModel: MainViewModel by lifecycleScope.viewModel(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        mainViewModel.setModeNight()
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        navController = findNavController(R.id.nav_host_fragment)
    }

    fun setupToolbar(title: String, hasBackButton: Boolean, myBackFunction:  () -> Unit = {}) {
        binding.toolbarTitle.text = title
        if(hasBackButton) {
            binding.backButton.visibility = View.VISIBLE
            binding.backButton.setOnClickListener { myBackFunction() }
        } else {
            binding.backButton.visibility = View.INVISIBLE
        }
    }

    fun progressVisibility(isVisible: Boolean) {
        binding.progressBar.visibility = if (isVisible) View.VISIBLE else View.GONE
    }
}