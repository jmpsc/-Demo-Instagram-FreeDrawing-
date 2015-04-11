package com.jc.tpdemo.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.jc.tpdemo.R;
import com.jc.tpdemo.activities.MainActivity;
import com.jc.tpdemo.models.DrawerEntry;

import java.util.List;

/**
 * Created by Jorge on 11-04-2015.
 */
public class DrawerArrayAdapter extends ArrayAdapter<DrawerEntry> {
    Context context;
    List<DrawerEntry> drawerItemList;
    int layoutResID;

    public DrawerArrayAdapter(Context context, int layoutResourceID, List<DrawerEntry> listItems) {
        super(context, layoutResourceID, listItems);
        this.context = context;
        this.drawerItemList = listItems;
        this.layoutResID = layoutResourceID;

    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        View view = convertView;

        if (view == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            viewHolder = new ViewHolder();

            view = inflater.inflate(layoutResID, parent, false);
            viewHolder.title = (TextView) view
                    .findViewById(R.id.title);
            viewHolder.icon = (ImageView) view.findViewById(R.id.icon);

            view.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) view.getTag();

        }

        DrawerEntry item = this.drawerItemList.get(position);

        viewHolder.icon.setImageDrawable(view.getResources().getDrawable(
                item.iconResourceId));
        viewHolder.title.setText(item.title);

        return view;
    }

    private static class ViewHolder {
        TextView title;
        ImageView icon;
    }
}
