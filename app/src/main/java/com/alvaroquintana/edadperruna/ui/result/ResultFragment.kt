package com.alvaroquintana.edadperruna.ui.result

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.alvaroquintana.edadperruna.R
import com.alvaroquintana.edadperruna.databinding.ResultFragmentBinding
import com.alvaroquintana.edadperruna.ui.MainActivity
import org.koin.android.scope.lifecycleScope
import org.koin.android.viewmodel.scope.viewModel
import androidx.lifecycle.Observer
import com.alvaroquintana.domain.App
import com.alvaroquintana.edadperruna.ui.components.AspectRatioImageView
import com.alvaroquintana.edadperruna.utils.glideLoadBase64
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds


class ResultFragment : Fragment() {
    private lateinit var binding: ResultFragmentBinding
    private val resultViewModel: ResultViewModel by lifecycleScope.viewModel(this)

    private val dogYears by lazy { arguments?.let { ResultFragmentArgs.fromBundle(it).years } }
    private val dogMonths by lazy { arguments?.let { ResultFragmentArgs.fromBundle(it).months } }
    private val icon by lazy { arguments?.let { ResultFragmentArgs.fromBundle(it).icon } }
    private val name by lazy { arguments?.let { ResultFragmentArgs.fromBundle(it).name } }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = ResultFragmentBinding.inflate(inflater, container, false)
        val root = binding.root

        val btnSubmit: Button = root.findViewById(R.id.btnSubmit)
        btnSubmit.setOnClickListener { resultViewModel.navigateHome() }


        val textTime: TextView = root.findViewById(R.id.textTime)
        when {
            dogYears == 0 -> {
                val m: String = resources.getQuantityString(R.plurals.month, dogMonths!!, dogMonths)
                textTime.text = String.format(resources.getString(R.string.time_month), m)
            }
            dogMonths == 0 -> {
                val y: String = resources.getQuantityString(R.plurals.year, dogYears!!, dogYears)
                textTime.text = String.format(resources.getString(R.string.time_year), y)
            }
            else -> {
                val m: String = resources.getQuantityString(R.plurals.month, dogMonths!!, dogMonths)
                val y: String = resources.getQuantityString(R.plurals.year, dogYears!!, dogYears)
                textTime.text = String.format(resources.getString(R.string.time_year_month), y, m)
            }
        }

        val imageBreed: AspectRatioImageView = root.findViewById(R.id.imageBreed)
        glideLoadBase64((activity as MainActivity), icon, imageBreed)

        val textBreed: TextView = root.findViewById(R.id.textBreed)
        textBreed.text = name

        // RESULT
        val textResult: TextView = root.findViewById(R.id.textResult)
        val result = resultViewModel.translateToHuman(dogYears!!, dogMonths!!)
        when {
            result[0] == 0 -> {
                val m: String = resources.getQuantityString(R.plurals.month, result[1], result[1])
                textResult.text = String.format(resources.getString(R.string.time_month), m)
            }
            result[1] == 0 -> {
                val y: String = resources.getQuantityString(R.plurals.year, result[0], result[0])
                textResult.text = String.format(resources.getString(R.string.time_year), y)
            }
            else -> {
                val m: String = resources.getQuantityString(R.plurals.month, result[1], result[1])
                val y: String = resources.getQuantityString(R.plurals.year, result[0], result[0])
                textResult.text = String.format(resources.getString(R.string.time_year_month), y, m)
            }
        }

        // RESULT CATEGORY
        val textResultCategory: TextView = root.findViewById(R.id.textResultCategory)
        val totalMonths = dogYears!! * 12 + dogMonths!!

        when {
            totalMonths < 4 -> textResultCategory.text = resources.getStringArray(R.array.breed_step)[0] // cachorro: 0 a 3 meses
            totalMonths < 13 -> textResultCategory.text = resources.getStringArray(R.array.breed_step)[1] // juvenil: 3 meses a 12 meses
            totalMonths < 84 -> textResultCategory.text = resources.getStringArray(R.array.breed_step)[2] // adulta: 12 meses a 7 años (84 meses)
            else -> textResultCategory.text = resources.getStringArray(R.array.breed_step)[3] // senior: 7 años o mas (+84 meses)
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MainActivity).setupToolbar(getString(R.string.completed), true) { resultViewModel.navigateHome() }

        resultViewModel.navigation.observe(viewLifecycleOwner, Observer(::navigate))
        resultViewModel.progress.observe(viewLifecycleOwner, Observer(::progressVisibility))
        resultViewModel.list.observe(viewLifecycleOwner, Observer(::fillAppList))
    }

    private fun fillAppList(appList: MutableList<App>) {
        binding.otherAppText.visibility = View.VISIBLE
        binding.recyclerviewOtherApps.adapter = AppListAdapter(
            activity as MainActivity,
            appList,
            resultViewModel::onAppClicked
        )
    }

    private fun navigate(navigation: ResultViewModel.Navigation?) {
        (activity as MainActivity).apply {
            when (navigation) {
                ResultViewModel.Navigation.Home -> {
                    val action = ResultFragmentDirections.actionNavigationResultToHome("","")
                    findNavController().navigate(action)
                }
                is ResultViewModel.Navigation.Open -> {
                    openAppOnPlayStore(navigation.url)
                }
            }
        }
    }

    private fun progressVisibility(isVisible: Boolean) {
        binding.imagenLoading.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    private fun loadAd(mAdView: AdView) {
        MobileAds.initialize(activity as MainActivity)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)
    }

    private fun openAppOnPlayStore(appPackageName: String) {
        try {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$appPackageName")))
        } catch (notFoundException: ActivityNotFoundException) {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")))
        }
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
            resultViewModel.navigateHome()
        }
    }
}