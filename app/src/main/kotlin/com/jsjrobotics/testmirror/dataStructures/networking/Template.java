
package com.jsjrobotics.testmirror.dataStructures.networking;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Template {

    @SerializedName("modified")
        public String modified;
    @SerializedName("variant_label")
        public String variantLabel;
    @SerializedName("skill_avg")
        public Double skillAvg;
    @SerializedName("trainer")
        public Trainer trainer;
    @SerializedName("duration")
        public Integer duration;
    @SerializedName("image_center")
        public ImageCenter imageCenter;
    @SerializedName("channel")
        public Channel channel;
    @SerializedName("last_broadcast")
        public LastBroadcast lastBroadcast;
    @SerializedName("name")
        public String name;
    @SerializedName("featured")
        public Boolean featured;
    @SerializedName("rating_avg")
        public Double ratingAvg;
    @SerializedName("image_thumb")
        public ImageThumb imageThumb;
    @SerializedName("is_demo")
        public Boolean isDemo;
    @SerializedName("is_competitive")
        public Boolean isCompetitive;
    @SerializedName("pn_channel")
        public PnChannel pnChannel;
    @SerializedName("uuid")
        public String uuid;
    @SerializedName("image")
        public Object image;
    @SerializedName("equipment")
        public List<Equipment> equipment = null;
    @SerializedName("image_hero")
        public ImageHero_ imageHero;

}
