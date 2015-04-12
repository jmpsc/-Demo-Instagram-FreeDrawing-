package com.jc.tpdemo.data.utils;

import com.jc.tpdemo.data.models.TagQueryResult;
import com.jc.tpdemo.models.InstagramListItem;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jorge on 11-04-2015.
 */
public class InstagramModelUtils {
    public static List<InstagramListItem> extractImagesFromResult(TagQueryResult queryResult) {
        ArrayList<InstagramListItem> items = new ArrayList<>();

        for(TagQueryResult.Media m : queryResult.data){
            String date = getDate(m.createTime);

            items.add(new InstagramListItem(m.getImageURL(), m.getUsername(), date));
        }

        return items;
    }

    /**
     *
     * @param timeStamp timestamp in seconds
     * @return date representation
     */
    private static String getDate(String timeStamp){
        long timestampNorm = Long.parseLong(timeStamp) * 1000;
        try{
            DateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
            Date netDate = (new Date(timestampNorm));
            return sdf.format(netDate);
        }
        catch(Exception ex){
            return "xx";
        }
    }

}
