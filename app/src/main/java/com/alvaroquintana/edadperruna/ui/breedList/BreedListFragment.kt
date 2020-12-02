package com.alvaroquintana.edadperruna.ui.breedList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.alvaroquintana.domain.Dog
import com.alvaroquintana.edadperruna.R
import com.alvaroquintana.edadperruna.databinding.BreedListFragmentBinding
import com.alvaroquintana.edadperruna.ui.MainActivity
import com.alvaroquintana.edadperruna.ui.helpers.ImagePreviewer
import com.alvaroquintana.edadperruna.utils.glideLoadGif
import org.koin.android.scope.lifecycleScope
import org.koin.android.viewmodel.scope.viewModel


class BreedListFragment : Fragment() {
    private lateinit var binding: BreedListFragmentBinding
    private val breedListViewModel: BreedListViewModel by lifecycleScope.viewModel(this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {

        binding = BreedListFragmentBinding.inflate(inflater, container, false)
        val root = binding.root
        glideLoadGif(activity as MainActivity, binding.imageLoading)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MainActivity).setupToolbar(getString(R.string.choose_breed), hasSettings = false, hasBackButton = true)
        (activity as MainActivity).setupBackground(MainActivity.Screen.BREED_LIST)

        breedListViewModel.init()
        breedListViewModel.progress.observe(viewLifecycleOwner, Observer(::progressVisibility))
        breedListViewModel.list.observe(viewLifecycleOwner, Observer(::fillBreedList))
        breedListViewModel.navigation.observe(viewLifecycleOwner, Observer(::navigate))
        breedListViewModel.showingAds.observe(viewLifecycleOwner, Observer(::loadAd))
    }

    private fun fillBreedList(breedList: MutableList<Dog>) {
        binding.recyclerList.adapter = BreedListAdapter(
            requireContext(),
            breedList,
            breedListViewModel::onDogClicked,
            breedListViewModel::onDogLongClicked
        )
    }

    private fun progressVisibility(isVisible: Boolean) {
        binding.imageLoading.visibility = if (isVisible) View.VISIBLE else View.GONE
    }


    private fun navigate(navigation: BreedListViewModel.Navigation?) {
        (activity as MainActivity).apply {
            when (navigation) {
                is BreedListViewModel.Navigation.BreedDescription -> {
                    val action = BreedListFragmentDirections.actionNavigationBreedListToBreedDescription(
                        navigation.breed.icon!!,
                        navigation.breed.name!!,
                        navigation.idBreed.toString())
                    findNavController().navigate(action)
                }
                is BreedListViewModel.Navigation.Expand -> {
                    expandImage(navigation.imageView, navigation.icon)
                }
            }
        }
    }

    private fun expandImage(imageView: ImageView, icon: String) {
        ImagePreviewer().show(activity as MainActivity, imageView, icon)
    }

    private fun loadAd(model: BreedListViewModel.UiModel) {
        if (model is BreedListViewModel.UiModel.ShowAd && model.show) {
            (activity as MainActivity).showRewardAd()
            (activity as MainActivity).showAd(model.show)
        }
    }
}