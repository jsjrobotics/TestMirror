package com.jsjrobotics.testmirror.service.networking

import com.jsjrobotics.testmirror.dataStructures.networking.responses.LoginResponse
import com.jsjrobotics.testmirror.dataStructures.networking.responses.SignUpResponse
import com.jsjrobotics.testmirror.dataStructures.networking.responses.UserDataResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST

interface RefineMirrorApi {
    @POST(Paths.SIGNUP_PATH)
    fun signUp(): Call<SignUpResponse>

    @POST(Paths.LOGIN_PATH)
    fun login(): Call<LoginResponse>

    @GET(Paths.USER_DATA_PATH)
    fun getUserData(@Header(Paths.AUTHORIZATION_HEADER) userkey : String) : Call<UserDataResponse>

    @PATCH
    fun updateUserData() : Call<UpdateDataResponse>
}