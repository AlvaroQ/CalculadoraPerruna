package com.alvaroquintana.calculadoraperruna.ui.breedList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.alvaroquintana.calculadoraperruna.R
import com.alvaroquintana.calculadoraperruna.databinding.BreedListFragmentBinding
import com.alvaroquintana.calculadoraperruna.ui.MainActivity
import com.alvaroquintana.calculadoraperruna.utils.glideLoadGif
import com.alvaroquintana.domain.Dog
import org.koin.android.scope.lifecycleScope
import org.koin.android.viewmodel.scope.viewModel


class BreedListFragment : Fragment() {
    private lateinit var binding: BreedListFragmentBinding
    private val breedListViewModel: BreedListViewModel by lifecycleScope.viewModel(this)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = BreedListFragmentBinding.inflate(inflater, container, false)
        val root = binding.root

        glideLoadGif(activity as MainActivity, binding.imageLoading)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MainActivity).setupToolbar(getString(R.string.choose_breed), true) { breedListViewModel.onBackPressed() }

        breedListViewModel.init()
        breedListViewModel.progress.observe(viewLifecycleOwner, Observer(::progressVisibility))
        breedListViewModel.list.observe(viewLifecycleOwner, Observer(::fillBreedList))
        breedListViewModel.navigation.observe(viewLifecycleOwner, Observer(::navigate))
    }

    private fun fillBreedList(breedList: MutableList<Dog>) {
        binding.recycler.adapter = BreedListAdapter(activity as MainActivity, breedList, breedListViewModel::onDogClicked, breedListViewModel::onDogLongClicked)
    }

    private fun progressVisibility(isVisible: Boolean) {
        binding.imageLoading.visibility = if (isVisible) View.VISIBLE else View.GONE
    }


    private fun navigate(navigation: BreedListViewModel.Navigation?) {
        (activity as MainActivity).apply {
            when (navigation) {
                is BreedListViewModel.Navigation.Home -> {
                    val action = BreedListFragmentDirections.actionNavigationBreedListToHome(
                        navigation.breed.icon!!,
                        navigation.breed.name!!,
                        navigation.breed.longevidad!!
                    )
                    findNavController().navigate(action)
                }
                is BreedListViewModel.Navigation.Expand -> {
                    // Expand Image
                }
            }
        }
    }
}