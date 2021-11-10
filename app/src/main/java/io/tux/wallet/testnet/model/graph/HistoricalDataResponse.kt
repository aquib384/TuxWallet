package io.tux.wallet.testnet.model.graph

data class HistoricalDataResponse(
    val code: Int,
    val `data`: HistoricalDataItemX,
    val message: String
)