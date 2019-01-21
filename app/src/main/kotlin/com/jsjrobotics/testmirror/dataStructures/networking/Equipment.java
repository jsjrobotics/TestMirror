
package com.jsjrobotics.testmirror.dataStructures.networking;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Equipment {

    @SerializedName("uuid")
        public String uuid;
    @SerializedName("name")
        public String name;
    @SerializedName("image")
        public Image_ image;

}
