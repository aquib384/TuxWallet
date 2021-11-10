package io.tux.wallet.testnet.model.topPairResponseModel

import java.io.Serializable

data class TopPairDataItem(
    val exchange: String,
    val exchangeGrade: String,
    val exchangeGradePoints: Int,
    val fromSymbol: String,
    val lastUpdateTs: Int,
    val price: Double,
    val toSymbol: String,
    val volume24h: Double,
    val volume24hTo: Double
):Serializable{}