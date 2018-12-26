package com.jsjrobotics.testmirror.service.networking

import com.jsjrobotics.testmirror.dataStructures.networking.MirrorDataResponse
import com.jsjrobotics.testmirror.dataStructures.networking.requests.SignUpRequest
import com.jsjrobotics.testmirror.dataStructures.networking.responses.SignUpResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface MirrorPeerToPeerApi {
    @GET(" ")
    fun getMirrorData(): Call<MirrorDataResponse>

}