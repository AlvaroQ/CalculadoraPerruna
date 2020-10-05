package com.alvaroquintana.calculadoraperruna.ui.result

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.alvaroquintana.calculadoraperruna.R
import com.alvaroquintana.calculadoraperruna.databinding.ResultFragmentBinding
import com.alvaroquintana.calculadoraperruna.ui.MainActivity
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import org.koin.android.scope.lifecycleScope
import org.koin.android.viewmodel.scope.viewModel


class ResultFragment : Fragment() {
    private lateinit var binding: ResultFragmentBinding
    private val resultViewModel: ResultViewModel by lifecycleScope.viewModel(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as MainActivity).setupToolbar(getString(R.string.completed), true) { navigateHome() }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = ResultFragmentBinding.inflate(inflater, container, false)
        val root = binding.root

        resultViewModel.init()

        val btnSubmit: ExtendedFloatingActionButton = root.findViewById(R.id.btnSubmit)
        btnSubmit.setOnClickListener { navigateHome() }

        return root
    }

    private fun navigateHome() {
        val action = ResultFragmentDirections.actionNavigationResultToHome()
        findNavController().navigate(action)
    }
}