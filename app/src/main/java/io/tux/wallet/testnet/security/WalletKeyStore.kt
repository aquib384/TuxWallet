package io.tux.wallet.testnet.security

import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics
import io.tux.wallet.testnet.utils.BytesUtil
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.IOException
import java.security.*
import java.security.cert.CertificateException
import java.util.*
import java.util.concurrent.locks.ReentrantLock
import javax.crypto.*
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.IvParameterSpec

class WalletKeyStore {
    companion object {
        private val TAG = WalletKeyStore::class.java.name

        private const val KEY_STORE_PREFS_NAME = "keyStorePrefs"

        private const val ANDROID_KEY_STORE = "AndroidKeyStore"
        private const val CIPHER_ALGORITHM = "AES/CBC/PKCS7Padding"
        private const val NEW_CIPHER_ALGORITHM = "AES/GCM/NoPadding"
        private const val NEW_PADDING = KeyProperties.ENCRYPTION_PADDING_NONE
        private const val NEW_BLOCK_MODE = KeyProperties.BLOCK_MODE_GCM

        private const val PHRASE_ALIAS = "phrase"
        private const val ETH_PUBKEY_ALIAS = "ethpubkey"
        private const val AUTH_KEY_ALIAS = "authKey"
        private const val PUB_KEY_ALIAS = "pubKey"

        private const val PHRASE_FILENAME = "my_phrase"
        private const val ETH_PUBKEY_FILENAME = "my_eth_pubkey"
        private const val PUB_KEY_FILENAME = "my_pub_key"
        private const val AUTH_KEY_FILENAME = "my_auth_key"
        private const val PHRASE_IV = "ivphrase"
        private const val ETH_PUBKEY_IV = "ivethpubkey"
        private const val PUB_KEY_IV = "ivpubkey"
        private const val AUTH_KEY_IV = "ivauthkey"

        //bip39
        private const val BIP39_PHRASE_ALISA = "bip39_phrase"
        private const val BIP39_PHRASE_FILENAME = "bip39_phrase_filename"
        private const val BIP39_PHRASE_IV = "bip39_phrase_iv"
        private const val BIP39_PUB_KEY_ALIAS = "BIP39_PUB_KEY_ALIAS"
        private const val BIP39_PUB_KEY_FILENAME = "BIP39_PUB_KEY_FILENAME"
        private const val BIP39_PUB_KEY_IV = "BIP39_PUB_KEY_IV"

        private const val WALLET_CREATION_TIME_ALIAS = "creationTime"
        private const val WALLET_CREATION_TIME_FILENAME = "my_creation_time"
        private const val WALLET_CREATION_TIME_IV = "ivtime"

        private var aliasObjectMap: MutableMap<String, AliasObject> = HashMap()
        private const val AUTH_DURATION_SEC = 300
        private val lock = ReentrantLock()

        init {
            aliasObjectMap[BIP39_PHRASE_ALISA] =
                AliasObject(BIP39_PHRASE_ALISA, BIP39_PHRASE_FILENAME, BIP39_PHRASE_IV)
            aliasObjectMap[BIP39_PUB_KEY_ALIAS] =
                AliasObject(BIP39_PUB_KEY_ALIAS, BIP39_PUB_KEY_FILENAME, BIP39_PUB_KEY_IV)

            aliasObjectMap[PHRASE_ALIAS] = AliasObject(PHRASE_ALIAS, PHRASE_FILENAME, PHRASE_IV)
            aliasObjectMap[PUB_KEY_ALIAS] = AliasObject(PUB_KEY_ALIAS, PUB_KEY_FILENAME, PUB_KEY_IV)
            aliasObjectMap[AUTH_KEY_ALIAS] =
                AliasObject(AUTH_KEY_ALIAS, AUTH_KEY_FILENAME, AUTH_KEY_IV)
            aliasObjectMap[ETH_PUBKEY_ALIAS] =
                AliasObject(ETH_PUBKEY_ALIAS, ETH_PUBKEY_FILENAME, ETH_PUBKEY_IV)
            aliasObjectMap[WALLET_CREATION_TIME_ALIAS] = AliasObject(
                WALLET_CREATION_TIME_ALIAS,
                WALLET_CREATION_TIME_FILENAME,
                WALLET_CREATION_TIME_IV
            )
        }

        @Throws(IllegalArgumentException::class)
        @JvmStatic
        private fun validateGet(alias: String, alias_file: String, alias_iv: String) {
            val obj = aliasObjectMap[alias]
            if (obj != null && (obj.alias != alias || obj.datafileName != alias_file || obj.ivFileName != alias_iv)) {
                val err =
                    alias + "|" + alias_file + "|" + alias_iv + ", obj: " + obj.alias + "|" + obj.datafileName + "|" + obj.ivFileName
                throw IllegalArgumentException("keystore insert inconsistency in names: $err")
            }

        }

        @Throws(IllegalArgumentException::class)
        @JvmStatic
        private fun validateSet(
            data: ByteArray?,
            alias: String,
            alias_file: String,
            alias_iv: String
        ) {
            if (data == null) throw IllegalArgumentException("keystore insert data is null")
            val obj = aliasObjectMap[alias]
            if (obj != null && (obj.alias != alias || obj.datafileName != alias_file || obj.ivFileName != alias_iv)) {
                val err =
                    alias + "|" + alias_file + "|" + alias_iv + ", obj: " + obj.alias + "|" + obj.datafileName + "|" + obj.ivFileName
                throw IllegalArgumentException("keystore insert inconsistency in names: $err")
            }
        }

        @Throws(
            InvalidAlgorithmParameterException::class,
            KeyStoreException::class,
            NoSuchProviderException::class,
            NoSuchAlgorithmException::class,
            InvalidKeyException::class,
        )
        @JvmStatic
        private fun createKeys(alias: String): SecretKey? {
            val keyGenerator =
                KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEY_STORE)

            // Set the alias of the entry in Android KeyStore where the key will appear
            // and the constrains (purposes) in the constructor of the Builder
            keyGenerator.init(
                KeyGenParameterSpec.Builder(
                    alias,
                    KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
                )
                    .setBlockModes(NEW_BLOCK_MODE)
                    //                .setUserAuthenticationRequired(auth_required)
                    .setUserAuthenticationValidityDurationSeconds(AUTH_DURATION_SEC)
                    .setRandomizedEncryptionRequired(false)
                    .setEncryptionPaddings(NEW_PADDING)
                    .build()
            )
            return keyGenerator.generateKey()
        }

        @Synchronized
        @JvmStatic
        private fun setData(
            context: Context,
            data: ByteArray,
            alias: String,
            alias_file: String,
            alias_iv: String
        ): Boolean {
            validateSet(data, alias, alias_file, alias_iv)
            try {
                lock.lock()
                val keyStore = KeyStore.getInstance(ANDROID_KEY_STORE)
                keyStore!!.load(null)
                var secretKey: SecretKey? = keyStore.getKey(alias, null) as SecretKey?
                val inCipher = Cipher.getInstance(NEW_CIPHER_ALGORITHM)

                if (secretKey == null) {
                    //create key if not present
                    secretKey = createKeys(alias)
                    try {
                        inCipher.init(Cipher.ENCRYPT_MODE, secretKey)
                    } catch (e: InvalidKeyException) {
                        FirebaseCrashlytics.getInstance().recordException(e)
                    }
                } else {
                    //see if the key is old format, create a new one if it is
                    try {
                        inCipher.init(Cipher.ENCRYPT_MODE, secretKey)
                    } catch (ignored: InvalidKeyException) {
                        Log.e(TAG, "setData: OLD KEY PRESENT: $alias")
                        //create new key and reinitialize the cipher
                        secretKey = createKeys(alias)
                        try {
                            inCipher.init(Cipher.ENCRYPT_MODE, secretKey)
                        } catch (e: InvalidKeyException) {
                            FirebaseCrashlytics.getInstance().recordException(e)
                        }
                    }
                }
                //the key cannot still be null
                if (secretKey == null) {
                    return false
                }

                val iv = inCipher.iv ?: throw NullPointerException("iv is null!")

                //store the iv
                storeEncryptedData(context, iv, alias_iv)
                val encryptedData = inCipher.doFinal(data)
                //store the encrypted data
                storeEncryptedData(context, encryptedData, alias)
                return true
            } catch (ex: InvalidKeyException) {
                return false
            } catch (e: Exception) {
                e.printStackTrace()
                return false
            } finally {
                lock.unlock()
            }
        }

        @JvmStatic
        private fun getData(
            context: Context,
            alias: String,
            alias_file: String,
            alias_iv: String
        ): ByteArray? {
            validateGet(alias, alias_file, alias_iv)//validate entries
            //        Log.e(TAG, "getData: " + alias);
            try {
                lock.lock()
                val keyStore = KeyStore.getInstance(ANDROID_KEY_STORE)
                Log.e("keyStore", keyStore.toString())
                keyStore!!.load(null)
                val secretKey: SecretKey? = keyStore.getKey(alias, null) as SecretKey?

                var encryptedData = retrieveEncryptedData(context, alias)
                if (encryptedData != null) {
                    //new format data is present, good
                    val iv = retrieveEncryptedData(context, alias_iv)
                    if (iv == null) {
                        if (alias.equals(PHRASE_ALIAS, ignoreCase = true))
                            throw RuntimeException("iv is missing when data isn't: $alias (Can't proceed, risking user's phrase! )") //crash here!
                        return null
                    }
                    val outCipher: Cipher = Cipher.getInstance(NEW_CIPHER_ALGORITHM)
                    outCipher.init(Cipher.DECRYPT_MODE, secretKey, GCMParameterSpec(128, iv))
                    try {
                        val decryptedData = outCipher.doFinal(encryptedData)
                        if (decryptedData != null) {
                            return decryptedData
                        }
                    } catch (e: IllegalBlockSizeException) {
                        e.printStackTrace()
                        throw RuntimeException("failed to decrypt data: " + e.message)
                    } catch (e: BadPaddingException) {
                        e.printStackTrace()
                        throw RuntimeException("failed to decrypt data: " + e.message)
                    }

                }
                //no new format data, get the old one and migrate it to the new format
                val encryptedDataFilePath = getFilePath(alias_file, context)

                if (secretKey == null) {
                    /* no such key, the key is just simply not there */
                    val fileExists = File(encryptedDataFilePath).exists()
                    //                Log.e(TAG, "getData: " + alias + " file exist: " + fileExists);
                    return if (!fileExists) {
                        null/* file also not there, fine then */
                    } else null
                }

                val ivExists = File(getFilePath(alias_iv, context)).exists()
                val aliasExists = File(getFilePath(alias_file, context)).exists()
                //cannot happen, they all should be present
                if (!ivExists || !aliasExists) {
                    removeAliasAndDatas(keyStore, alias, context)
                    //report it if one exists and not the other.
                    return if (ivExists != aliasExists) {
                        null
                    } else {
                        null
                    }
                }

                var iv = readBytesFromFile(getFilePath(alias_iv, context))
                val outCipher = Cipher.getInstance(CIPHER_ALGORITHM)
                outCipher.init(Cipher.DECRYPT_MODE, secretKey, IvParameterSpec(iv!!))
                val cipherInputStream =
                    CipherInputStream(FileInputStream(encryptedDataFilePath), outCipher)
                val result = BytesUtil.readBytesFromStream(cipherInputStream)

                //create the new format key
                val newKey = createKeys(alias)
                    ?: throw RuntimeException("Failed to create new key for alias $alias")
                //            SecretKey newKey = createKeys(alias, (alias.equals(PHRASE_ALIAS) || alias.equals(CANARY_ALIAS)));
                val inCipher = Cipher.getInstance(NEW_CIPHER_ALGORITHM)
                //init the cipher
                inCipher.init(Cipher.ENCRYPT_MODE, newKey)
                iv = inCipher.iv
                //store the new iv
                storeEncryptedData(context, iv, alias_iv)
                //encrypt the data
                encryptedData = inCipher.doFinal(result)
                //store the new data
                storeEncryptedData(context, encryptedData, alias)
                return result

            } catch (e: InvalidKeyException) {

                throw e

            } catch (e: IOException) {
                /** keyStore.load(null) threw the Exception, meaning the keystore is unavailable  */
                Log.e(
                    TAG,
                    "getData: keyStore.load(null) threw the Exception, meaning the keystore is unavailable",
                    e
                )
                if (e is FileNotFoundException) {
                    Log.e(TAG, "getData: File not found exception", e)

                    throw RuntimeException(e.message)
                } else {
                    throw RuntimeException(e.message)
                }

            } catch (e: CertificateException) {
                Log.e(
                    TAG,
                    "getData: keyStore.load(null) threw the Exception, meaning the keystore is unavailable",
                    e
                )
                if (e is FileNotFoundException) {
                    Log.e(TAG, "getData: File not found exception", e)
                    throw RuntimeException(e.message)
                } else {
                    throw RuntimeException(e.message)
                }
            } catch (e: KeyStoreException) {
                Log.e(
                    TAG,
                    "getData: keyStore.load(null) threw the Exception, meaning the keystore is unavailable",
                    e
                )
                if (e is FileNotFoundException) {
                    Log.e(TAG, "getData: File not found exception", e)
                    throw RuntimeException(e.message)
                } else {
                    throw RuntimeException(e.message)
                }
            } catch (e: UnrecoverableKeyException) {
                /** if for any other reason the keystore fails, crash!  */
                Log.e(TAG, "getData: error: " + e.javaClass.superclass?.name)
                throw RuntimeException(e.message)
            } catch (e: NoSuchAlgorithmException) {
                Log.e(TAG, "getData: error: " + e.javaClass.superclass?.name)
                throw RuntimeException(e.message)
            } catch (e: NoSuchPaddingException) {
                Log.e(TAG, "getData: error: " + e.javaClass.superclass?.name)
                throw RuntimeException(e.message)
            } catch (e: InvalidAlgorithmParameterException) {
                Log.e(TAG, "getData: error: " + e.javaClass.superclass?.name)
                throw RuntimeException(e.message)
            } catch (e: BadPaddingException) {
                e.printStackTrace()
                throw RuntimeException(e.message)
            } catch (e: IllegalBlockSizeException) {
                e.printStackTrace()
                throw RuntimeException(e.message)
            } catch (e: NoSuchProviderException) {
                e.printStackTrace()
                throw RuntimeException(e.message)
            } finally {
                lock.unlock()
            }
        }

        private fun readBytesFromFile(path: String): ByteArray? {
            var bytes: ByteArray? = null
            try {
                val file = File(path)
                val fin = FileInputStream(file)
                bytes = BytesUtil.readBytesFromStream(fin)
            } catch (e: IOException) {
                e.printStackTrace()
            }

            return bytes
        }

        @Synchronized
        fun removeAliasAndDatas(keyStore: java.security.KeyStore, alias: String, context: Context) {
            try {
                keyStore.deleteEntry(alias)
                val iv = aliasObjectMap[alias] ?: return
                WalletKeyStore.destroyEncryptedData(context, alias)
                WalletKeyStore.destroyEncryptedData(context, iv.ivFileName)
            } catch (e: KeyStoreException) {
                e.printStackTrace()
            }
        }

        @JvmStatic
        fun destroyEncryptedData(ctx: Context, name: String) {
            val pref = ctx.getSharedPreferences(KEY_STORE_PREFS_NAME, Context.MODE_PRIVATE)
            val edit = pref.edit()
            edit.remove(name)
            edit.apply()
        }

        @JvmStatic
        fun storeEncryptedData(ctx: Context, data: ByteArray?, name: String) {
            val pref = ctx.getSharedPreferences(KEY_STORE_PREFS_NAME, Context.MODE_PRIVATE)
            val base64 = Base64.encodeToString(data, Base64.DEFAULT)
            val edit = pref.edit()
            edit.putString(name, base64)
            edit.apply()

        }

        @JvmStatic
        fun getPhrase(context: Context, isBip39NewPhrase: Boolean = false): ByteArray? {
            val KEYALIAS = if (isBip39NewPhrase) BIP39_PHRASE_ALISA else PHRASE_ALIAS
            val obj = aliasObjectMap[KEYALIAS] ?: return null
            return getData(context, obj.alias, obj.datafileName, obj.ivFileName)
        }

        @JvmStatic
        fun getPhraseString(context: Context) = getPhrase(context)?.let { String(it) }

        @JvmStatic
        fun putPhrase(
            strToStore: ByteArray?,
            context: Context,
            isBip39NewPhrase: Boolean = false
        ): Boolean {
            var KEYALIAS = if (isBip39NewPhrase) BIP39_PHRASE_ALISA else PHRASE_ALIAS
            val obj = aliasObjectMap[KEYALIAS] ?: return false
            return !(strToStore == null || strToStore.isEmpty()) && setData(
                context,
                strToStore,
                obj.alias,
                obj.datafileName,
                obj.ivFileName
            )
        }

        @JvmStatic
        fun putEthPublicKey(masterPubKey: ByteArray?, context: Context): Boolean {
            val obj = aliasObjectMap[ETH_PUBKEY_ALIAS] ?: return false
            return masterPubKey != null && masterPubKey.isNotEmpty() && setData(
                context,
                masterPubKey,
                obj.alias,
                obj.datafileName,
                obj.ivFileName
            )
        }

        @JvmStatic
        fun getEthPublicKey(context: Context): ByteArray? {
            val obj = aliasObjectMap[ETH_PUBKEY_ALIAS] ?: return null
            return getData(context, obj.alias, obj.datafileName, obj.ivFileName)
        }

        @JvmStatic
        fun putAuthKey(authKey: ByteArray?, context: Context): Boolean {
            val obj = aliasObjectMap[AUTH_KEY_ALIAS] ?: return false
            return authKey != null && authKey.isNotEmpty() && setData(
                context,
                authKey,
                obj.alias,
                obj.datafileName,
                obj.ivFileName
            )
        }

//        @JvmStatic
//        fun getAuthKey(context: Context): ByteArray? {
//            val obj = aliasObjectMap[AUTH_KEY_ALIAS] ?: return null
//            return getData(context, obj.alias, obj.datafileName, obj.ivFileName)
//        }

        @JvmStatic
        fun retrieveEncryptedData(ctx: Context, name: String): ByteArray? {
            val pref = ctx.getSharedPreferences(KEY_STORE_PREFS_NAME, Context.MODE_PRIVATE)
            val base64 = pref.getString(name, null) ?: return null
            return Base64.decode(base64, Base64.DEFAULT)
        }

        @JvmStatic
        fun getFilePath(fileName: String, context: Context): String {
            val filesDirectory = context.filesDir.absolutePath
            return filesDirectory + File.separator + fileName
        }

        @JvmStatic
        fun putMasterPublicKey(
            masterPubKey: ByteArray?,
            context: Context,
            isBip39NewPhrase: Boolean = false
        ): Boolean {
            val KEYALIAS = if (isBip39NewPhrase) BIP39_PUB_KEY_ALIAS else PUB_KEY_ALIAS
            val obj = aliasObjectMap[KEYALIAS] ?: return false
            return masterPubKey != null && masterPubKey.isNotEmpty() && setData(
                context,
                masterPubKey,
                obj.alias,
                obj.datafileName,
                obj.ivFileName
            )
        }

        @JvmStatic
        fun getMasterPublicKey(context: Context, isBip39NewPhrase: Boolean = false): ByteArray? {
            var KEYALIAS = if (isBip39NewPhrase) BIP39_PUB_KEY_ALIAS else PUB_KEY_ALIAS
            val obj = aliasObjectMap[KEYALIAS] ?: return null
            return getData(context, obj.alias, obj.datafileName, obj.ivFileName)
        }

        @JvmStatic
        fun putWalletCreationTime(creationTime: Int, context: Context): Boolean {
            val obj = aliasObjectMap[WALLET_CREATION_TIME_ALIAS] ?: return false
            val bytesToStore = BytesUtil.intToBytes(creationTime)
            return bytesToStore.isNotEmpty() && setData(
                context,
                bytesToStore,
                obj.alias,
                obj.datafileName,
                obj.ivFileName
            )
        }

        @JvmStatic
        fun getWalletCreationTime(context: Context): Int {
            val obj = aliasObjectMap[WALLET_CREATION_TIME_ALIAS] ?: return 0
            val result: ByteArray? = getData(context, obj.alias, obj.datafileName, obj.ivFileName)
            return if (result == null) {
                0
            } else {
                BytesUtil.bytesToInt(result)
            }
        }

        @Synchronized
        fun resetWalletKeyStore(context: Context): Boolean {
            val keyStore: KeyStore
            try {
                keyStore = KeyStore.getInstance(ANDROID_KEY_STORE)
                keyStore.load(null)
                var count = 0
                if (keyStore.aliases() != null) {
                    while (keyStore.aliases().hasMoreElements()) {
                        val alias = keyStore.aliases().nextElement()
                        removeAliasAndDatas(keyStore, alias, context)
                        destroyEncryptedData(context, alias)
                        count++
                    }
                } else {
                    return false
                }
                Log.e(TAG, "resetWalletKeyStore: removed:$count")
            } catch (e: NoSuchAlgorithmException) {
                e.printStackTrace()
                return false
            } catch (e: KeyStoreException) {
                e.printStackTrace()
                return false
            } catch (e: IOException) {
                e.printStackTrace()
                return false
            } catch (e: java.security.cert.CertificateException) {
                e.printStackTrace()
            }

            return true
        }

        class AliasObject internal constructor(
            var alias: String,
            var datafileName: String,
            var ivFileName: String
        )
    }
}