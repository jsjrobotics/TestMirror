// IBackend.aidl
package com.jsjrobotics.testmirror;
import com.jsjrobotics.testmirror.IProfileCallback;
import com.jsjrobotics.testmirror.dataStructures.LoginData;
import com.jsjrobotics.testmirror.dataStructures.SignUpData;
import com.jsjrobotics.testmirror.dataStructures.UpdateInfoData;
import com.jsjrobotics.testmirror.dataStructures.Account;


// Declare any non-default types here with import statements

interface IBackend {
    void getProfileData(String userEmail);
    void registerCallback(IProfileCallback callback);
    void unregisterCallback(IProfileCallback callback);
    void attemptLogin(IProfileCallback callback, in LoginData data);
    void attemptSignup(IProfileCallback callback, in SignUpData data);
    void attemptUpdateInfo(IProfileCallback callback, in Account account, in UpdateInfoData data);
    void getOnDemandWorkout();
}
