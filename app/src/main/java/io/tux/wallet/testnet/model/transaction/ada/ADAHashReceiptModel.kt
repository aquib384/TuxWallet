package io.tux.wallet.testnet.model.transaction.ada

data class ADAHashReceiptModel(
    val amount: Amount,
    val collateral: List<Any>,
    val deposit: Deposit,
    val depth: Depth,
    val direction: String,
    val fee: Fee,
    val id: String,
    val inputs: List<Input>,
    val inserted_at: InsertedAt,
    val metadata: Any?,
    val mint: List<Any>,
    val outputs: List<Output>,
    val status: String,
    val withdrawals: List<Any>
) {
    data class Amount(
        val quantity: Long,
        val unit: String
    )

    data class Deposit(
        val quantity: Long,
        val unit: String
    )

    data class Depth(
        val quantity: Long,
        val unit: String
    )

    data class Fee(
        val quantity: Long,
        val unit: String
    )

    data class Input(
        val address: String?,
        val amount: Amount,
        val assets: List<Any>,
        val id: String,
        val index: Long
    ) {
        data class Amount(
            val quantity: Long,
            val unit: String
        )
    }

    data class InsertedAt(
        val absolute_slot_number: Long,
        val epoch_number: Long,
        val height: Height,
        val slot_number: Long,
        val time: String
    ) {
        data class Height(
            val quantity: Long,
            val unit: String
        )
    }

    data class Output(
        val address: String,
        val amount: Amount,
        val assets: List<Any>
    ) {
        data class Amount(
            val quantity: Long,
            val unit: String
        )
    }
}