package io.tux.wallet.testnet.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import com.bumptech.glide.Glide
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.LargeValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.google.android.material.tabs.TabLayout
import io.tux.wallet.testnet.R
import io.tux.wallet.testnet.adapter.CoinPerformanceAdapter
import io.tux.wallet.testnet.adapter.OrderHistoryAdapter
import io.tux.wallet.testnet.databinding.FragmentMarketDetailBinding
import io.tux.wallet.testnet.model.CoinPerformanceModel
import io.tux.wallet.testnet.model.OrderHistoryModel
import io.tux.wallet.testnet.model.coins.CoinListModel
import io.tux.wallet.testnet.model.exchangeResponse.ExchangesItem
import io.tux.wallet.testnet.model.graph.CoinPriceHistorical
import io.tux.wallet.testnet.utils.Constants
import io.tux.wallet.testnet.utils.Constants.PAIR
import io.tux.wallet.testnet.utils.SharedPref

import io.tux.wallet.testnet.utils.Utils
import io.tux.wallet.testnet.viewModels.CoinMarketViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.tux.wallet.testnet.activity.GuestModeActivity
import io.tux.wallet.testnet.graphUtils.*
import io.tux.wallet.testnet.model.coins.MarketPairModel
import io.tux.wallet.testnet.utils.NetworkManager
import javax.inject.Inject

@AndroidEntryPoint
class MarketDetailFragment : Fragment(), View.OnClickListener {
lateinit var binding : FragmentMarketDetailBinding
    private var orderList= ArrayList<OrderHistoryModel>()
    private var dayList = mutableListOf<CoinPriceHistorical>()
    private var minList = mutableListOf<CoinPriceHistorical>()
    private var hourList = mutableListOf<CoinPriceHistorical>()
    private var performanceList = ArrayList<CoinPerformanceModel>()
    private lateinit var orderHistoryAdapter: OrderHistoryAdapter
   private lateinit var coinPerformanceAdapter: CoinPerformanceAdapter
    lateinit var pair : MarketPairModel
    var coinList = mutableListOf<CoinListModel>()
    private val viewModel: CoinMarketViewModel by viewModels()

    private lateinit var lineChart: LineChart
    private lateinit var xAxis: XAxis
    private lateinit var yAxis: YAxis
    @Inject
    lateinit var sharedPref: SharedPref
    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentMarketDetailBinding.inflate(layoutInflater)
        pair = arguments?.get(PAIR) as MarketPairModel

        initViews()
        if(NetworkManager.isConnected(binding.root,requireContext())) {
            binding.progressBar.visibility = VISIBLE
            callChartApis()
            setPerformance()
            dayHistoryApi()
            setOrderAdapter()
        }

        return binding.root
    }

    private fun initViews()
    {
        binding.ivFav.isVisible = sharedPref.isLogin()
        try {

            for( i in 0 until viewModel.coinList.size) {
                if(coinList[i].symbol.equals(pair.raw.currencies[0].data.FROMSYMBOL.toString(),true)) {
                    if (!coinList[i].isToken) {
                        val ic =
                            context?.resources?.getString(R.string.ic_prefix) + coinList[i].symbol.lowercase()
                        binding.ivIcon.setImageResource(Utils.getDrawableRes(requireContext(), ic))
                    } else {
                        Glide.with(requireContext()).load(coinList[i]?.img).into(binding.ivIcon)
                    }
                }
            }
//            val icon = resources.getString(R.string.ic_prefix) + pair.raw.currencies[0].data.FROMSYMBOL.toString().lowercase()
//            binding.ivIcon.setImageResource(Utils.getDrawableRes(requireContext(),icon))

            binding.tvTitle.text = pair.raw.currencies[0].data.FROMSYMBOL.toString() + "/" + pair.raw.currencies[0].data.TOSYMBOL.toString()

            if (pair.raw.currencies[0].data.CHANGEPCT24HOUR.toString().startsWith("-")) {
                binding.tvChange.text = Utils.getDoubleValueFromString(pair.raw.currencies[0].data.CHANGEPCT24HOUR.toString()) + "%"
                binding.tvChange.setTextColor(resources.getColor(R.color.red, resources.newTheme()))
            } else {
                binding.tvChange.text = "+" +Utils.getDoubleValueFromString( pair.raw.currencies[0].data.CHANGEPCT24HOUR.toString()) + "%"
                binding.tvChange.setTextColor(
                    resources.getColor(
                        R.color.green,
                        resources.newTheme()
                    )
                )
            }
            binding.tvNewPrice.text = Utils.getDoubleValueFromString(pair.raw.currencies[0].data.PRICE.toString())
            binding.tv24vol.text = resources.getString(R.string.vol)+" "+pair.display.currencies[0].data.VOLUME24HOUR
//                    Utils.getDoubleValueFromString(pair.raw.currencies[0].data.VOLUME24HOUR.toString())


        } catch (e: Exception) {
            e.printStackTrace()
        }

        binding.ivBack.setOnClickListener(this)
        binding.ivFav.setOnClickListener(this)
        binding.btnBuy.setOnClickListener(this)
        binding.btnSell.setOnClickListener(this)
        binding.btnDay.setOnClickListener(this)
        binding.btnHour.setOnClickListener(this)
        binding.btnMin.setOnClickListener(this)
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener
        {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when(tab?.text.toString())
                {
                    resources.getString(R.string.buy_order)->Utils.showComingSoonDialog(requireContext())
                    resources.getString(R.string.sell_order)->Utils.showComingSoonDialog(requireContext())
                    resources.getString(R.string.track_order)->Utils.showComingSoonDialog(requireContext())

                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                return
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                return
            }

        })


    }

    private fun callChartApis()
    {
        val map = HashMap<String?, Any?>()
        map[Constants.AGGREGATE] = ""
        map[Constants.FSYM] = pair.raw.currencies[0].data.FROMSYMBOL.toString()
        map[Constants.EXTRA_PARAM] = ""
        map[Constants.LIMIT] = "6"
        map[Constants.TSYM] = pair.raw.currencies[0].data.TOSYMBOL.toString()
        map[Constants.TIMESTAMP] = ""

        viewModel.coinDayData(map)
        viewModel.coinHourData(map)
        viewModel.coinMinuteData(map)
        initializeLineChart()
    }

    private fun setOrderAdapter()
    {
        if (orderList.isEmpty())
        {
            binding.tvNoData.visibility = VISIBLE
        }
        else {
            orderHistoryAdapter = OrderHistoryAdapter(requireContext(), orderList)
            binding.rvOrders.apply {
                val linearLayoutManager = LinearLayoutManager(context)
                linearLayoutManager.orientation = VERTICAL
                layoutManager = linearLayoutManager
                adapter = orderHistoryAdapter
            }
            binding.tvNoData.visibility = GONE
        }
    }

    override fun onClick(v: View?) {
        when(v?.id)
        {
            R.id.iv_back -> findNavController().popBackStack()
            R.id.btn_buy -> checkLogin()
            R.id.btn_sell -> checkLogin()
            R.id.btn_day -> dayHistoryApi()
            R.id.btn_hour -> hourHistoryApi()
            R.id.btn_min -> minHistoryApi()
            R.id.iv_fav ->{
                if (sharedPref.isLogin())
                {}
                else{
                   gotToGuestLogin()
                }
            }
        }
    }


    private fun dayHistoryApi() {
        setButtonTint(binding.btnDay, binding.btnHour, binding.btnMin)
        viewModel.dayDataModel.observe(viewLifecycleOwner, {
            dayList = it.data.Data.Data as ArrayList<CoinPriceHistorical>
            Log.e("dayList", dayList.toString())
            val dayEntries = ArrayList<Entry>()
            for (i in 0 until dayList.size) {
                dayEntries.add(
                    Entry(
                        dayList[i].time.toFloat(),
                        dayList[i].open.toFloat(),
                        dayList[i].high.toFloat()
                    )
                )

            }
            xAxis.valueFormatter = MyXAxisDayFormatter()
            lineChart.description.text = resources.getString(R.string.one_day)
            setLineChart(dayEntries)

        })

    }

    private fun hourHistoryApi() {
        setButtonTint(binding.btnHour, binding.btnDay, binding.btnMin)
        viewModel.hourDataModel.observe(viewLifecycleOwner, {
            hourList = it.data.Data.Data as ArrayList<CoinPriceHistorical>
            Log.e("hourList", hourList.toString())
            val hourEntries = ArrayList<Entry>()
            for (i in 0 until hourList.size) {
                hourEntries.add(
                    Entry(
                        hourList[i].time.toFloat(),
                        hourList[i].open.toFloat(),
                        hourList[i].high.toFloat()
                    )
                )

            }
            xAxis.valueFormatter = MyXAxisHourFormatter()
            lineChart.description.text = resources.getString(R.string.one_hour)
            setLineChart(hourEntries)

        })

    }

    private fun minHistoryApi() {
        setButtonTint(binding.btnMin, binding.btnHour, binding.btnDay)
        viewModel.minuteDataModel.observe(viewLifecycleOwner, {
            minList = it.data.Data.Data as ArrayList<CoinPriceHistorical>
            Log.e("minList", minList.toString())
            val minEntries = ArrayList<Entry>()
            for (i in 0 until minList.size) {
                minEntries.add(
                    Entry(
                        minList[i].time.toFloat(),
                        minList[i].open.toFloat(),
                        minList[i].high.toFloat()
                    )
                )

            }

            xAxis.valueFormatter = MyXAxisMinFormatter()
            lineChart.description.text = resources.getString(R.string.one_minute)
            setLineChart(minEntries)

        })

    }


    private fun setPerformance() {
        performanceList.clear()
        performanceList.add(
            CoinPerformanceModel(
                resources.getString(R.string.tow_high),
                "high",
                pair.raw.currencies[0].data.HIGH24HOUR.toString()
            )
        )
        performanceList.add(
            CoinPerformanceModel(
                resources.getString(R.string.tow_low),
                "low",
                pair.raw.currencies[0].data.LOW24HOUR.toString()
            )
        )
//        performanceList.add(
//            CoinPerformanceModel(
//                resources.getString(R.string.one_high),
//                "high",
//                pair.raw.currencies[0].data.HIGHHOUR.toString()
//            )
//        )
//        performanceList.add(CoinPerformanceModel(resources.getString(R.string.one_high), "low", pair.raw.currencies[0].data.LOWHOUR.toString()))
        coinPerformanceAdapter = CoinPerformanceAdapter(requireContext(), performanceList)
        binding.rvRanking.apply {
            val linearLayoutManager = LinearLayoutManager(context)
            linearLayoutManager.orientation = VERTICAL
            layoutManager = linearLayoutManager
            adapter = coinPerformanceAdapter
        }
    }

    private fun initializeLineChart() {

        lineChart = binding.chart
        lineChart.axisRight.isEnabled = false
        lineChart.xAxis.isEnabled = true
        lineChart.axisLeft.isEnabled = true
        lineChart.setDrawGridBackground(false)
        lineChart.setDragEnabled(false)
        lineChart.setScaleEnabled(true)
        lineChart.setPinchZoom(false)
        lineChart.setDrawMarkers(true)
//        val markerView = CustomMarker(requireContext(), R.layout.marker_view)
        val markerView = MyMarkerView(requireContext(), R.layout.marker_view)
        lineChart.marker = markerView
        markerView.clearFocus()
        xAxis = lineChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM

        xAxis.setDrawGridLines(false)
        xAxis.setDrawLabels(true)
        xAxis.disableGridDashedLine()
        yAxis = lineChart.axisLeft

        yAxis.setDrawLabels(true)
        yAxis.enableGridDashedLine(15f,2f,0f)

        when (sharedPref.isLightTheme()) {
            true -> {
                xAxis.textColor = Color.BLACK
                xAxis.textSize = 12f
                lineChart.description.textColor = Color.BLACK
                yAxis.textColor = Color.BLACK
                yAxis.textSize = 12f

            }

            else -> {
                xAxis.textColor = Color.WHITE
                xAxis.textSize = 12f
                lineChart.description.textColor = Color.WHITE
                yAxis.textColor = Color.WHITE
                yAxis.textSize = 12f
            }

        }
        lineChart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {


            override fun onValueSelected(p0: Entry?, p1: Highlight?) {
//                Log.e("highlightValue", p1.toString() + "\n"+ p0.toString())
                markerView.refreshContent(p0, p1)
                lineChart.setMarker(markerView)

            }

            override fun onNothingSelected() {
//                lineDataSet.setCircleColor(Color.rgb(26, 115, 197))
                return
            }
        })
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun setLineChart(entries: ArrayList<Entry>) {
//        lineChart.animateX(1000, Easing.EaseInExpo)
        yAxis.valueFormatter = LargeValueFormatter()
        val vl = LineDataSet(entries, "")
        vl.setDrawValues(false)
        vl.setDrawFilled(true)
        vl.lineWidth = 3f
        vl.valueTextColor = R.attr.textColor

        vl.fillDrawable = resources.getDrawable(R.drawable.graph_gradient_selector, null)
        vl.mode = LineDataSet.Mode.CUBIC_BEZIER
        lineChart.data = LineData(vl)
        vl.setDrawHorizontalHighlightIndicator(true)
        vl.setDrawVerticalHighlightIndicator(true)

        lineChart.invalidate()
        binding.progressBar.visibility = GONE

    }


    private fun setButtonTint(b1: TextView, b2: TextView, b3: TextView) {

        b1.setBackgroundResource(R.drawable.button_small_gradient)
        b1.setTextColor(resources.getColor(R.color.white, resources.newTheme()))
        b2.setBackgroundResource(R.drawable.button_small_color)
        b3.setBackgroundResource(R.drawable.button_small_color)
        if (sharedPref.isLightTheme()) {
            b2.setTextColor(resources.getColor(R.color.black, resources.newTheme()))
            b3.setTextColor(resources.getColor(R.color.black, resources.newTheme()))
        } else {
            b2.setTextColor(resources.getColor(R.color.white, resources.newTheme()))
            b3.setTextColor(resources.getColor(R.color.white, resources.newTheme()))
        }
    }




    private fun gotToGuestLogin()
    {
        startActivity(Intent(requireContext() , GuestModeActivity::class.java))
    }

    private fun checkLogin()
    {
        if (sharedPref.isLogin()) {
            Utils.showComingSoonDialog(requireContext())
        }
        else
        {
            gotToGuestLogin()
        }
    }


}
