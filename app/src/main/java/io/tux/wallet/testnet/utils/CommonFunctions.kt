package io.tux.wallet.testnet.utils

import io.tux.wallet.testnet.model.createEntropy.EntropyResponse
import java.text.NumberFormat
import java.util.*

object CommonFunctions {
    fun setEntropyData(data: EntropyResponse, sharedPref: SharedPref) {
        data.apply {

            addresses_bch?.p2wpkh_in_p2sh?.let { sharedPref.setBchAddress(it) }
            wif_bch?.let {  it1 -> sharedPref.setBCHWIF(it1)}
            addresses_ltc?.p2wpkh_in_p2sh?.let { sharedPref.setLtcAddress(it) }
            wif_ltc?.let { sharedPref.setLTCWIF(it) }
            entropy?.let { it1 -> sharedPref.setEntropy(it1) }
            addresses?.p2wpkh_in_p2sh?.let { it1 -> sharedPref.setBtcAddress(it1) }
            ethereumaddress?.let { it1 -> sharedPref.setEthAddress(it1) }
            ethereum_private_key?.let { it1 -> sharedPref.setEthKey(it1) }
            wif?.let { it1 -> sharedPref.setWIF(it1) }
        }
    }

    fun String.convertIntoBalance(): String {
        val format = NumberFormat.getNumberInstance(Locale.US)
        return if (this.contains(".")) {
            val split = this.split(".")
            val before = split[0]
            val after = split[1]
            format.format(before.toInt()) + "." + after
        } else format.format(this.toInt())
    }
}