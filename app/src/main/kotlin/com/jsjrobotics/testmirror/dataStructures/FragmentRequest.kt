package com.jsjrobotics.testmirror.dataStructures

import android.os.Bundle
import com.jsjrobotics.testmirror.FragmentId

sealed class FragmentRequest(val fragmentId: FragmentId)

class RemoveFragment(fragmentId: FragmentId) : FragmentRequest(fragmentId)

class AddFragment(fragmentId: FragmentId,
                  val addToBackstack: Boolean,
                  val containerId: Int,
                  val backstackTag: String? = fragmentId.tag(),
                  val clearBackStack : Boolean = false,
                  val extra: Bundle? = null) : FragmentRequest(fragmentId)