
package com.jsjrobotics.testmirror.dataStructures.networking;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ImageThumb {

    @SerializedName("cloudinary_id")
        public Object cloudinaryId;
    @SerializedName("cloudinary_version")
        public Object cloudinaryVersion;
    @SerializedName("image_type")
        public Object imageType;
    @SerializedName("url")
        public String url;

}
