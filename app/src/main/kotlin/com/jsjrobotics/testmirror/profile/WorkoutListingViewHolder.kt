package com.jsjrobotics.testmirror.profile

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.jsjrobotics.testmirror.R
import com.jsjrobotics.testmirror.dataStructures.networking.responses.ListingResponseData

class WorkoutListingViewHolder(view: View) : RecyclerView.ViewHolder(view){
    private var type: TextView = itemView.findViewById(R.id.type)
    private var trainerName: TextView = itemView.findViewById(R.id.trainer_name)

    fun bind(listingResponseData: ListingResponseData) {
        type.text = listingResponseData.template.channel.name
        trainerName.text = listingResponseData.template.trainer.displayName

    }

}
