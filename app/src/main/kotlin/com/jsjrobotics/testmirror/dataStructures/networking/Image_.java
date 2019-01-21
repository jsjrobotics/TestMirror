
package com.jsjrobotics.testmirror.dataStructures.networking;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Image_ {

    @SerializedName("cloudinary_id")
        public String cloudinaryId;
    @SerializedName("cloudinary_version")
        public String cloudinaryVersion;
    @SerializedName("image_type")
        public String imageType;
    @SerializedName("url")
        public String url;

}
