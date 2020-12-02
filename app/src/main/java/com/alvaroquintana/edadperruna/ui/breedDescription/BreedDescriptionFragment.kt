package com.alvaroquintana.edadperruna.ui.breedDescription

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.alvaroquintana.edadperruna.databinding.BreedDescriptionFragmentBinding
import com.alvaroquintana.edadperruna.ui.MainActivity
import com.alvaroquintana.edadperruna.ui.breedList.BreedListFragmentArgs
import org.koin.android.scope.lifecycleScope
import org.koin.android.viewmodel.scope.viewModel
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.alvaroquintana.domain.Dog
import com.alvaroquintana.edadperruna.R
import com.alvaroquintana.edadperruna.utils.glideLoadBase64
import com.alvaroquintana.edadperruna.utils.glideLoadURL

class BreedDescriptionFragment : Fragment() {
    private lateinit var binding : BreedDescriptionFragmentBinding
    private val breedDescriptionViewModel: BreedDescriptionViewModel by lifecycleScope.viewModel(this)

    private val icon by lazy { arguments?.let { BreedListFragmentArgs.fromBundle(it).icon } }
    private val name by lazy { arguments?.let { BreedListFragmentArgs.fromBundle(it).name } }
    private val id by lazy { arguments?.let { BreedListFragmentArgs.fromBundle(it).idBreed } }
    lateinit var dog: Dog

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        binding = BreedDescriptionFragmentBinding.inflate(inflater, container, false)
        val root = binding.root

        val btnSubmit: Button = root.findViewById(R.id.btnSubmit)
        btnSubmit.setOnClickListener {
            breedDescriptionViewModel.navigateToHome(dog)
        }

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
        dog = Dog(name = name, icon = icon, life = breedDescription.mainInformation?.lifeExpectancy?.expectancy?.toString())

        if(breedDescription.image != "") {
            glideLoadURL(requireContext(), breedDescription.image, binding.imageBreed)
        } else {
            glideLoadBase64(requireContext(), icon, binding.imageBreed)
        }

        binding.layoutImageResume.visibility = View.VISIBLE
        binding.imageSizeDescription.text = breedDescription.mainInformation?.sizeBreed
        binding.imageLifeDescription.text = resources.getQuantityString(R.plurals.year, breedDescription.mainInformation?.lifeExpectancy?.expectancy!!)

        binding.textBreedDescription.text = breedDescription.shortDescription

        if(breedDescription.fci?.group == 0L) {
            binding.textfciTitle.visibility = View.GONE
            binding.layoutGroupFCI.visibility = View.GONE
            binding.layoutSectionFCI.visibility = View.GONE
        } else {
            binding.textfciTitle.visibility = View.VISIBLE
            binding.layoutGroupFCI.visibility = View.VISIBLE
            binding.layoutSectionFCI.visibility = View.VISIBLE

            binding.textGroup.text = breedDescription.fci?.groupType
            binding.textSection.text = breedDescription.fci?.sectionType
        }

        if(breedDescription.otherNames?.size == 0) {
            binding.textOtherNamesTitle.visibility = View.GONE
            binding.textOtherNames.visibility = View.GONE
        } else {
            binding.textOtherNamesTitle.visibility = View.VISIBLE
            binding.textOtherNames.visibility = View.VISIBLE
            binding.textOtherNames.text = breedDescription.otherNames.toString()
        }

        if(breedDescription.commonDiseases?.size == 0) {
            binding.textDiseasesTitle.visibility = View.GONE
            binding.textDiseases.visibility = View.GONE
        } else {
            binding.textDiseasesTitle.visibility = View.VISIBLE
            binding.textDiseases.visibility = View.VISIBLE
            binding.textDiseases.text = breedDescription.otherNames.toString()
        }
    }

    private fun progressVisibility(isVisible: Boolean) {
        binding.imagenLoading.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    private fun loadAd(model: BreedDescriptionViewModel.UiModel) {
        if (model is BreedDescriptionViewModel.UiModel.ShowAd && model.show)
            (activity as MainActivity).showInstersticialAd()
    }

    private fun navigate(navigation: BreedDescriptionViewModel.Navigation?) {
        (activity as MainActivity).apply {
            when (navigation) {
                is BreedDescriptionViewModel.Navigation.Home -> {
                    val action = BreedDescriptionFragmentDirections.actionNavigationBreedDescriptionToHome(
                        navigation.breed.icon!!,
                        navigation.breed.name!!,
                        navigation.breed.life!!)
                    findNavController().navigate(action)
                }
            }
        }
    }
}