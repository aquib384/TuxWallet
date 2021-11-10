package io.tux.wallet.testnet.graphUtils

import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.ValueFormatter
import java.text.SimpleDateFormat
import java.util.*

class MyXAxisMinFormatter : ValueFormatter() {

    override fun getAxisLabel(value: Float, axis: AxisBase?): String {
        return dayFromTimeStamp(value.toLong())
    }

    private fun dayFromTimeStamp(timestamp: Long): String {
        val d: Date = Date(timestamp * 1000L)
        return SimpleDateFormat("mm").format(d)
    }
}