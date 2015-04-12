package com.jc.tpdemo.data.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Jorge on 11-04-2015.
 */
public class TagQueryResult {
    public Pagination pagination;
    public List<Media> data;

    public class Pagination {
        @SerializedName("next_max_id")
        public String nextMaxId;
        @SerializedName("next_url")
        public String nextUrl;
    }

    public class Media {
        @SerializedName("created_time")
        public String createTime;
        public ImagesInfo images;
        public User user;

        public String getUsername() {
            return user.username;
        }

        public String getImageURL(){
            return images.lowResolution.url;
        }
    }

    public class User{
        public String username;
    }

    public class ImagesInfo {
        @SerializedName("low_resolution")
        public LowResolutionImage lowResolution;
    }

    public class LowResolutionImage {
        public String url;
    }
}
