// IWebSocket.aidl
package com.jsjrobotics.testmirror;
import android.net.nsd.NsdServiceInfo;
import com.jsjrobotics.testmirror.service.RemoteMirrorState;
// Declare any non-default types here with import statements

interface IWebSocket {
    void connectToClient(in NsdServiceInfo info);
    void sendPairingCode(in NsdServiceInfo info, String code);
    void sendScreenRequest(String screenName);
    void sendIdentifyRequest(in NsdServiceInfo info);
    Map getConnectedMirrors();
}
