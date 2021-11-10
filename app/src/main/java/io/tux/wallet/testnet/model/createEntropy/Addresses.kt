package io.tux.wallet.testnet.model.createEntropy

data class Addresses(
   val p2pkh: String,
   val p2sh: String,
   val p2wpkh: String,
   val p2wpkh_in_p2sh: String?,
   val p2wsh: String,
   val p2wsh_in_p2sh: String,
)
