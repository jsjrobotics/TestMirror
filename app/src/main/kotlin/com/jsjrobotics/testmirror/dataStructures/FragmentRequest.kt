package com.jsjrobotics.testmirror.dataStructures

import com.jsjrobotics.testmirror.FragmentId

data class FragmentRequest(
        val fragmentId: FragmentId,
        val addToBackstack: Boolean,
        val backstackTag: String? = fragmentId.tag(),
        val clearBackStack : Boolean = false)