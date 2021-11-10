package io.tux.wallet.testnet.model.transaction.trx

data class TRXHashReceiptModel(
    val code: Int,
    val `data`: Data,
    val message: String
) {
    data class Data(
        val block: Int,
        val confirmations: Int,
        val confirmed: Boolean,
        val contractData: ContractData,
        val contractInfo: ContractInfo,
        val contractRet: String,
        val contractType: Int,
        val contract_map: ContractMap,
        val cost: Cost,
        val `data`: String,
        val hash: String,
        val info: Info,
        val internal_transactions: String,
        val ownerAddress: String,
        val revert: Boolean,
        val signature_addresses: List<Any>,
        val srConfirmList: List<SrConfirm>,
        val timestamp: Long,
        val toAddress: String,
        val trigger_info: TriggerInfo
    ) {
        data class ContractData(
            val amount: Int,
            val owner_address: String,
            val to_address: String
        )

        class ContractInfo

        data class ContractMap(
            val TQa5ec7QudhNyb4e9kfYTcdHMeaZhKkbTz: Boolean,
            val TVXiYoJU9Ngrs75C6HrgoVbifVSvc8DRao: Boolean
        )

        data class Cost(
            val account_create_fee: Int,
            val energy_fee: Int,
            val energy_fee_cost: Int,
            val energy_usage: Int,
            val energy_usage_total: Int,
            val fee: Int,
            val multi_sign_fee: Int,
            val net_fee: Int,
            val net_fee_cost: Int,
            val net_usage: Int,
            val origin_energy_usage: Int
        )

        class Info

        data class SrConfirm(
            val address: String,
            val block: Int,
            val name: String,
            val url: String
        )

        class TriggerInfo
    }
}