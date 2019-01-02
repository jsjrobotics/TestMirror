package com.jsjrobotics.testmirror.profile

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import com.jsjrobotics.testmirror.DefaultView
import com.jsjrobotics.testmirror.R
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class ProfileView @Inject constructor() : DefaultView(){
    override fun getContext(): Context = rootXml.context

    lateinit var rootXml: ViewGroup; private set
    lateinit var refreshButton: Button; private set

    private val refreshClick : PublishSubject<Unit> = PublishSubject.create()
    val onRefreshClick: Observable<Unit> = refreshClick

    fun init(inflater: LayoutInflater, container: ViewGroup) {
        rootXml = inflater.inflate(R.layout.fragment_profile, container, false) as ViewGroup
        refreshButton = rootXml.findViewById(R.id.refresh)
        refreshButton.setOnClickListener {
            refreshClick.onNext(Unit)
        }
    }

}