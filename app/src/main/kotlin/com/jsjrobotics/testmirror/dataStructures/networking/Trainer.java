
package com.jsjrobotics.testmirror.dataStructures.networking;

import com.google.gson.annotations.SerializedName;

public class Trainer {

    @SerializedName("tag")
        public Object tag;
    @SerializedName("gender")
        public String gender;
    @SerializedName("name")
        public String name;
    @SerializedName("uuid")
        public String uuid;
    @SerializedName("image")
        public Image image;
    @SerializedName("age")
        public Object age;
    @SerializedName("bio")
        public Object bio;
    @SerializedName("image_hero")
        public ImageHero imageHero;
    @SerializedName("display_name")
        public String displayName;
    @SerializedName("email")
        public String email;
    @SerializedName("location")
        public Object location;

}
