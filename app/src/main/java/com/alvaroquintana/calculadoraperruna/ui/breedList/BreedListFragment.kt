package com.alvaroquintana.calculadoraperruna.ui.breedList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.alvaroquintana.calculadoraperruna.R
import com.alvaroquintana.calculadoraperruna.databinding.BreedListFragmentBinding
import com.alvaroquintana.calculadoraperruna.ui.MainActivity
import org.koin.android.scope.lifecycleScope
import org.koin.android.viewmodel.scope.viewModel


class BreedListFragment : Fragment() {
    private lateinit var binding: BreedListFragmentBinding

    private val breedListViewModel: BreedListViewModel by lifecycleScope.viewModel(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as MainActivity).setupToolbar(getString(R.string.choose_breed), true) { navigateToHome() }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = BreedListFragmentBinding.inflate(inflater, container, false)
        val root = binding.root

        breedListViewModel.init(activity as MainActivity)

        return root
    }

    private fun navigateToHome() {
        val action = BreedListFragmentDirections.actionNavigationBreedListToHome()
        findNavController().navigate(action)
    }
}