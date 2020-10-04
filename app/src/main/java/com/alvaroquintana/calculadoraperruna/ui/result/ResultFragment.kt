package com.alvaroquintana.calculadoraperruna.ui.result

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.alvaroquintana.calculadoraperruna.R
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import org.koin.android.scope.lifecycleScope
import org.koin.android.viewmodel.scope.viewModel


class ResultFragment : Fragment() {
    private val resultViewModel: ResultViewModel by lifecycleScope.viewModel(this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        val root = inflater.inflate(R.layout.result_fragment, container, false)

        val backButton: ImageView = root.findViewById(R.id.backButton)
        backButton.setOnClickListener {
            val action = ResultFragmentDirections.actionNavigationResultToHome()
            findNavController().navigate(action)
        }

        val btnSubmit: ExtendedFloatingActionButton = root.findViewById(R.id.btnSubmit)
        btnSubmit.setOnClickListener {
            val action = ResultFragmentDirections.actionNavigationResultToHome()
            findNavController().navigate(action)
        }

        return root

    }
}