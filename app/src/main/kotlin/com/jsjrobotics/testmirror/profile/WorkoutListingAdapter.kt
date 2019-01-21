package com.jsjrobotics.testmirror.profile

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.jsjrobotics.testmirror.R
import com.jsjrobotics.testmirror.dataStructures.networking.responses.ListingResponseData

class WorkoutListingAdapter(val listings : List<ListingResponseData>) : RecyclerView.Adapter<WorkoutListingViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutListingViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return WorkoutListingViewHolder(inflater.inflate(R.layout.view_holder_workout_listing, parent, false))
    }

    override fun getItemCount() = listings.size

    override fun onBindViewHolder(viewHolder: WorkoutListingViewHolder, position: Int) {
        viewHolder.bind(listings[position])
    }

}
