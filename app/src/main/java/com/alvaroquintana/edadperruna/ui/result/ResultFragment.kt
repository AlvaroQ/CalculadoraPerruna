package com.alvaroquintana.edadperruna.ui.result

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.alvaroquintana.edadperruna.R
import com.alvaroquintana.edadperruna.databinding.ResultFragmentBinding
import com.alvaroquintana.edadperruna.ui.MainActivity
import com.alvaroquintana.edadperruna.utils.expandImage
import com.alvaroquintana.edadperruna.utils.glideLoadBase64
import com.alvaroquintana.edadperruna.utils.glideLoadGif
import com.alvaroquintana.edadperruna.utils.openURL
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import org.koin.android.scope.lifecycleScope
import org.koin.android.viewmodel.scope.viewModel


class ResultFragment : Fragment() {
    private lateinit var binding: ResultFragmentBinding
    private val resultViewModel: ResultViewModel by lifecycleScope.viewModel(this)

    private val dogYears by lazy { arguments?.let { ResultFragmentArgs.fromBundle(it).years } }
    private val dogMonths by lazy { arguments?.let { ResultFragmentArgs.fromBundle(it).months } }
    private val image by lazy { arguments?.let { ResultFragmentArgs.fromBundle(it).image } }
    private val name by lazy { arguments?.let { ResultFragmentArgs.fromBundle(it).name } }
    private val life by lazy { arguments?.let { ResultFragmentArgs.fromBundle(it).life } }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {

        binding = ResultFragmentBinding.inflate(inflater, container, false)
        val root = binding.root

        // Image breed selected
        val imageBreed: ImageView = root.findViewById(R.id.imageBreed)
        glideLoadBase64(requireActivity(), image, imageBreed)
        imageBreed.setOnClickListener { resultViewModel.onDogLongClicked() }

        // Text breed selected
        val textBreed: TextView = root.findViewById(R.id.textBreed)
        textBreed.text = name

        // text average life breed
        val textLifeCategory: TextView = root.findViewById(R.id.textLifeCategory)
        textLifeCategory.text = String.format(resources.getString(R.string.life_resumen), name, life)

        val imageInfo: ImageView = root.findViewById(R.id.imageInfo)
        imageInfo.setOnClickListener { openURL(requireContext(), "https://www.nbcnews.com/health/health-news/how-old-your-dog-new-equation-shows-how-calculate-its-n1233459") }

        val btnSubmit: Button = root.findViewById(R.id.btnSubmit)
        btnSubmit.setOnClickListener { resultViewModel.navigateHome() }

        writeResult(root)
        writeChart(root)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MainActivity).setupToolbar(
            getString(R.string.completed),
            hasSettings = false,
            hasBackButton = true
        )

        resultViewModel.navigation.observe(viewLifecycleOwner, Observer(::navigate))
        resultViewModel.progress.observe(viewLifecycleOwner, Observer(::progressVisibility))
        resultViewModel.showingAds.observe(viewLifecycleOwner, Observer(::loadAd))
    }

    private fun navigate(navigation: ResultViewModel.Navigation?) {
        (activity as MainActivity).apply {
            when (navigation) {
                ResultViewModel.Navigation.Home -> {
                    val action = ResultFragmentDirections.actionNavigationResultToHome("", "")
                    findNavController().navigate(action)
                }
                ResultViewModel.Navigation.Expand -> {
                    expandImage(activity, binding.imageBreed, image!!)
                }
            }
        }
    }

    private fun progressVisibility(isVisible: Boolean) {
        glideLoadGif(activity as MainActivity, binding.imagenLoading)
        binding.imagenLoading.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    private fun loadAd(model: ResultViewModel.UiModel) {
        if (model is ResultViewModel.UiModel.ShowAd && model.show) {
            (activity as MainActivity).showInstersticialAd()
        }
    }

    private fun writeChart(root: View) {
        val reportingChart: LineChart = root.findViewById(R.id.reportingChart)
        reportingChart.setTouchEnabled(false)
        reportingChart.setPinchZoom(false)
        reportingChart.description.isEnabled = false
        reportingChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        reportingChart.animateY(500)
        reportingChart.xAxis.isGranularityEnabled = true
        reportingChart.setDrawMarkers(true)

        val yValues: ArrayList<Entry> = resultViewModel.generateEntries()

        val dataSets: ArrayList<ILineDataSet> = ArrayList()
        val set1 = LineDataSet(yValues, "Edades de $name")
        set1.axisDependency = YAxis.AxisDependency.LEFT
        set1.isHighlightEnabled = true
        set1.color = requireActivity().getColor(R.color.blue)
        set1.setCircleColor(requireActivity().getColor(R.color.colorAccent))
        set1.setDrawCircles(false)
        set1.setDrawHighlightIndicators(true)
        set1.valueTextSize = 0f
        set1.valueTextColor = requireActivity().getColor(R.color.colorPrimaryDark)
        set1.fillAlpha = 0
        set1.lineWidth = 2f
        val index = if(dogYears!! > 22) 22 else dogYears!!
        set1.getEntryForIndex(index).icon = resources.getDrawable(R.drawable.ic_marker)
        dataSets.add(set1)

        val data = LineData(dataSets)
        reportingChart.xAxis.labelCount = set1.entryCount

        val limitLineAdulto = LimitLine(1f)
        val limitLineSenior = LimitLine(7f)
        reportingChart.xAxis.addLimitLine(limitLineAdulto)
        reportingChart.xAxis.addLimitLine(limitLineSenior)

        reportingChart.data = data
    }

    private fun writeResult(root: View) {

        // User input
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

        // RESULT CATEGORY
        val textResultCategory: TextView = root.findViewById(R.id.textResultCategory)
        textResultCategory.text = resources.getStringArray(R.array.breed_step)[when {
            (dogYears!! * 12 + dogMonths!! < 4) -> 0// cachorro: 0 a 3 meses
            (dogYears!! * 12 + dogMonths!! < 13) -> 1 // juvenil: 3 meses a 12 meses
            (dogYears!! * 12 + dogMonths!! < 84) -> 2 // adulta: 12 meses a 7 años (84 meses)
            else -> 3}] // senior: 7 años o mas (>84 meses)

        // RESULT AGE
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
    }
}