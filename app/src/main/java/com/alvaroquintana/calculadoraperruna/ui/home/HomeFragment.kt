package com.alvaroquintana.calculadoraperruna.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
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
import com.alvaroquintana.calculadoraperruna.ui.home.HomeFragmentArgs.Companion.fromBundle
import com.alvaroquintana.calculadoraperruna.utils.glideLoadBase64
import com.alvaroquintana.domain.Dog

class HomeFragment : Fragment() {

    private lateinit var binding: MainFragmentBinding
    private val homeViewModel: HomeViewModel by lifecycleScope.viewModel(this)
    private lateinit var editTextMonth: EditText
    private lateinit var editTextYear: EditText

    private val icon by lazy { arguments?.let { fromBundle(it).icon } }
    private val name by lazy { arguments?.let { fromBundle(it).name } }
    private val longevity by lazy { arguments?.let { fromBundle(it).longevity } }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = MainFragmentBinding.inflate(inflater, container, false)
        val root = binding.root

        val parentLayout: ConstraintLayout = root.findViewById(R.id.parentLayout)
        parentLayout.setOnClickListener { hideKeyboard(activity as MainActivity) }

        val constraintSelectBreed: RelativeLayout = root.findViewById(R.id.constraintSelectBreed)
        constraintSelectBreed.setOnClickListener { homeViewModel.navigateToBreedList() }

        editTextMonth = root.findViewById(R.id.editTextMonth)
        editTextYear = root.findViewById(R.id.editTextYear)

        val btnSubmit: ExtendedFloatingActionButton = root.findViewById(R.id.btnSubmit)
        btnSubmit.setOnClickListener {
            val dog = Dog(icon!!, longevity!!, name!!)

            if(homeViewModel.checkErrors(dog, editTextYear.text.toString(), editTextMonth.text.toString())) {
                homeViewModel.navigateToResult(dog)
            }
        }

        val imageBreed: ImageView = root.findViewById(R.id.imageBreed)
        val breedText: TextView = root.findViewById(R.id.textBreed)
        if(icon != "" && name != "") {
            breedText.text = name
            glideLoadBase64((activity as MainActivity),  icon, imageBreed)
            imageBreed.visibility = View.VISIBLE
        } else {
            breedText.text = getString(R.string.select_breed)
            imageBreed.visibility = View.GONE
        }


        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MainActivity).setupToolbar(getString(R.string.app_name), false)

        homeViewModel.navigation.observe(viewLifecycleOwner, Observer(::navigate))
        homeViewModel.error.observe(viewLifecycleOwner, Observer(::showError))
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
                    val action = HomeFragmentDirections.actionNavigationHomeToResult(
                        editTextYear.text.toString().toInt(),
                        editTextMonth.text.toString().toInt(),
                        navigation.breed.icon!!,
                        navigation.breed.name!!,
                        navigation.breed.longevidad!!
                    )
                    findNavController().navigate(action)
                }
            }
        }
    }

    private fun showError(error: HomeViewModel.Error) {
        (activity as MainActivity).apply {
            when(error) {
                HomeViewModel.Error.ErrorBreedEmpty -> { binding.constraintSelectBreed.background = getDrawable(R.drawable.border_error)}
                HomeViewModel.Error.ErrorYearEmpty -> { binding.fieldYear.error = getString(R.string.fill_year) }
                HomeViewModel.Error.ErrorMonthEmpty -> { binding.fieldMonth.error = getString(R.string.fill_month) }
            }
        }
    }
}