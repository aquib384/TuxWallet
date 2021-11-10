package io.tux.wallet.testnet.utils

import android.util.Base64.*
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import java.security.SignatureException
import java.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

object HmacEncryption {
    private const val HMAC_SHA512 = "HmacSHA512"

    private fun toHexString(bytes: ByteArray): String {
        val formatter = Formatter()
        for (b in bytes) {
            formatter.format("%02x", b)
        }
        return formatter.toString()
    }

    @Throws(SignatureException::class, NoSuchAlgorithmException::class, InvalidKeyException::class)
    fun calculateHMAC(data: String, key: String): String {
        val secretKeySpec = SecretKeySpec(key.toByteArray(), HMAC_SHA512)
        val mac: Mac = Mac.getInstance(HMAC_SHA512)
        mac.init(secretKeySpec)
        return toHexString(mac.doFinal(data.toByteArray()))
    }

    @Throws(Exception::class)
    @JvmStatic

    fun getHmacEncryptedKey(data: String) = convertIntoBase64(calculateHMAC(data, Constants.HMAC_SECRET))

    private fun convertIntoBase64(input: String): String {
        val bytes = input.chunked(2).map { it.toInt(16).toByte() }.toByteArray()
        return encodeToString(bytes, NO_WRAP)
    }
}