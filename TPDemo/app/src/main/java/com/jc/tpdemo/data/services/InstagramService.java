package com.jc.tpdemo.data.services;

import com.jc.tpdemo.data.models.TagQueryResult;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by Jorge on 11-04-2015.
 */

public interface InstagramService {
    ///tags/evangelion/media/recent?count=1&client_id=fffbf01e91954193a6a6698825079a9c&max_tag_id=960986999673443605
    @GET("/tags/{hashtag}/media/recent")
    void getMediaForHashtag(@Path("hashtag") String hashtag,
                                  @Query("client_id") String clientId,
                                  @Query("count") int count, Callback<TagQueryResult> cb);

    @GET("/tags/{hashtag}/media/recent")
    void getMediaForHashtagStartingAtId(@Path("hashtag") String hashtag,
                                  @Query("client_id") String clientId,
                                  @Query("count") int count,
                                  @Query("max_tag_id") String tagId, Callback<TagQueryResult> cb);
}
