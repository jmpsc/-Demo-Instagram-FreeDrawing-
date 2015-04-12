package com.jc.tpdemo.data.utils;

import com.jc.tpdemo.data.models.TagQueryResult;
import com.jc.tpdemo.models.InstagramListItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jorge on 11-04-2015.
 */
public class InstagramModelUtils {
    public static List<InstagramListItem> extractImagesFromResult(TagQueryResult queryResult) {
        ArrayList<InstagramListItem> items = new ArrayList<>();

        for(TagQueryResult.Media m : queryResult.data){
            items.add(new InstagramListItem(m.getImageURL(), m.getUsername(), m.createTime));
        }

        return items;
    }
}
