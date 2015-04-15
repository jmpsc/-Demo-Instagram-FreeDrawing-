package com.jc.tpdemo.data.services;

import com.jc.tpdemo.data.models.TagQueryResult;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by Jorge on 11-04-2015.
 */

/**
 * Instagram API service. To be used with Retrofit or other RestAPI built through
 * interfaces/annotation. This class exposes two GET methods related to search by HashTag.
 */
public interface InstagramService {
    /**
     * Get media related to the provided {@code hashtag}. There is no
     * distinction made between pictures and video.
     * @param hashtag search keyword
     * @param clientId of the application
     * @param count number of elements per page
     * @param cb callback to deliver the request's result
     */
    @GET("/tags/{hashtag}/media/recent")
    void getMediaForHashtag(@Path("hashtag") String hashtag,
                                  @Query("client_id") String clientId,
                                  @Query("count") int count, Callback<TagQueryResult> cb);

    /**
     * Get media related to the provided {@code hashtag}, starting from the element with the
     * id {@code tagId}.
     * @param hashtag search keyword
     * @param clientId of the application
     * @param count number of elements per page
     * @param tagId id of the first element of the next media list (pagination)
     * @param cb callback to deliver the request's result
     */
    @GET("/tags/{hashtag}/media/recent")
    void getMediaForHashtagStartingAtId(@Path("hashtag") String hashtag,
                                  @Query("client_id") String clientId,
                                  @Query("count") int count,
                                  @Query("max_tag_id") String tagId, Callback<TagQueryResult> cb);
}
