// IDataPersistence.aidl
package com.jsjrobotics.testmirror;
import com.jsjrobotics.testmirror.IProfileCallback;
// Declare any non-default types here with import statements

interface IDataPersistence {
    String getProfileData(String profile);
    void registerCallback(IProfileCallback callback);
    void unregisterCallback(IProfileCallback callback);
}
