package com.jsjrobotics.testmirror.profile

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.jsjrobotics.testmirror.R
import com.jsjrobotics.testmirror.dataStructures.networking.responses.ListingResponseData
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class WorkoutListingAdapter(val listings : List<ListingResponseData>) : RecyclerView.Adapter<WorkoutListingViewHolder>() {

    private val viewClicked : PublishSubject<ListingResponseData> = PublishSubject.create()
    val onViewClicked : Observable<ListingResponseData> = viewClicked

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutListingViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.view_holder_workout_listing, parent, false)
        return WorkoutListingViewHolder(view)
    }

    override fun getItemCount() = listings.size

    override fun onBindViewHolder(viewHolder: WorkoutListingViewHolder, position: Int) {
        val data = listings[position]
        viewHolder.bind(data, { viewClicked.onNext(data)})
    }

}
