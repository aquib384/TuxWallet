package io.tux.wallet.testnet.repository

import io.tux.wallet.testnet.apis.ApiInterface
import javax.inject.Inject


class StakesRepository @Inject constructor(private val apiInterface: ApiInterface )
{

    fun stakeAda(map: Map<String, Any?>) = apiInterface.stakeAda(map)
    fun stakeXTZ(map: Map<String, Any?>) = apiInterface.stakeAda(map)
    fun getStakeList(id :String) =apiInterface.getStakesList(id)
    fun saveStake(map: Map<String, Any?>) =apiInterface.submitStake(map)


}