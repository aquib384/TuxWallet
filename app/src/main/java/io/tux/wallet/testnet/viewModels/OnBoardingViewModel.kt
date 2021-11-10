package io.tux.wallet.testnet.viewModels


import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.tux.wallet.testnet.apis.ApiInterface
import io.tux.wallet.testnet.model.ContactUsModel
import io.tux.wallet.testnet.model.FCMStatusModel
import io.tux.wallet.testnet.model.SaveFcmStatusResponse
import io.tux.wallet.testnet.model.address.ADAAddressModel
import io.tux.wallet.testnet.model.address.LTCAddressModel
import io.tux.wallet.testnet.model.address.XLMAddressModel
import io.tux.wallet.testnet.model.address.XRPAddressModel
import io.tux.wallet.testnet.model.coins.CoinListModel
import io.tux.wallet.testnet.model.wallet.MapAddressModel
import io.tux.wallet.testnet.model.wallet.RecoverWalletResponseModel
import io.tux.wallet.testnet.repository.OnBoardingRepository
import io.tux.wallet.testnet.utils.SharedPref
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import wallet.core.jni.HDWallet
import javax.inject.Inject


@HiltViewModel
class OnBoardingViewModel @Inject constructor(
    private val sharedPref: SharedPref,
    private val onBoardingRepository: OnBoardingRepository
) :
    ViewModel() {

    val errorMsg = MutableLiveData<String>()
    var recoverModel = MutableLiveData<RecoverWalletResponseModel>()
    var stellarModel = MutableLiveData<XLMAddressModel>()
    var rippleModel = MutableLiveData<XRPAddressModel>()
    var adaModel = MutableLiveData<ADAAddressModel>()
    var btcModel = MutableLiveData<LTCAddressModel>()
    var ltcModel = MutableLiveData<LTCAddressModel>()
    var mapAddrModel1 = MutableLiveData<MapAddressModel>()
    var saveFcmModel = MutableLiveData<SaveFcmStatusResponse>()
    var fcmStatusModel = MutableLiveData<FCMStatusModel>()
    var contactUsModel = MutableLiveData<ContactUsModel>()
    var keyList = MutableLiveData<List<String>>()
    private lateinit var myWallet: HDWallet

    var cList = MutableLiveData<ArrayList<CoinListModel>>()

    fun setKeyList(address: List<String>) {
        keyList.value = address
    }


    fun updatePin(pin: String, walletId: Long) {
        onBoardingRepository.updatePin(pin, walletId)
            .enqueue(object : Callback<RecoverWalletResponseModel> {
                override fun onResponse(
                    call: Call<RecoverWalletResponseModel>,
                    response: Response<RecoverWalletResponseModel>
                ) {
                    recoverModel.value = response.body()
                }

                override fun onFailure(call: Call<RecoverWalletResponseModel>, t: Throwable) {
                    errorMsg.value = t.message
                }
            })


    }

    fun updateBiometrics(pin: String, walletId: Long) {
        onBoardingRepository.updateBiometric(pin, walletId)
            .enqueue(object : Callback<RecoverWalletResponseModel> {
                override fun onResponse(
                    call: Call<RecoverWalletResponseModel>,
                    response: Response<RecoverWalletResponseModel>
                ) {
                    recoverModel.value = response.body()
                }

                override fun onFailure(call: Call<RecoverWalletResponseModel>, t: Throwable) {
                    errorMsg.value = t.message
                }

            })


    }

    fun sendEmail(map: HashMap<String?, Any?>) {
        onBoardingRepository.sendEmail(map)
            .enqueue(object : Callback<ContactUsModel> {
                override fun onResponse(
                    call: Call<ContactUsModel>,
                    response: Response<ContactUsModel>
                ) {
                    contactUsModel.value = response.body()
                }

                override fun onFailure(call: Call<ContactUsModel>, t: Throwable) {
                    errorMsg.value = t.message
                }

            })


    }

    fun updateFcmStatus(map: HashMap<String?, Any?>) {
        onBoardingRepository.updateFcmStatus(map)
            .enqueue(object : Callback<SaveFcmStatusResponse> {
                override fun onResponse(
                    call: Call<SaveFcmStatusResponse>,
                    response: Response<SaveFcmStatusResponse>
                ) {
                    saveFcmModel.value = response.body()
                }

                override fun onFailure(call: Call<SaveFcmStatusResponse>, t: Throwable) {
                    errorMsg.value = t.message
                }

            })


    }

    fun setFcmStatus(map: HashMap<String?, Any?>) {
        onBoardingRepository.setFcmStatus(map)
            .enqueue(object : Callback<SaveFcmStatusResponse> {
                override fun onResponse(
                    call: Call<SaveFcmStatusResponse>,
                    response: Response<SaveFcmStatusResponse>
                ) {
                    saveFcmModel.value = response.body()
                }

                override fun onFailure(call: Call<SaveFcmStatusResponse>, t: Throwable) {
                    errorMsg.value = t.message
                }

            })


    }

    fun getFcmStatus(id: String) {
        onBoardingRepository.getFcmStatus(id)
            .enqueue(object : Callback<FCMStatusModel> {
                override fun onResponse(
                    call: Call<FCMStatusModel>,
                    response: Response<FCMStatusModel>
                ) {
                    fcmStatusModel.value = response.body()
                }

                override fun onFailure(call: Call<FCMStatusModel>, t: Throwable) {
                    errorMsg.value = t.message
                }

            })
    }

    fun getStellerAddress(apiInterface: ApiInterface, hashMap: HashMap<String, Any>) {
        apiInterface.getStellerAddress(hashMap).enqueue(object : Callback<XLMAddressModel> {
            override fun onResponse(
                call: Call<XLMAddressModel>,
                response: Response<XLMAddressModel>
            ) {
                if (response.isSuccessful) {
                    stellarModel.postValue(response.body())
                }
            }

            override fun onFailure(call: Call<XLMAddressModel>, t: Throwable) {
                t.printStackTrace()
            }

        })
    }

    fun getRippleAddress(apiInterface: ApiInterface) {
        apiInterface.getRippleAddress().enqueue(object : Callback<XRPAddressModel> {
            override fun onResponse(
                call: Call<XRPAddressModel>,
                response: Response<XRPAddressModel>
            ) {
                if (response.isSuccessful) {
                    rippleModel.postValue(response.body())
                }
            }

            override fun onFailure(call: Call<XRPAddressModel>, t: Throwable) {
                t.printStackTrace()
            }

        })
    }

    fun getADAAddress(apiInterface: ApiInterface, map: Map<String, Any?>) {
        apiInterface.getADAAddress(map).enqueue(object : Callback<ADAAddressModel> {
            override fun onResponse(
                call: Call<ADAAddressModel>,
                response: Response<ADAAddressModel>
            ) {
                if (response.isSuccessful) {
                    adaModel.postValue(response.body())
                }
            }

            override fun onFailure(call: Call<ADAAddressModel>, t: Throwable) {
                t.printStackTrace()
            }

        })
    }

    fun getBTCAddress(apiInterface: ApiInterface) {
        apiInterface.getBTCAddress().enqueue(object : Callback<LTCAddressModel> {
            override fun onResponse(
                call: Call<LTCAddressModel>,
                response: Response<LTCAddressModel>
            ) {
                if (response.isSuccessful) {
                    btcModel.postValue(response.body())
                }
            }

            override fun onFailure(call: Call<LTCAddressModel>, t: Throwable) {
                t.printStackTrace()
            }

        })
    }

    fun getLTCAddress(apiInterface: ApiInterface) {
        apiInterface.getLTCAddress().enqueue(object : Callback<LTCAddressModel> {
            override fun onResponse(
                call: Call<LTCAddressModel>,
                response: Response<LTCAddressModel>
            ) {
                if (response.isSuccessful) {
                    ltcModel.postValue(response.body())
                }
            }

            override fun onFailure(call: Call<LTCAddressModel>, t: Throwable) {
                t.printStackTrace()
            }

        })
    }


    fun mapAddresses1(list: ArrayList<HashMap<String, Any>>) {
        try {
            onBoardingRepository.setCoinAddress2(list)
                .enqueue(object : Callback<MapAddressModel> {
                    override fun onResponse(
                        call: Call<MapAddressModel>,
                        responseModel: Response<MapAddressModel>
                    ) {

                        Log.e("map_response", responseModel.body().toString())
                        if (responseModel.code() == 200) {
                            Log.e(
                                "mapAddress",
                                responseModel.body()?.message.toString()
                            )
                            mapAddrModel1.postValue(responseModel.body())
                        }
                    }

                    override fun onFailure(call: Call<MapAddressModel>, t: Throwable) {
                        errorMsg.postValue(t.message)
                    }
                })

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun createEntropy(map: Map<String, Any?>) =
        onBoardingRepository.getEntropy(map)


}