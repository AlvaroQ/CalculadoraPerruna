package com.alvaroquintana.edadperruna.ui.breedDescription

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.alvaroquintana.domain.Dog
import com.alvaroquintana.edadperruna.R
import com.alvaroquintana.edadperruna.databinding.BreedDescriptionFragmentBinding
import com.alvaroquintana.edadperruna.ui.MainActivity
import com.alvaroquintana.edadperruna.ui.breedList.BreedListFragmentArgs
import com.alvaroquintana.edadperruna.utils.glideLoadBase64
import com.alvaroquintana.edadperruna.utils.glideLoadGif
import com.alvaroquintana.edadperruna.utils.glideLoadURL
import org.koin.android.scope.lifecycleScope
import org.koin.android.viewmodel.scope.viewModel

class BreedDescriptionFragment : Fragment() {
    private lateinit var binding : BreedDescriptionFragmentBinding
    private val breedDescriptionViewModel: BreedDescriptionViewModel by lifecycleScope.viewModel(this)

    private val image by lazy { arguments?.let { BreedListFragmentArgs.fromBundle(it).image } }
    private val name by lazy { arguments?.let { BreedListFragmentArgs.fromBundle(it).name } }
    private val id by lazy { arguments?.let { BreedListFragmentArgs.fromBundle(it).idBreed } }
    lateinit var dog: Dog

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        binding = BreedDescriptionFragmentBinding.inflate(inflater, container, false)
        val root = binding.root

        val imageBreed: ImageView = root.findViewById(R.id.imageBreed)
        glideLoadURL(requireContext(), image, imageBreed)

        val textBreed: TextView = root.findViewById(R.id.textBreed)
        textBreed.text = name

        val btnSubmit: Button = root.findViewById(R.id.btnSubmit)
        btnSubmit.setOnClickListener { breedDescriptionViewModel.navigateToHome(dog) }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MainActivity).setupToolbar(name!!, hasSettings = false, hasBackButton = true)
        breedDescriptionViewModel.getDescription(id!!)

        breedDescriptionViewModel.progress.observe(viewLifecycleOwner, Observer(::progressVisibility))
        breedDescriptionViewModel.showingAds.observe(viewLifecycleOwner, Observer(::loadAd))
        breedDescriptionViewModel.navigation.observe(viewLifecycleOwner, Observer(::navigate))
        breedDescriptionViewModel.breedData.observe(viewLifecycleOwner, Observer(::fillBreedDescription))
    }

    private fun fillBreedDescription(breedDescription: Dog) {
        dog = Dog(name = name, image = image, life = breedDescription.mainInformation?.lifeExpectancy?.expectancy?.toString())

        binding.cardResume.visibility = View.VISIBLE
        binding.imageSizeDescription.text = breedDescription.mainInformation?.sizeBreed
        binding.imageLifeDescription.text = String.format(getString(R.string.life_expectative_text), breedDescription.mainInformation?.lifeExpectancy?.expectancy!!)
        binding.textBreedDescription.text = breedDescription.shortDescription

        if(breedDescription.fci?.group == 0L) {
            binding.cardFCI.visibility = View.GONE
        } else {
            binding.cardFCI.visibility = View.VISIBLE
            binding.textTitleGroup.text = String.format(getString(R.string.fci_group), breedDescription.fci?.group)
            binding.textGroup.text = breedDescription.fci?.groupType
            binding.textTitleSection.text = String.format(getString(R.string.fci_section), breedDescription.fci?.section)
            binding.textSection.text = breedDescription.fci?.sectionType
        }

        if(breedDescription.otherNames?.size == 0) {
            binding.cardOtherNames.visibility = View.GONE
        } else {
            binding.cardOtherNames.visibility = View.VISIBLE
            binding.textOtherNames.text = breedDescription.otherNames?.joinToString(", ")
        }

        if(breedDescription.commonDiseases?.size == 0) {
            binding.cardDiseases.visibility = View.GONE
        } else {
            binding.cardDiseases.visibility = View.VISIBLE
            binding.textDiseases.text = breedDescription.commonDiseases?.joinToString(", ")
        }
        binding.btnSubmit.isEnabled = true
    }

    private fun progressVisibility(isVisible: Boolean) {
        glideLoadGif(activity as MainActivity, binding.imagenLoading)
        binding.imagenLoading.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    private fun loadAd(model: BreedDescriptionViewModel.UiModel) {
        if (model is BreedDescriptionViewModel.UiModel.ShowAd && model.show)
            (activity as MainActivity).showInstersticialAd()
    }

    private fun navigate(navigation: BreedDescriptionViewModel.Navigation?) {
        when (navigation) {
            is BreedDescriptionViewModel.Navigation.Home -> {
                val action = BreedDescriptionFragmentDirections.actionNavigationBreedDescriptionToHome(
                    navigation.breed.image!!,
                    navigation.breed.name!!,
                    navigation.breed.life!!)
                findNavController().navigate(action)
            }
        }
    }
}