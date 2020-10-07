package com.alvaroquintana.calculadoraperruna.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.alvaroquintana.calculadoraperruna.R
import com.alvaroquintana.calculadoraperruna.databinding.MainFragmentBinding
import com.alvaroquintana.calculadoraperruna.ui.MainActivity
import com.alvaroquintana.calculadoraperruna.utils.hideKeyboard
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import org.koin.android.scope.lifecycleScope
import org.koin.android.viewmodel.scope.viewModel
import androidx.lifecycle.Observer

class HomeFragment : Fragment() {

    private lateinit var binding: MainFragmentBinding
    private val homeViewModel: HomeViewModel by lifecycleScope.viewModel(this)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = MainFragmentBinding.inflate(inflater, container, false)
        val root = binding.root

        val parentLayout: ConstraintLayout = root.findViewById(R.id.parentLayout)
        parentLayout.setOnClickListener { hideKeyboard(activity as MainActivity) }

        val constraintSelectBreed: RelativeLayout = root.findViewById(R.id.constraintSelectBreed)
        constraintSelectBreed.setOnClickListener { homeViewModel.navigateToBreedList() }

        val btnSubmit: ExtendedFloatingActionButton = root.findViewById(R.id.btnSubmit)
        btnSubmit.setOnClickListener { homeViewModel.navigateToResult() }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MainActivity).setupToolbar(getString(R.string.app_name), false)

        homeViewModel.navigation.observe(viewLifecycleOwner, Observer(::navigate))
    }

    private fun navigate(navigation: HomeViewModel.Navigation?) {
        (activity as MainActivity).apply {
            when (navigation) {
                HomeViewModel.Navigation.BreedList -> {
                    hideKeyboard(activity as MainActivity)
                    val action = HomeFragmentDirections.actionNavigationHomeToBreedList()
                    findNavController().navigate(action)
                }
                is HomeViewModel.Navigation.Result -> {
                    hideKeyboard(activity as MainActivity)
                    val action = HomeFragmentDirections.actionNavigationHomeToResult() // navigation.dogId
                    findNavController().navigate(action)
                }
            }
        }
    }
}