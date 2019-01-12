// IWebSocket.aidl
package com.jsjrobotics.testmirror;
// Declare any non-default types here with import statements

interface IWebSocket {
    void connectToClient(String ipAddress);
    void sendPairingCode(String code);
    void sendScreenRequest(String screenName);
    void sendIdentifyRequest();
}
