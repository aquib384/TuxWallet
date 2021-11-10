package io.tux.wallet.testnet.repository

import io.tux.wallet.testnet.apis.ApiInterface
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CoinMarketRepository @Inject constructor(private val apiInterface: ApiInterface) {
    suspend fun getCoinList(params: HashMap<String?, Any?>) = apiInterface.getCoinsListData(params)
    fun getCoinDayData(params: HashMap<String?, Any?>) = apiInterface.getHistoricalDataDay(params)
    fun getCoinMinData(params: HashMap<String?, Any?>) =
        apiInterface.getHistoricalDataMinute(params)

    fun getCoinHourData(params: HashMap<String?, Any?>) = apiInterface.getHistoricalDataHour(params)
//    fun getCoinTopPairs(coin: String, limit: Int) = apiInterface.getCoinTopPairs(coin, limit)
    fun getERC20Coins() = apiInterface.getCoins()
    suspend fun getERC20Balance(params: HashMap<String?, Any?>) =
        apiInterface.getERC20Balance(params)

    suspend fun getETHBalance(add: String) = apiInterface.getETHBalance(add)
    suspend fun getBTCBalance(add: String) = apiInterface.getBTCBalance(add)
    suspend fun getXRPBalance(add: String) = apiInterface.getXRPBalance(add)
    suspend fun getXTZBalance(add: String) = apiInterface.getXTZBalance(add)
    suspend fun getEOSBalance(add: String) = apiInterface.getEOSBalance(add)
    suspend fun getLTCBalance(add: String) = apiInterface.getLTCBalance(add)
    suspend fun getNEOBalance(add: String) = apiInterface.getNEOBalance(add)
    suspend fun getNEMBalance(add: String) = apiInterface.getNEMBalance(add)
    suspend fun getALGOBalance(add: String) = apiInterface.getALGOBalance(add)
    suspend fun getATOMBalance(add: String) = apiInterface.getATomBalance(add)
    suspend fun getBCHBalance(add: String) = apiInterface.getBCHBalance(add)
    suspend fun getTRXBalance(add: String) = apiInterface.getTRXBalance(add)
    suspend fun getXLMBalance(add: String) = apiInterface.getXLMBalance(add)
    suspend fun getADABalance(add: String) = apiInterface.getADABalance(add)

    suspend fun getBalance(address: String, coin: String) = apiInterface.getBalance(address, coin)

    fun getCoinMarketPairs(coin :String , limit :Int) = apiInterface.getMarketPairs(coin,limit)
}