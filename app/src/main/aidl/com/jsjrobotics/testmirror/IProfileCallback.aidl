// IProfileCallback.aidl
package com.jsjrobotics.testmirror;
import com.jsjrobotics.testmirror.dataStructures.Account;
// Declare any non-default types here with import statements

oneway interface IProfileCallback {
    void update(in Account account);
    void loginSuccess(in Account account);
    void loginFailure();
    void signUpFailure(in String error);
    void signUpSuccess();
}
