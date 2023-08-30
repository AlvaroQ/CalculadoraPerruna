package com.alvaroquintana.edadperruna.ui.moreApps

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.alvaroquintana.domain.App
import com.alvaroquintana.edadperruna.R
import com.alvaroquintana.edadperruna.databinding.MoreAppsFragmentBinding
import com.alvaroquintana.edadperruna.ui.MainActivity
import com.alvaroquintana.edadperruna.utils.glideLoadGif
import org.koin.androidx.viewmodel.ext.android.viewModel

class MoreAppsFragment : Fragment() {
    private lateinit var binding: MoreAppsFragmentBinding
    private val moreAppsViewModel: MoreAppsViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        binding = MoreAppsFragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).setupToolbar(getString(R.string.more_apps), hasSettings = false, hasBackButton = true)
        moreAppsViewModel.list.observe(viewLifecycleOwner, Observer(::fillList))
        moreAppsViewModel.progress.observe(viewLifecycleOwner, Observer(::updateProgress))
        moreAppsViewModel.showingAds.observe(viewLifecycleOwner, Observer(::loadAd))
        moreAppsViewModel.showingAds.observe(viewLifecycleOwner, Observer(::loadAd))
    }

    private fun fillList(appList: MutableList<App>) {
        binding.recyclerviewMoreApps.adapter = MoreAppsListAdapter(requireContext(), appList)
    }

    private fun loadAd(model: MoreAppsViewModel.UiModel) {
        if (model is MoreAppsViewModel.UiModel.ShowAd) (activity as MainActivity).showAd(model.show)
    }

    private fun updateProgress(model: MoreAppsViewModel.UiModel?) {
        if (model is MoreAppsViewModel.UiModel.Loading && model.show) {
            glideLoadGif(activity as MainActivity, binding.imagenLoading)
            binding.imagenLoading.visibility = View.VISIBLE
        } else {
            binding.imagenLoading.visibility = View.GONE
        }
    }
}