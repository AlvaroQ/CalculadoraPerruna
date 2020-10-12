package com.alvaroquintana.calculadoraperruna.ui.result

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.alvaroquintana.calculadoraperruna.R
import com.alvaroquintana.calculadoraperruna.databinding.ResultFragmentBinding
import com.alvaroquintana.calculadoraperruna.ui.MainActivity
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import org.koin.android.scope.lifecycleScope
import org.koin.android.viewmodel.scope.viewModel
import androidx.lifecycle.Observer
import com.alvaroquintana.calculadoraperruna.ui.components.AspectRatioImageView
import com.alvaroquintana.calculadoraperruna.ui.home.HomeFragmentArgs
import com.alvaroquintana.calculadoraperruna.utils.glideLoadBase64
import com.alvaroquintana.domain.Dog


class ResultFragment : Fragment() {
    private lateinit var binding: ResultFragmentBinding
    private val resultViewModel: ResultViewModel by lifecycleScope.viewModel(this)

    private val years by lazy { arguments?.let { ResultFragmentArgs.fromBundle(it).years } }
    private val months by lazy { arguments?.let { ResultFragmentArgs.fromBundle(it).months } }
    private val icon by lazy { arguments?.let { ResultFragmentArgs.fromBundle(it).icon } }
    private val name by lazy { arguments?.let { ResultFragmentArgs.fromBundle(it).name } }
    private val longevity by lazy { arguments?.let { ResultFragmentArgs.fromBundle(it).longevity } }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = ResultFragmentBinding.inflate(inflater, container, false)
        val root = binding.root

        resultViewModel.init()

        val btnSubmit: ExtendedFloatingActionButton = root.findViewById(R.id.btnSubmit)
        btnSubmit.setOnClickListener { resultViewModel.navigateHome() }


        val textTime: TextView = root.findViewById(R.id.textTime)
        when {
            years == 0 -> {
                val m: String = resources.getQuantityString(R.plurals.month, months!!, months)
                textTime.text = String.format(resources.getString(R.string.time_month), m)
            }
            months == 0 -> {
                val y: String = resources.getQuantityString(R.plurals.year, years!!, years)
                textTime.text = String.format(resources.getString(R.string.time_year), y)
            }
            else -> {
                val m: String = resources.getQuantityString(R.plurals.month, months!!, months)
                val y: String = resources.getQuantityString(R.plurals.year, years!!, years)
                textTime.text = String.format(resources.getString(R.string.time_year_month), y, m)
            }
        }

        val imageBreed: AspectRatioImageView = root.findViewById(R.id.imageBreed)
        glideLoadBase64((activity as MainActivity), icon, imageBreed)

        val textBreed: TextView = root.findViewById(R.id.textBreed)
        textBreed.text = name

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MainActivity).setupToolbar(getString(R.string.completed), true) { resultViewModel.navigateHome() }

        resultViewModel.navigation.observe(viewLifecycleOwner, Observer(::navigate))
    }

    private fun navigate(navigation: ResultViewModel.Navigation?) {
        (activity as MainActivity).apply {
            when (navigation) {
                ResultViewModel.Navigation.Home -> {
                    val action = ResultFragmentDirections.actionNavigationResultToHome("","",0)
                    findNavController().navigate(action)
                }
            }
        }
    }
}