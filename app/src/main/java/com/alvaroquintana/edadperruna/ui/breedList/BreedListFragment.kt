package com.alvaroquintana.edadperruna.ui.breedList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.alvaroquintana.edadperruna.R
import com.alvaroquintana.edadperruna.databinding.BreedListFragmentBinding
import com.alvaroquintana.edadperruna.ui.MainActivity
import com.alvaroquintana.edadperruna.ui.helpers.ImagePreviewer
import com.alvaroquintana.edadperruna.utils.glideLoadGif
import com.alvaroquintana.domain.Dog
import com.alvaroquintana.edadperruna.utils.log
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.google.firebase.crashlytics.FirebaseCrashlytics
import org.koin.android.scope.lifecycleScope
import org.koin.android.viewmodel.scope.viewModel


class BreedListFragment : Fragment() {
    private lateinit var binding: BreedListFragmentBinding
    private val breedListViewModel: BreedListViewModel by lifecycleScope.viewModel(this)
    private lateinit var rewardedAd: RewardedAd

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {

        binding = BreedListFragmentBinding.inflate(inflater, container, false)
        val root = binding.root

        glideLoadGif(activity as MainActivity, binding.imageLoading)
        loadRewardAd()
        loadBannerAd(root.findViewById(R.id.adView))

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MainActivity).setupToolbar(getString(R.string.choose_breed), true) { breedListViewModel.onBackPressed() }
        (activity as MainActivity).setupBackground(MainActivity.Screen.BREED_LIST)

        breedListViewModel.init()
        breedListViewModel.progress.observe(viewLifecycleOwner, Observer(::progressVisibility))
        breedListViewModel.list.observe(viewLifecycleOwner, Observer(::fillBreedList))
        breedListViewModel.navigation.observe(viewLifecycleOwner, Observer(::navigate))
    }

    private fun fillBreedList(breedList: MutableList<Dog>) {
        binding.recycler.adapter = BreedListAdapter(
            activity as MainActivity,
            breedList,
            breedListViewModel::onDogClicked,
            breedListViewModel::onDogLongClicked
        )
    }

    private fun progressVisibility(isVisible: Boolean) {
        binding.imageLoading.visibility = if (isVisible) View.VISIBLE else View.GONE
    }


    private fun navigate(navigation: BreedListViewModel.Navigation?) {
        (activity as MainActivity).apply {
            when (navigation) {
                is BreedListViewModel.Navigation.Home -> {
                    val action = BreedListFragmentDirections.actionNavigationBreedListToHome(
                        navigation.breed.icon!!,
                        navigation.breed.name!!
                    )
                    findNavController().navigate(action)
                }
                is BreedListViewModel.Navigation.Expand -> {
                    expandImage(navigation.imageView, navigation.icon)
                }
            }
        }
    }

    private fun expandImage(imageView: ImageView, icon: String) {
        ImagePreviewer().show(activity as MainActivity, imageView, icon)
    }

    private fun loadBannerAd(mAdView: AdView) {
        MobileAds.initialize(activity as MainActivity)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)
    }

    private fun loadRewardAd() {
        rewardedAd = RewardedAd(requireContext(), getString(R.string.BONIFICADO_LIST))
        val adLoadCallback: RewardedAdLoadCallback = object : RewardedAdLoadCallback() {
            override fun onRewardedAdLoaded() {
                rewardedAd.show(activity, null)
            }

            override fun onRewardedAdFailedToLoad(adError: LoadAdError) {
                FirebaseCrashlytics.getInstance().recordException(Throwable(adError.message))
                log("ResultActivity - loadAd", "Ad failed to load.")
            }
        }
        rewardedAd.loadAd(AdRequest.Builder().build(), adLoadCallback)
    }

    override fun onStart() {
        super.onStart()
        requireActivity().onBackPressedDispatcher.addCallback(callback)
    }

    override fun onStop() {
        callback.remove()
        super.onStop()
    }

    private val callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            breedListViewModel.onBackPressed()
        }
    }
}