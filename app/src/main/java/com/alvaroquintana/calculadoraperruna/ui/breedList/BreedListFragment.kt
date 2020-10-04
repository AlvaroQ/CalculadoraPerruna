package com.alvaroquintana.calculadoraperruna.ui.breedList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.alvaroquintana.calculadoraperruna.R
import org.koin.android.scope.lifecycleScope
import org.koin.android.viewmodel.scope.viewModel


class BreedListFragment : Fragment() {
    private val breedListViewModel: BreedListViewModel by lifecycleScope.viewModel(this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        val root = inflater.inflate(R.layout.breed_list_fragment, container, false)

        val backButton: ImageView = root.findViewById(R.id.backButton)
        backButton.setOnClickListener {
            val action = BreedListFragmentDirections.actionNavigationBreedListToHome()
            findNavController().navigate(action)
        }

        return root

    }
}