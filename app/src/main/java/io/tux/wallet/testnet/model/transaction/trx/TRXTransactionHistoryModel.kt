package io.tux.wallet.testnet.model.transaction.trx

data class TRXTransactionHistoryModel(
    val code: Int,
    val `data`: List<Data1>,
    val message: String
) {
    data class Data1(
        val contractInfo: ContractInfo,
        val contractMap: Any,
        val `data`: List<Data2>,
        val rangeTotal: Int,
        val total: Int,
        val wholeChainTxCount: Long
    ) {
        class ContractInfo

        data class Data2(
            val Events: String,
            val SmartCalls: String,
            val amount: String,
            val block: Int,
            val confirmed: Boolean,
            val contractData: ContractData,
            val contractRet: String,
            val contractType: Int,
            val cost: Cost,
            val `data`: String,
            val fee: String,
            val hash: String,
            val id: String,
            val ownerAddress: String,
            val result: String,
            val revert: Boolean,
            val timestamp: Long,
            val toAddress: String,
            val toAddressList: List<String>,
            val tokenInfo: TokenInfo,
            val tokenType: String
        ) {
            data class ContractData(
                val amount: Int,
                val owner_address: String,
                val to_address: String
            )

            data class Cost(
                val energy_fee: Int,
                val energy_usage: Int,
                val energy_usage_total: Int,
                val fee: Int,
                val net_fee: Int,
                val net_usage: Int,
                val origin_energy_usage: Int
            )

            data class TokenInfo(
                val tokenAbbr: String,
                val tokenCanShow: Int,
                val tokenDecimal: Int,
                val tokenId: String,
                val tokenLevel: String,
                val tokenLogo: String,
                val tokenName: String,
                val tokenType: String,
                val vip: Boolean
            )
        }
    }
}