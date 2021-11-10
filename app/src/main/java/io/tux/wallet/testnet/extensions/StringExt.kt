package io.tux.wallet.testnet.extensions

import java.nio.charset.StandardCharsets
import java.security.MessageDigest


fun String.toSHA256(): String {
    val digest = MessageDigest.getInstance("SHA-256")
    val hash = digest.digest(this.toByteArray(StandardCharsets.UTF_8))
    return hash.pinByteToHex()
}

fun ByteArray.pinByteToHex(): String {
    return JavaUtil.bytesToHex(this)
}
