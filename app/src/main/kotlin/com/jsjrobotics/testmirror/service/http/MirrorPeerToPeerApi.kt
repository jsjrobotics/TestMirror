package com.jsjrobotics.testmirror.service.http

import com.jsjrobotics.testmirror.dataStructures.networking.MirrorDataResponse
import retrofit2.Call
import retrofit2.http.GET

interface MirrorPeerToPeerApi {
    @GET(" ")
    fun getMirrorData(): Call<MirrorDataResponse>

}