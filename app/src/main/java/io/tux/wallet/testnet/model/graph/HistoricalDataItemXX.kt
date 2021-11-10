package io.tux.wallet.testnet.model.graph

data class HistoricalDataItemXX(
    val Aggregated: Boolean,
    val Data: List<CoinPriceHistorical>,
    val TimeFrom: Int,
    val TimeTo: Int
)