package io.tux.wallet.testnet.graphUtils

import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.data.Entry

import com.github.mikephil.charting.utils.ViewPortHandler

import com.github.mikephil.charting.formatter.IAxisValueFormatter

import com.github.mikephil.charting.formatter.IValueFormatter
import java.text.DecimalFormat


    class LargeValuesFormatter() : IValueFormatter, IAxisValueFormatter {
        var regex ="[0-9]+\\.[a-z]"
        private var mSuffix = arrayOf(
            "", "k", "m", "b", "t"
        )
        private var mMaxLength = 5
        private val mFormat: DecimalFormat
        private var mText = ""

        /**
         * Creates a formatter that appends a specified text to the result string
         *
         * @param appendix a text that will be appended
         */
        constructor(appendix: String) : this() {
            mText = appendix
        }

        // IValueFormatter
        fun getFormattedValue(
            value: Float,
            entry: Map.Entry<*, *>?,
            dataSetIndex: Int,
            viewPortHandler: ViewPortHandler?
        ): String {
            return makePretty(value.toDouble()) + mText
        }

        // IAxisValueFormatter
        override fun getFormattedValue(value: Float, axis: AxisBase): String {
            return makePretty(value.toDouble()) + mText
        }

        /**
         * Set an appendix text to be added at the end of the formatted value.
         *
         * @param appendix
         */
        fun setAppendix(appendix: String) {
            mText = appendix
        }

        /**
         * Set custom suffix to be appended after the values.
         * Default suffix: ["", "k", "m", "b", "t"]
         *
         * @param suffix new suffix
         */
        fun setSuffix(suffix: Array<String>) {
            mSuffix = suffix
        }

        fun setMaxLength(maxLength: Int) {
            mMaxLength = maxLength
        }

        /**
         * Formats each number properly. Special thanks to Roman Gromov
         * (https://github.com/romangromov) for this piece of code.
         */
        private fun makePretty(number: Double): String {
            var r: String = mFormat.format(number)
            val numericValue1 = Character.getNumericValue(r[r.length - 1])
            val numericValue2 = Character.getNumericValue(r[r.length - 2])
            val combined = Integer.valueOf(numericValue2.toString() + "" + numericValue1)
            r = r.replace("E[0-9][0-9]".toRegex(), mSuffix[combined / 3])
            while (r.length > mMaxLength || r.matches(regex.toRegex())) {
                r = r.substring(0, r.length - 2) + r.substring(r.length - 1)
            }
            return r
        }

        val decimalDigits: Int
            get() = 0

        init {
            mFormat = DecimalFormat("###E00")
        }

        override fun getFormattedValue(
            value: Float,
            entry: Entry?,
            dataSetIndex: Int,
            viewPortHandler: ViewPortHandler?
        ): String {
            TODO("Not yet implemented")
        }
    }
