package com.jsjrobotics.testmirror.profile

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import com.jsjrobotics.testmirror.DefaultView
import com.jsjrobotics.testmirror.R
import com.jsjrobotics.testmirror.dataStructures.networking.responses.ListingResponseData
import com.jsjrobotics.testmirror.runOnUiThread
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class ProfileView @Inject constructor() : DefaultView(){
    override fun getContext(): Context = rootXml.context

    lateinit var rootXml: ViewGroup; private set
    private lateinit var refreshButton: Button
    private lateinit var workoutListings: RecyclerView
    private val refreshClick : PublishSubject<Unit> = PublishSubject.create()
    val onRefreshClick: Observable<Unit> = refreshClick

    fun init(inflater: LayoutInflater, container: ViewGroup) {
        rootXml = inflater.inflate(R.layout.fragment_profile, container, false) as ViewGroup
        workoutListings = rootXml.findViewById(R.id.workout_listings)
        refreshButton = rootXml.findViewById(R.id.refresh)
        workoutListings.layoutManager = LinearLayoutManager(rootXml.context)
        refreshButton.setOnClickListener {
            refreshClick.onNext(Unit)
        }
    }

    fun showListings(data: List<ListingResponseData>) {
        workoutListings.adapter = WorkoutListingAdapter(data)

    }

    fun disableRefresh() {
        runOnUiThread {
            refreshButton.isEnabled = false
        }
    }

    fun enableRefresh() {
        runOnUiThread {
            refreshButton.isEnabled = true
        }
    }

}