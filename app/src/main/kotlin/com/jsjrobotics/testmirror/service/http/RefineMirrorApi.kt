package com.jsjrobotics.testmirror.service.http

import com.jsjrobotics.testmirror.dataStructures.networking.UpdateUserDataRequest
import com.jsjrobotics.testmirror.dataStructures.networking.requests.LoginRequest
import com.jsjrobotics.testmirror.dataStructures.networking.requests.SignUpRequest
import com.jsjrobotics.testmirror.dataStructures.networking.responses.LoginResponse
import com.jsjrobotics.testmirror.dataStructures.networking.responses.SignUpResponse
import com.jsjrobotics.testmirror.dataStructures.networking.responses.UpdateDataResponse
import com.jsjrobotics.testmirror.dataStructures.networking.responses.UserDataResponse
import retrofit2.Call
import retrofit2.http.*

interface RefineMirrorApi {
    @POST(Paths.SIGNUP_PATH)
    fun signUp(@Body signUpRequest: SignUpRequest): Call<SignUpResponse>

    @POST(Paths.LOGIN_PATH)
    fun login(@Body loginRequest: LoginRequest): Call<LoginResponse>

    @GET(Paths.USER_ME_PATH)
    fun getUserData(@Header(Paths.AUTHORIZATION_HEADER) userkey : String) : Call<UserDataResponse>

    @PATCH(Paths.USER_ME_PATH)
    fun updateUserData(@Header(Paths.AUTHORIZATION_HEADER) userkey : String,
                       @Body updateUserData: UpdateUserDataRequest) : Call<UpdateDataResponse>
}