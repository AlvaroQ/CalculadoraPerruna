package com.alvaroquintana.edadperruna.ui.breedList

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alvaroquintana.domain.Dog
import com.alvaroquintana.edadperruna.R
import com.alvaroquintana.edadperruna.databinding.BreedListFragmentBinding
import com.alvaroquintana.edadperruna.ui.MainActivity
import com.alvaroquintana.edadperruna.utils.expandImage
import com.alvaroquintana.edadperruna.utils.glideLoadGif
import com.alvaroquintana.edadperruna.utils.hideKeyboard
import org.koin.android.scope.lifecycleScope
import org.koin.android.viewmodel.scope.viewModel
import java.util.*


class BreedListFragment : Fragment() {
    private lateinit var binding: BreedListFragmentBinding
    private val breedListViewModel: BreedListViewModel by lifecycleScope.viewModel(this)

    lateinit var allBreedList: MutableList<Dog>
    lateinit var adapter: BreedListAdapter
    var isOrderByDescending = false
    var spanCount = 3

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        binding = BreedListFragmentBinding.inflate(inflater, container, false)
        val root = binding.root

        binding.imageAdvanceFilter.setOnClickListener { orderBy() }
        binding.imageNumberColumn.setOnClickListener {
            if(spanCount == 5) spanCount = 1 else spanCount++
            hideKeyboard(requireActivity())
            (binding.recyclerList.layoutManager as GridLayoutManager).spanCount = spanCount
        }
        binding.imageBack.setOnClickListener {
            requireActivity().apply {
                hideKeyboard(this)
                onBackPressed()
            }

        }
        binding.descriptionEdit.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterByName(s.toString())
            }
        })
        binding.recyclerList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                hideKeyboard(requireActivity())
            }
        })

        glideLoadGif(activity as MainActivity, binding.imageLoading)
        return root
    }
    private fun orderBy() {
        isOrderByDescending = !isOrderByDescending
        binding.imageAdvanceFilter.animate().rotation(binding.imageAdvanceFilter.rotation + 180f)

        if(isOrderByDescending) allBreedList.sortByDescending { it.name }
        else allBreedList.sortBy { it.name }

        updateRecyclerList(allBreedList)
    }

    private fun filterByName(textName: String) {
        val newList: MutableList<Dog> = mutableListOf()
        for (item in allBreedList) {
            if (item.name!!.toUpperCase(Locale.ROOT).contains(textName.toUpperCase(Locale.ROOT)) ||
                    item.otherNames.toString().toUpperCase(Locale.ROOT).contains(textName.toUpperCase(Locale.ROOT))) {
                newList.add(item)
            }
        }
        updateRecyclerList(newList)
    }

    private fun updateRecyclerList(newList: MutableList<Dog>) {
        adapter.data = newList
        binding.recyclerList.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).setupToolbar(getString(R.string.choose_breed), hasSettings = false, hasBackButton = true)

        breedListViewModel.progress.observe(viewLifecycleOwner, Observer(::progressVisibility))
        breedListViewModel.list.observe(viewLifecycleOwner, Observer(::fillBreedList))
        breedListViewModel.navigation.observe(viewLifecycleOwner, Observer(::navigate))
        breedListViewModel.showingAds.observe(viewLifecycleOwner, Observer(::loadAd))
    }

    private fun fillBreedList(breedList: MutableList<Dog>) {
        adapter = BreedListAdapter(
                requireContext(),
                breedListViewModel::onDogClicked,
                breedListViewModel::onDogLongClicked)
        allBreedList = breedList
        updateRecyclerList(allBreedList)
    }

    private fun progressVisibility(isVisible: Boolean) {
        binding.imageLoading.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    private fun navigate(navigation: BreedListViewModel.Navigation?) {
        hideKeyboard(requireActivity())
        (activity as MainActivity).apply {
            when (navigation) {
                is BreedListViewModel.Navigation.BreedDescription -> {
                    val action = BreedListFragmentDirections.actionNavigationBreedListToBreedDescription(
                            navigation.breed.image!!,
                            navigation.breed.name!!,
                            navigation.idBreed.toString())
                    findNavController().navigate(action)
                }
                is BreedListViewModel.Navigation.Expand -> {
                    expandImage(activity, navigation.imageView, navigation.image)
                }
            }
        }
    }

    private fun loadAd(model: BreedListViewModel.UiModel) {
        if (model is BreedListViewModel.UiModel.ShowAd && model.show) {
            (activity as MainActivity).showRewardAd()
            (activity as MainActivity).showAd(model.show)
        }
    }
}