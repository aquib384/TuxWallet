package io.tux.wallet.testnet.model.transaction.ada

data class AdaBalanceModel(
    val code: Int,
    val `data`: Data?,
    val message: String
) {
    data class Data(
        val address_pool_gap: Int,
        val assets: Assets,
        val balance: Balance,
        val delegation: Delegation,
        val id: String,
        val name: String,
        val passphrase: Passphrase,
        val state: State,
        val tip: Tip
    ) {
        data class Assets(
            val available: List<Any>,
            val total: List<Any>
        )

        data class Balance(
            val available: Available,
            val reward: Reward,
            val total: Total
        ) {
            data class Available(
                val quantity: Int,
                val unit: String
            )

            data class Reward(
                val quantity: Int,
                val unit: String
            )

            data class Total(
                val quantity: Int?,
                val unit: String
            )
        }

        data class Delegation(
            val active: Active,
            val next: List<Any>
        ) {
            data class Active(
                val status: String
            )
        }

        data class Passphrase(
            val last_updated_at: String
        )

        data class State(
            val progress: Progress,
            val status: String
        ) {
            data class Progress(
                val quantity: Double,
                val unit: String
            )
        }

        data class Tip(
            val absolute_slot_number: Int,
            val epoch_number: Int,
            val height: Height,
            val slot_number: Int,
            val time: String
        ) {
            data class Height(
                val quantity: Int,
                val unit: String
            )
        }
    }
}