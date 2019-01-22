// IWebSocket.aidl
package com.jsjrobotics.testmirror;
import android.net.nsd.NsdServiceInfo;
import com.jsjrobotics.testmirror.dataStructures.ResolvedMirrorData;

interface IWebSocket {
    void connectToClient(in ResolvedMirrorData info);
    void sendPairingCode(in NsdServiceInfo info, String code);
    void sendScreenRequest(String screenName);
    void sendIdentifyRequest(in NsdServiceInfo info);
    void sendPreWorkoutRequest(String uuid, String variantId, String workoutData);
    //@Nullable Map<NsdServiceInfo, RemoteMirrorState>
    Map getConnectedMirrors();
}
