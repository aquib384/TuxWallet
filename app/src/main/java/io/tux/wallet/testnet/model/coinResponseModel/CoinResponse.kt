package io.tux.wallet.testnet.model.coinResponseModel

import java.io.Serializable

data class CoinResponse(
    val code: Int,
    val `data`: CoinData,
    val message: String
):Serializable{}

data class CoinData(
    val display: List<Display>,
    val raw: List<Raw>
):Serializable{}
data class Display(
    val coin: String,
    val coinFullName: Any,
    val coinStatus: String,
    val currencies: List<DisplayCurrency>,
    val stakeStatus: String
):Serializable{}

data class DisplayCurrency(
    val currency: String,
    val `data`: CoinDataItem
):Serializable{}

data class Raw(
    val coin: String,
    val currencies: List<RawCurrency>
):Serializable{}
data class RawCurrency(
    val currency: String,
    val `data`: CoinDataItem
):Serializable{}