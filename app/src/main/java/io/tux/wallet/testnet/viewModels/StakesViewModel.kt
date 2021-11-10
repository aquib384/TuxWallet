package io.tux.wallet.testnet.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.crashlytics.FirebaseCrashlytics

import dagger.hilt.android.lifecycle.HiltViewModel
import io.tux.wallet.testnet.model.stake.StakeResponse
import io.tux.wallet.testnet.model.stake.StakesHistoryModel
import io.tux.wallet.testnet.model.stake.SubmitStakeModel
import io.tux.wallet.testnet.repository.StakesRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class StakesViewModel@Inject constructor(
    private val stakesRepository: StakesRepository): ViewModel() {

    var errorMsg = MutableLiveData<String>()
    var successMsg = MutableLiveData<String>()
    lateinit var tab: String

    //top pairs
    var stakesHistoryModel = MutableLiveData<StakesHistoryModel>()
    var submitStakeModel = MutableLiveData<SubmitStakeModel>()
    var stakesResponse = MutableLiveData<StakeResponse>()


    fun stakeAda(map: Map<String, Any?>)
    {
    stakesRepository.stakeAda(map).enqueue(
    object : Callback<StakeResponse> {
        override fun onResponse(call: Call<StakeResponse>, response: Response<StakeResponse>) {
            stakesResponse.postValue(response.body())
        }

        override fun onFailure(call: Call<StakeResponse>, t: Throwable) {

        }
    })
}


    fun stakeXTZ(map: Map<String, Any?>) {
        stakesRepository.stakeXTZ(map).enqueue(object : Callback<StakeResponse> {
            override fun onResponse(call: Call<StakeResponse>, response: Response<StakeResponse>) {
                if (response.isSuccessful && response.code() == 200) {
                    stakesResponse.postValue(response.body())
                }
            }

            override fun onFailure(call: Call<StakeResponse>, t: Throwable) {

            }
        })
    }

    fun getStakeHistory(walletId :String) {
        stakesRepository.getStakeList(walletId).enqueue(object : Callback<StakesHistoryModel> {
            override fun onResponse(
                call: Call<StakesHistoryModel>,
                response: Response<StakesHistoryModel>
            ) {
                if (response.isSuccessful && response.code() == 200) {
                    stakesHistoryModel.postValue(response.body())
                }
            }

            override fun onFailure(call: Call<StakesHistoryModel>, t: Throwable) {
                FirebaseCrashlytics.getInstance().recordException(t)
                errorMsg.postValue(t.message)
            }
        })
    }

    fun saveStake(hashMap: HashMap<String,Any?>) {
        stakesRepository.saveStake(hashMap).enqueue(object : Callback<SubmitStakeModel> {
            override fun onResponse(
                call: Call<SubmitStakeModel>,
                response: Response<SubmitStakeModel>
            ) {
                if (response.isSuccessful && response.code() == 200) {
                    submitStakeModel.postValue(response.body())
                }
            }

            override fun onFailure(call: Call<SubmitStakeModel>, t: Throwable) {
                FirebaseCrashlytics.getInstance().recordException(t)
                errorMsg.postValue(t.message)
            }
        })
    }
}