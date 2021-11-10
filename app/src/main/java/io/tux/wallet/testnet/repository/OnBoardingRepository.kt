package io.tux.wallet.testnet.repository

import io.tux.wallet.testnet.apis.ApiInterface
import javax.inject.Inject


class OnBoardingRepository @Inject constructor(private val apiInterface: ApiInterface) {


    fun updatePin(pin: String, walletId: Long) = apiInterface.updateWalletPin(pin, walletId)
    fun updateBiometric(lockType: String, walletId: Long) =
        apiInterface.updateBiometrics(lockType, walletId)

    fun setCoinAddress2(list: ArrayList<HashMap<String, Any>>) =
        apiInterface.linkAddressToWalletId2(list)

    suspend fun getEntropy(map:Map<String, Any?>) =
        apiInterface.getEntropy(map)


    fun sendEmail(map :HashMap<String?,Any?>) = apiInterface.submitEmail(map)
    fun updateFcmStatus(map :HashMap<String?,Any?>) = apiInterface.updateFCMStatus(map)
    fun setFcmStatus(map :HashMap<String?,Any?>) = apiInterface.setFCMStatus(map)
    fun getFcmStatus(id:String) = apiInterface.getFCMStatus(id)


}