package io.tux.wallet.testnet.model.topPairResponseModel

import io.tux.wallet.testnet.model.graph.RateLimit
import java.io.Serializable

data class TopPairData(
    val Data: List<TopPairDataItem>,
    val HasWarning: Boolean,
    val RateLimit: RateLimit,
    val Response: String,
    val Type: Int
):Serializable{}