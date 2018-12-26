package com.jsjrobotics.testmirror.dataStructures

import android.net.nsd.NsdServiceInfo
import com.jsjrobotics.testmirror.dataStructures.networking.MirrorDataResponse

data class ResolvedMirrorData(val serviceInfo: NsdServiceInfo, val mirrorData: MirrorDataResponse? = null)
