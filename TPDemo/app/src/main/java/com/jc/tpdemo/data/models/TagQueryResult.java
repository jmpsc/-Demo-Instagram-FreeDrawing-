package com.jc.tpdemo.data.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Jorge on 11-04-2015.
 */
public class TagQueryResult {
    Pagination pagination;
    List<Media> data;

    private class Pagination {
        @SerializedName("next_max_id")
        String nextMaxId;
        @SerializedName("next_url")
        String nextUrl;
    }

    private class Media {
        @SerializedName("create_time")
        String createTime;
        ImagesInfo images;
    }

    private class User{
        String userName;
    }

    private class ImagesInfo {
        @SerializedName("low_resolution")
        LowResolutionImage lowResolution;
    }

    private class LowResolutionImage {
        String url;
    }
}
