package com.alvaroquintana.calculadoraperruna.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.alvaroquintana.calculadoraperruna.R
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import org.koin.android.scope.lifecycleScope
import org.koin.android.viewmodel.scope.viewModel

class HomeFragment : Fragment() {
    private val homeViewModel: HomeViewModel by lifecycleScope.viewModel(this)


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val root = inflater.inflate(R.layout.main_fragment, container, false)

        val constraintSelectBreed: RelativeLayout = root.findViewById(R.id.constraintSelectBreed)
        constraintSelectBreed.setOnClickListener {
            val action = HomeFragmentDirections.actionNavigationHomeToBreedList()
            findNavController().navigate(action)
        }

        val btnSubmit: ExtendedFloatingActionButton = root.findViewById(R.id.btnSubmit)
        btnSubmit.setOnClickListener {
            val action = HomeFragmentDirections.actionNavigationHomeToResult()
            findNavController().navigate(action)
        }

        return root
    }

}