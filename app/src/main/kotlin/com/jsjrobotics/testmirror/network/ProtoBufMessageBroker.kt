package com.jsjrobotics.testmirror.network

import com.mirror.proto.navigation.MirrorScreenRequest
import com.mirror.proto.navigation.PairingScreenResponse
import com.mirror.proto.oobe.PairResponse
import com.mirror.proto.user.IdentifyResponse
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProtoBufMessageBroker @Inject constructor(){

    private val identifyResponseEvent:  PublishSubject<IdentifyResponse> = PublishSubject.create()
    val onIdentifyResponse : Observable<IdentifyResponse> = identifyResponseEvent

    private val mirrorScreenRequestEvent:  PublishSubject<MirrorScreenRequest> = PublishSubject.create()
    val onMirrorScreenRequestEvent : Observable<MirrorScreenRequest> = mirrorScreenRequestEvent

    private val pairResponseEvent:  PublishSubject<PairResponse> = PublishSubject.create()
    val onPairResponseEvent : Observable<PairResponse> = pairResponseEvent

    fun dispatchIdentityResponse(response: IdentifyResponse) = identifyResponseEvent.onNext(response)
    fun dispatchMirrorScreenRequest(request: MirrorScreenRequest) = mirrorScreenRequestEvent.onNext(request)
    fun dispatchPairResponse(response: PairResponse) = pairResponseEvent.onNext(response)
}
