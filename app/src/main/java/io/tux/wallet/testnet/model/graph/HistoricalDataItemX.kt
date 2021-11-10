package io.tux.wallet.testnet.model.graph

data class HistoricalDataItemX(
    val Data: HistoricalDataItemXX,
    val HasWarning: Boolean,
    val Message: String,
    val RateLimit: RateLimit,
    val Response: String,
    val Type: Int
)