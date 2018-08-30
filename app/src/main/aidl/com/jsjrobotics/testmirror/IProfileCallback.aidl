// IProfileCallback.aidl
package com.jsjrobotics.testmirror;

// Declare any non-default types here with import statements

oneway interface IProfileCallback {
    void update(String value);
}
