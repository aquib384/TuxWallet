package io.tux.wallet.testnet.utils

import android.content.Context
import android.content.SharedPreferences
import io.tux.wallet.testnet.utils.Constants.BCH_ADDRESS_KEY
import io.tux.wallet.testnet.utils.Constants.BCH_WIF_KEY
import io.tux.wallet.testnet.utils.Constants.BTC_ADDRESS_KEY
import io.tux.wallet.testnet.utils.Constants.LTC_ADDRESS_KEY
import io.tux.wallet.testnet.utils.Constants.CURRENCY
import io.tux.wallet.testnet.utils.Constants.CURRENCY_SYMBOL
import io.tux.wallet.testnet.utils.Constants.DEVICE_ID
import io.tux.wallet.testnet.utils.Constants.ENTROPY_KEY
import io.tux.wallet.testnet.utils.Constants.ETH_ADDRESS_KEY
import io.tux.wallet.testnet.utils.Constants.ETH_PRIVATE_KEY
import io.tux.wallet.testnet.utils.Constants.FCM_TOKEN_ID
import io.tux.wallet.testnet.utils.Constants.IS_BIOMETRIC_ENABLE
import io.tux.wallet.testnet.utils.Constants.IS_FIRST_TIME
import io.tux.wallet.testnet.utils.Constants.IS_LIGHT_THEME
import io.tux.wallet.testnet.utils.Constants.IS_LOGIN_KEY
import io.tux.wallet.testnet.utils.Constants.IS_NOTIFY_ENABLE
import io.tux.wallet.testnet.utils.Constants.IS_OLD_USER
import io.tux.wallet.testnet.utils.Constants.IS_PIN_ENABLE
import io.tux.wallet.testnet.utils.Constants.KEYLIST
import io.tux.wallet.testnet.utils.Constants.LTC_WIF_KEY
import io.tux.wallet.testnet.utils.Constants.MNEMONIC_LANGUGAGE
import io.tux.wallet.testnet.utils.Constants.SHARED_PREF_KEY
import io.tux.wallet.testnet.utils.Constants.SP_KEY_LANGUAGE
import io.tux.wallet.testnet.utils.Constants.SP_PIN_CODE_LOGIN
import io.tux.wallet.testnet.utils.Constants.WALLET_ADDRESSES
import io.tux.wallet.testnet.utils.Constants.WALLET_ID
import io.tux.wallet.testnet.utils.Constants.WALLET_PHRASE
import io.tux.wallet.testnet.utils.Constants.WIF_KEY


class SharedPref(context: Context?) {
    private val sharedPreferences: SharedPreferences =
        context!!.getSharedPreferences(SHARED_PREF_KEY, Context.MODE_PRIVATE)


    fun setLightTheme(lightTheme: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean(IS_LIGHT_THEME, lightTheme)
        editor.apply()
    }

    fun isLightTheme(): Boolean {
        return sharedPreferences.getBoolean(IS_LIGHT_THEME, false)
    }

    fun setLogin(isLogin: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean(IS_LOGIN_KEY, isLogin)
        editor.apply()
    }

    fun isLogin(): Boolean {
        return sharedPreferences.getBoolean(IS_LOGIN_KEY, false)
    }

    fun setFirstTime(isLogin: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean(IS_FIRST_TIME, isLogin)
        editor.apply()
    }

    fun isFirstTime(): Boolean {
        return sharedPreferences.getBoolean(IS_FIRST_TIME, true)
    }

    fun setOldUser(isLogin: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean(IS_OLD_USER, isLogin)
        editor.apply()
    }

    fun isOldUser(): Boolean {
        return sharedPreferences.getBoolean(IS_OLD_USER, true)
    }

    fun setFcmToken(isLogin: String?) {
        val editor = sharedPreferences.edit()
        editor.putString(FCM_TOKEN_ID, isLogin)
        editor.apply()
    }

    fun getFCMToken(): String? {
        return sharedPreferences.getString(FCM_TOKEN_ID, "")
    }

    fun getPin(): String? {
        return sharedPreferences.getString(SP_PIN_CODE_LOGIN, null)
    }

    fun setPin(pin: String?) {
        val editor = sharedPreferences.edit()
        editor.putString(SP_PIN_CODE_LOGIN, pin)
        editor.apply()
    }

    fun getWalletId(): String? {
        return sharedPreferences.getString(WALLET_ID, null)
    }

    fun setWalletId(w_id: String?) {
        val editor = sharedPreferences.edit()
        editor.putString(WALLET_ID, w_id)
        editor.apply()
    }

    fun getDeviceId(): String? {
        return sharedPreferences.getString(DEVICE_ID, null)
    }

    fun setDeviceId(w_id: String?) {
        val editor = sharedPreferences.edit()
        editor.putString(DEVICE_ID, w_id)
        editor.apply()
    }

    fun getPhrase(): String? {
        return sharedPreferences.getString(WALLET_PHRASE, null)
    }

    fun setPhrase(pin: String?) {
        val editor = sharedPreferences.edit()
        editor.putString(WALLET_PHRASE, pin)
        editor.apply()
    }

    fun getKeyList(): String? {
        return sharedPreferences.getString(KEYLIST, null)
    }

    fun setKeyList(keyList: String?) {
        val editor = sharedPreferences.edit()
        editor.putString(KEYLIST, keyList)
        editor.apply()
    }

    fun setPinEnabled(isEnable: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean(IS_PIN_ENABLE, isEnable)
        editor.apply()
    }

    fun isPinEnabled(): Boolean {
        return sharedPreferences.getBoolean(IS_PIN_ENABLE, false)
    }

    fun setNotifyEnabled(isEnable: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean(IS_NOTIFY_ENABLE, isEnable)
        editor.apply()
    }

    fun isNotifyEnabled(): Boolean {
        return sharedPreferences.getBoolean(IS_NOTIFY_ENABLE, false)
    }

    fun isBiometricEnabled(): Boolean {
        return sharedPreferences.getBoolean(IS_BIOMETRIC_ENABLE, false)
    }

    fun setBioMetricEnables(isEnable: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean(IS_BIOMETRIC_ENABLE, isEnable)
        editor.apply()
    }

    fun setLanguage(lang: Int) {
        val editor = sharedPreferences.edit()
        editor.putInt(SP_KEY_LANGUAGE, lang)
        editor.apply()
    }

    fun getLanguage(): Int? {
        return sharedPreferences.getInt(SP_KEY_LANGUAGE, 0)
    }

    fun setEntropy(lang: String) {
        val editor = sharedPreferences.edit()
        editor.putString(ENTROPY_KEY, lang)
        editor.apply()
    }

    fun getEntropy(): String? {
        return sharedPreferences.getString(ENTROPY_KEY, null)
    }

    fun setMnemonicLang(lang: String) {
        val editor = sharedPreferences.edit()
        editor.putString(MNEMONIC_LANGUGAGE, lang)
        editor.apply()
    }

    fun getMnemonicLang(): String? {
        return sharedPreferences.getString(MNEMONIC_LANGUGAGE, null)
    }


    fun setBtcAddress(lang: String) {
        val editor = sharedPreferences.edit()
        editor.putString(BTC_ADDRESS_KEY, lang)
        editor.apply()
    }

    fun setBchAddress(lang: String) {
        val editor = sharedPreferences.edit()
        editor.putString(BCH_ADDRESS_KEY, lang)
        editor.apply()
    }

    fun getBtcAddress() = sharedPreferences.getString(BTC_ADDRESS_KEY, "")

    fun setLtcAddress(lang: String?) {
        val editor = sharedPreferences.edit()
        editor.putString(LTC_ADDRESS_KEY, lang)
        editor.apply()
    }

    fun getBchAddress() = sharedPreferences.getString(BCH_ADDRESS_KEY, "")

    fun getLtcAddress() = sharedPreferences.getString(LTC_ADDRESS_KEY, "")

    fun setEthAddress(lang: String) {
        val editor = sharedPreferences.edit()
        editor.putString(ETH_ADDRESS_KEY, lang)
        editor.apply()
    }

    fun getEthAddress() = sharedPreferences.getString(ETH_ADDRESS_KEY, "")



    fun setWIF(lang: String) {
        val editor = sharedPreferences.edit()
        editor.putString(WIF_KEY, lang)
        editor.apply()
    }

    fun getWIF() = sharedPreferences.getString(WIF_KEY, "")

    fun setBCHWIF(lang: String) {
        val editor = sharedPreferences.edit()
        editor.putString(BCH_WIF_KEY, lang)
        editor.apply()
    }

    fun getBCHWIF() = sharedPreferences.getString(BCH_WIF_KEY, "")


    fun setLTCWIF(lang: String) {
        val editor = sharedPreferences.edit()
        editor.putString(LTC_WIF_KEY, lang)
        editor.apply()
    }

    fun getLTCWIF() = sharedPreferences.getString(LTC_WIF_KEY, "")




    fun setEthKey(lang: String) {
        val editor = sharedPreferences.edit()
        editor.putString(ETH_PRIVATE_KEY, lang)
        editor.apply()
    }

    fun getEthKey() = sharedPreferences.getString(ETH_PRIVATE_KEY, "")


    fun setCurrencyCode(currency: String) {
        val editor = sharedPreferences.edit()
        editor.putString(CURRENCY, currency)
        editor.apply()
    }

    fun setCurrencySymbol(currency: String) {
        val editor = sharedPreferences.edit()
        editor.putString(CURRENCY_SYMBOL, currency)
        editor.apply()
    }

    fun setAddresses(address: String) {
        val editor = sharedPreferences.edit()
        editor.putString(WALLET_ADDRESSES, address)
        editor.apply()
    }

    fun getAddresses(): String? {
        return sharedPreferences.getString(WALLET_ADDRESSES, null)
    }

    fun getCurrencyCode(): String? {
        return sharedPreferences.getString(CURRENCY, null)
    }

    fun getCurrencySymbol(): String? {
        return sharedPreferences.getString(CURRENCY_SYMBOL, null)
    }


    fun logoutWallet() {
        val editor = sharedPreferences.edit()
        editor.putBoolean(IS_LOGIN_KEY, false)
        editor.putBoolean(IS_PIN_ENABLE, false)
        editor.putBoolean(IS_BIOMETRIC_ENABLE, false)
        editor.putString(SP_PIN_CODE_LOGIN, "")
        editor.putString(WALLET_PHRASE, "")
        editor.putString(KEYLIST, "")
        editor.putString(CURRENCY, "")
        editor.putString(CURRENCY_SYMBOL, "")
        editor.putString(WALLET_ADDRESSES, "")
        editor.apply()

    }

    fun saveString(key: String, value: String) {
        val editor = sharedPreferences.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun getString(key: String) = sharedPreferences.getString(key, "")


}