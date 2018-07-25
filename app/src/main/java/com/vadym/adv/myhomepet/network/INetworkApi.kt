package com.vadym.adv.myhomepet.network

import com.vadym.adv.myhomepet.data.OwnerData
import io.reactivex.Observable
import retrofit2.http.GET

interface INetworkApi {

    @GET(Endpoints.owner)
    fun getAllOwner(): Observable<List<OwnerData>>
}