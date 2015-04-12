package com.jc.tpdemo.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jc.tpdemo.R;
import com.jc.tpdemo.models.InstagramListItem;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Jorge on 11-04-2015.
 */
public class InstagramImagesAdapter extends ArrayAdapter<InstagramListItem> {
    private final ColorDrawable defaultGreyDrawable;
    Context context;
    List<InstagramListItem> items;
    int layoutResID;

    public InstagramImagesAdapter(Context context, int layoutResourceID, List<InstagramListItem> listItems) {
        super(context, layoutResourceID, listItems);
        this.context = context;
        this.items = listItems;
        this.layoutResID = layoutResourceID;

        defaultGreyDrawable = new ColorDrawable(Color.GRAY);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        View view = convertView;

        if (view == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            viewHolder = new ViewHolder();

            view = inflater.inflate(layoutResID, parent, false);
            viewHolder.uploadDate = (TextView) view.findViewById(R.id.submission_date);
            viewHolder.username = (TextView) view
                    .findViewById(R.id.username);
            viewHolder.picture = (ImageView) view.findViewById(R.id.picture);

            view.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        InstagramListItem item = items.get(position);

        Picasso.with(getContext()).load(item.imageURL).placeholder(defaultGreyDrawable).into(viewHolder.picture);
        viewHolder.username.setText(item.username);
        viewHolder.uploadDate.setText(item.uploadDate);

        return view;
    }

    private static class ViewHolder {
        TextView username;
        ImageView picture;
        TextView uploadDate;
    }
}
