package io.tux.wallet.testnet.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
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
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import io.tux.wallet.testnet.R
import io.tux.wallet.testnet.activity.GuestModeActivity
import io.tux.wallet.testnet.adapter.CoinPerformanceAdapter
import io.tux.wallet.testnet.adapter.SendReceiveTxnAdapter
import io.tux.wallet.testnet.adapter.TopPairsAdapter
import io.tux.wallet.testnet.adapter.TxnCallBack
import io.tux.wallet.testnet.adapter.viewpagers.TrxHistoryViewPagerAdapter
import io.tux.wallet.testnet.databinding.FragmentCoinDetailsBinding
import io.tux.wallet.testnet.graphUtils.MyMarkerView
import io.tux.wallet.testnet.graphUtils.MyXAxisDayFormatter
import io.tux.wallet.testnet.graphUtils.MyXAxisHourFormatter
import io.tux.wallet.testnet.graphUtils.MyXAxisMinFormatter
import io.tux.wallet.testnet.model.CoinPerformanceModel
import io.tux.wallet.testnet.model.coinResponseModel.CoinDataItem
import io.tux.wallet.testnet.model.coinResponseModel.Display
import io.tux.wallet.testnet.model.coins.CoinListModel
import io.tux.wallet.testnet.model.graph.CoinPriceHistorical
import io.tux.wallet.testnet.model.topPairResponseModel.TopPairDataItem
import io.tux.wallet.testnet.utils.Constants.AGGREGATE
import io.tux.wallet.testnet.utils.Constants.COIN
import io.tux.wallet.testnet.utils.Constants.EXTRA_PARAM
import io.tux.wallet.testnet.utils.Constants.FSYM
import io.tux.wallet.testnet.utils.Constants.LIMIT
import io.tux.wallet.testnet.utils.Constants.TIMESTAMP
import io.tux.wallet.testnet.utils.Constants.TSYM
import io.tux.wallet.testnet.utils.NetworkManager
import io.tux.wallet.testnet.utils.SharedPref
import io.tux.wallet.testnet.utils.Utils
import io.tux.wallet.testnet.viewModels.CoinMarketViewModel
import javax.inject.Inject


@AndroidEntryPoint
class CoinDetailsFragment : Fragment(), View.OnClickListener, OnChartValueSelectedListener,
    TxnCallBack {
    lateinit var binding: FragmentCoinDetailsBinding
    private var pairList = mutableListOf<TopPairDataItem>()
    private var dayList: ArrayList<CoinPriceHistorical>? = arrayListOf()
    private var minList = mutableListOf<CoinPriceHistorical>()
    private var hourList = mutableListOf<CoinPriceHistorical>()
    private var performanceList = ArrayList<CoinPerformanceModel>()
    private lateinit var pairsAdapter: TopPairsAdapter
    private lateinit var txnAdapter: SendReceiveTxnAdapter
    private lateinit var coinPerformanceAdapter: CoinPerformanceAdapter
    private var coin: CoinListModel? = null
    private var displayItem: CoinDataItem? = null
    private var rawItem: CoinDataItem? = null
    private var display: Display? = null
    private val viewModel: CoinMarketViewModel by activityViewModels()
    private lateinit var lineChart: LineChart
    private lateinit var xAxis: XAxis
    private lateinit var yAxis: YAxis


    @Inject
    lateinit var sharedPref: SharedPref


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentCoinDetailsBinding.inflate(layoutInflater)
        coin = arguments?.get(COIN) as CoinListModel

        initViews()
        if (NetworkManager.isConnected(binding.root, requireContext())) {
//            binding.progressBar.visibility = VISIBLE
            callChartApis()
            setPerformance()
            dayHistoryApi()
        }
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    private fun initViews() {
        display = coin?.display
        rawItem = coin?.raw?.currencies?.get(0)?.data
        displayItem = coin?.display?.currencies?.get(0)?.data
        Log.e("display_detailsss", display.toString())


        try {
            if (!coin?.isToken!!) {
                val ic =
                    context?.resources?.getString(R.string.ic_prefix) + coin?.symbol?.lowercase()
                binding.ivIcon.setImageResource(Utils.getDrawableRes(requireContext(), ic))
            } else {
                Glide.with(requireContext()).load(coin?.img).into(binding.ivIcon)
            }
            binding.tvTitle.text = display?.coin + "(" + display?.coinFullName + ")"
            if (displayItem?.CHANGEPCTDAY.toString().startsWith("-")) {
                binding.tvChange.text = displayItem?.CHANGEPCTDAY.toString() + "%"
                binding.tvChange.setTextColor(resources.getColor(R.color.red, resources.newTheme()))
            } else {
                binding.tvChange.text = "+" + displayItem?.CHANGEPCTDAY.toString() + "%"
                binding.tvChange.setTextColor(
                    resources.getColor(
                        R.color.green,
                        resources.newTheme()
                    )
                )
            }
            binding.tvNewPrice.text = displayItem?.PRICE.toString()

            binding.tv24vol.text =
                resources.getString(R.string.vol) + " " + displayItem?.TOTALVOLUME24H.toString()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        if (sharedPref.isLogin()) {
            binding.cardOrder.visibility = VISIBLE
        } else {
            binding.cardOrder.visibility = GONE
        }


        binding.ivBack.setOnClickListener(this)
        binding.btnSend.setOnClickListener(this)
        binding.btnReceive.setOnClickListener(this)
        binding.btnDay.setOnClickListener(this)
        binding.btnHour.setOnClickListener(this)
        binding.btnMin.setOnClickListener(this)


        val adapter = coin?.let { TrxHistoryViewPagerAdapter(activity, it) }
        binding.viewpager.adapter = adapter


        TabLayoutMediator(binding.tabLayout, binding.viewpager) { tab, position ->

            when (position) {
                0 -> tab.text = resources.getString(R.string.send)
                1 -> tab.text = resources.getString(R.string.receive)
//                0-> tab.text = resources.getString(R.string.send)
            }

        }.attach()

    }

    private fun callChartApis() {
        val map = HashMap<String?, Any?>()
        map[AGGREGATE] = ""
        map[FSYM] = coin?.symbol
        map[EXTRA_PARAM] = ""
        map[LIMIT] = "6"
        map[TSYM] = "USD"
        map[TIMESTAMP] = ""

        viewModel.coinDayData(map)
        viewModel.coinHourData(map)
        viewModel.coinMinuteData(map)
        initializeLineChart()
    }

    override fun onClick(v: View?) {
        var args = Bundle()
        when (v?.id) {
            R.id.iv_back -> findNavController().popBackStack()
            R.id.btn_day -> dayHistoryApi()
            R.id.btn_hour -> hourHistoryApi()
            R.id.btn_min -> minHistoryApi()
            R.id.btn_send -> {
                checkLogin(R.id.nav_send)
//             args.putSerializable(COIN,coin)
//                findNavController().navigate(R.id.nav_send,args)
            }
            R.id.btn_receive -> {
                checkLogin(R.id.nav_receive)
//                args.putSerializable(COIN,coin)
//                findNavController().navigate(R.id.nav_receive,args)
            }

        }
    }
//
//    private fun callPairsApi() {
//
//        viewModel.coinTopPairsRequest(coin.symbol, 6)
//        viewModel.topPairModel.observe(viewLifecycleOwner, {
//            pairList = it.data.Data as ArrayList<TopPairDataItem>
//            Log.e("topPairsList", pairList.toString())
//            pairsAdapter = TopPairsAdapter(requireContext(), pairList)
//            binding.rvPairs.apply {
//                val linearLayoutManager = LinearLayoutManager(context)
//                linearLayoutManager.orientation = VERTICAL
//                layoutManager = linearLayoutManager
//                adapter = pairsAdapter
//            }
//            loadingBar.dismiss()
//        })
//    }

    private fun dayHistoryApi() {
        setButtonTint(binding.btnDay, binding.btnHour, binding.btnMin)
        viewModel.dayDataModel.observe(viewLifecycleOwner, {
            dayList = it.data.Data.Data as ArrayList<CoinPriceHistorical>?
            val dayEntries = ArrayList<Entry>()
            if (dayList?.isNotEmpty() == true)
                for (i in 0 until dayList?.size!!) {
                    dayEntries.add(
                        Entry(
                            dayList?.get(i)?.time?.toFloat()!!,
                            dayList?.get(i)?.high?.toFloat()!!
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
                displayItem?.HIGH24HOUR.toString()
            )
        )
        performanceList.add(
            CoinPerformanceModel(
                resources.getString(R.string.tow_low),
                "low",
                displayItem?.LOW24HOUR.toString()
            )
        )
//        performanceList.add(
//            CoinPerformanceModel(
//                resources.getString(R.string.one_high),
//                "high",
//                displayItem?.HIGHHOUR.toString()
//            )
//        )
//        performanceList.add(
//            CoinPerformanceModel(
//                resources.getString(R.string.one_high),
//                "low",
//                displayItem?.LOWHOUR.toString()
//            )
//        )
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
        lineChart.setTouchEnabled(true)
        lineChart.setDrawMarkers(true)
        lineChart.isDragEnabled = false
        lineChart.setScaleEnabled(true)
        lineChart.setPinchZoom(false)

        xAxis = lineChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM

        xAxis.setDrawGridLines(false)
        xAxis.setDrawLabels(true)
        xAxis.disableGridDashedLine()
        yAxis = lineChart.axisLeft

        yAxis.setDrawLabels(true)
        yAxis.enableGridDashedLine(15f, 2f, 0f)

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
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun setLineChart(entries: ArrayList<Entry>) {
//        lineChart.animateX(1000, Easing.EaseInExpo)
        lineChart.highlightValue(null)
        lineChart.setOnChartValueSelectedListener(this)
        val markerView = MyMarkerView(requireContext(), R.layout.marker_view)
        lineChart.marker = markerView
        markerView.clearFocus()
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


    override fun onValueSelected(e: Entry?, h: Highlight?) {
        lineChart.highlightValue(h)
        Log.i("Entry selected", e.toString())
        Log.i(
            "LOW HIGH",
            "low: " + lineChart.lowestVisibleX + ", high: " + lineChart.highestVisibleX
        )
        Log.i(
            "MIN MAX",
            "xMin: " + lineChart.xChartMin + ", xMax: " + lineChart.xChartMax + ", yMin: " + lineChart.yChartMin + ", yMax: " + lineChart.yChartMax
        )

    }

    override fun onNothingSelected() {
        Log.i("Entry selected", "")

    }


    private fun gotToGuestLogin() {
        startActivity(Intent(requireContext(), GuestModeActivity::class.java))
    }

    private fun checkLogin(nav: Int) {
        if (sharedPref.isLogin()) {
            val args = Bundle()
            args.putSerializable(COIN, coin)
            findNavController().navigate(nav, args)
        } else {
            gotToGuestLogin()
        }
    }


}
