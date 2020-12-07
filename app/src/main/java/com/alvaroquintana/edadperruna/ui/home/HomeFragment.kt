package com.alvaroquintana.edadperruna.ui.home

import android.content.res.Resources
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.alvaroquintana.domain.Dog
import com.alvaroquintana.edadperruna.R
import com.alvaroquintana.edadperruna.databinding.MainFragmentBinding
import com.alvaroquintana.edadperruna.ui.MainActivity
import com.alvaroquintana.edadperruna.ui.home.HomeFragmentArgs.Companion.fromBundle
import com.alvaroquintana.edadperruna.utils.getScreenHeight
import com.alvaroquintana.edadperruna.utils.getScreenWidth
import com.alvaroquintana.edadperruna.utils.glideLoadBase64
import com.alvaroquintana.edadperruna.utils.hideKeyboard
import org.koin.android.scope.lifecycleScope
import org.koin.android.viewmodel.scope.viewModel


class HomeFragment : Fragment() {
    private lateinit var binding: MainFragmentBinding
    private val homeViewModel: HomeViewModel by lifecycleScope.viewModel(this)
    private lateinit var editTextMonth: EditText
    private lateinit var editTextYear: EditText

    private val image by lazy { arguments?.let { fromBundle(it).icon } }
    private val name by lazy { arguments?.let { fromBundle(it).name } }
    private val life by lazy { arguments?.let { fromBundle(it).life } }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = MainFragmentBinding.inflate(inflater, container, false)
        val root = binding.root

        if(!(activity as MainActivity).appOpened) {
            loadAnimationScreen()
            (activity as MainActivity).appOpened = true
        }

        val parentLayout: ConstraintLayout = root.findViewById(R.id.parentLayout)
        parentLayout.setOnClickListener { hideKeyboard(activity as MainActivity) }

        val constraintSelectBreed: ConstraintLayout = root.findViewById(R.id.constraintSelectBreed)
        constraintSelectBreed.setOnClickListener { homeViewModel.navigateToBreedList() }

        editTextMonth = root.findViewById(R.id.editTextMonth)
        editTextYear = root.findViewById(R.id.editTextYear)

        val btnSubmit: Button = root.findViewById(R.id.btnSubmit)
        btnSubmit.setOnClickListener {
            val dog = Dog(icon = image!!, name = name!!, life = life)

            if(homeViewModel.checkErrors(
                    dog,
                    editTextYear.text.toString(),
                    editTextMonth.text.toString()
                )) {
                homeViewModel.navigateToResult(dog)
            }
        }

        val imageBreed: ImageView = root.findViewById(R.id.imageBreed)
        val breedText: TextView = root.findViewById(R.id.textBreed)
        if(image != "" && name != "") {
            breedText.text = name
            glideLoadBase64((activity as MainActivity), image, imageBreed)
            imageBreed.visibility = View.VISIBLE
        } else {
            breedText.text = getString(R.string.select_breed)
            imageBreed.visibility = View.GONE
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).setupToolbar(
            getString(R.string.app_name),
            hasSettings = true,
            hasBackButton = false
        )

        homeViewModel.navigation.observe(viewLifecycleOwner, Observer(::navigate))
        homeViewModel.error.observe(viewLifecycleOwner, Observer(::showError))
        homeViewModel.showingAds.observe(viewLifecycleOwner, Observer(::loadAd))
    }

    private fun loadAnimationScreen() {
        binding.textInfo.x = getScreenWidth()
        binding.textInfo.animate().translationX(0f).setDuration(500).start()

        binding.constraintSelectBreed.x = -getScreenWidth()
        binding.constraintSelectBreed.animate().translationX(0f).setDuration(500).setStartDelay(500).start()

        binding.fieldYear.x = -getScreenWidth()
        binding.fieldYear.animate().translationX(0f).setDuration(500).setStartDelay(600).start()

        binding.fieldMonth.x = -getScreenWidth()
        binding.fieldMonth.animate().translationX(0f).setDuration(500).setStartDelay(700).start()

        binding.btnSubmit.y = getScreenHeight()
        binding.btnSubmit.animate().translationY(0f).setDuration(500).setStartDelay(800) .start()
    }

    private fun navigate(navigation: HomeViewModel.Navigation?) {
        (activity as MainActivity).apply {
            when (navigation) {
                HomeViewModel.Navigation.BreedList -> {
                    hideKeyboard(this)
                    val action = HomeFragmentDirections.actionNavigationHomeToBreedList()
                    findNavController().navigate(action)
                }
                is HomeViewModel.Navigation.Result -> {
                    hideKeyboard(this)
                    val action = HomeFragmentDirections.actionNavigationHomeToResult(
                        editTextYear.text.toString().toInt(),
                        editTextMonth.text.toString().toInt(),
                        navigation.breed.icon!!,
                        navigation.breed.name!!,
                        navigation.breed.life!!
                    )
                    findNavController().navigate(action)
                }
            }
        }
    }

    private fun showError(error: HomeViewModel.Error) {
        (activity as MainActivity).apply {
            when(error) {
                HomeViewModel.Error.ErrorBreedEmpty -> {
                    binding.constraintSelectBreed.background = getDrawable(
                        R.drawable.border_error
                    )
                }
                HomeViewModel.Error.ErrorYearEmpty -> {
                    binding.fieldYear.error = getString(R.string.fill_year)
                }
                HomeViewModel.Error.ErrorMonthEmpty -> {
                    binding.fieldMonth.error = getString(R.string.fill_month)
                }
                HomeViewModel.Error.ErrorMonthIlegal -> {
                    binding.fieldMonth.error = getString(R.string.ilegal_month)
                }
            }
        }
    }

    private fun loadAd(model: HomeViewModel.UiModel) {
        if (model is HomeViewModel.UiModel.ShowAd) (activity as MainActivity).showAd(model.show)
    }
}