package io.tux.wallet.testnet.model.wallet

import java.io.Serializable


data class CoinAddressItem(
        val adaCoinAddressDetails: List<AdaCoinAddressDetail>,
        val adaMnemonic: String,
        val adaPhrase: String,
        val adaWalletId: String,
        val address: String,
        val coin: String,
        val contractId: String,
        val deviceId: String,
        val id: Int,
        val isAdaTypeCoin: String,
        val privateKey: String,
        val secretKey: String,
        var userDetailsId: Int
    ):Serializable {
        data class AdaCoinAddressDetail(
            val adaAddress: String,
            val coinAddressDetailsId: Int,
            val id: Int
        ):Serializable
    }
