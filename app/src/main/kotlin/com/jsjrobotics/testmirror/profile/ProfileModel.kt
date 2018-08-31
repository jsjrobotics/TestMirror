package com.jsjrobotics.testmirror.profile

import com.jsjrobotics.testmirror.Application
import com.jsjrobotics.testmirror.DefaultProfileCallback
import com.jsjrobotics.testmirror.IProfileCallback
import com.jsjrobotics.testmirror.dataStructures.Account
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProfileModel @Inject constructor(val application: Application)  {
    var currentAccount: Account? = null ; private set
    private val profileUpdated : PublishSubject<Unit> = PublishSubject.create()
    val onProfileUpdated : Observable<Unit> = profileUpdated

    init {
        application.dataPersistenceService?.registerCallback(buildUpdateCallback())
    }

    private fun buildUpdateCallback(): IProfileCallback {
        return object : DefaultProfileCallback() {
            override fun update(update: Account?) {
                update?.let { accountUpdate ->
                    currentAccount?.let { current ->
                        if (current.userEmail == accountUpdate.userEmail) {
                            currentAccount = update
                            profileUpdated.onNext(Unit)
                        }
                    }
                }
            }
        }
    }

    fun setAccount(account: Account) {
        currentAccount = account
    }
}