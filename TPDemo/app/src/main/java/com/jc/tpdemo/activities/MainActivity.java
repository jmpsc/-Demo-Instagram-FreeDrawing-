package com.jc.tpdemo.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.res.Configuration;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.jc.tpdemo.R;
import com.jc.tpdemo.adapters.DrawerArrayAdapter;
import com.jc.tpdemo.fragments.FingerPaintingFragment;
import com.jc.tpdemo.fragments.GraphicsFragment;
import com.jc.tpdemo.fragments.InstagramListFragment;
import com.jc.tpdemo.models.DrawerEntry;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity implements IDrawerManager {
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private CharSequence mTitle;
    private String[] mDrawerTitles;
    private ArrayList<DrawerEntry> menuEntries;
    private String  mDrawerTitle;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawerTitles = getResources().getStringArray(R.array.drawer_entries);

        menuEntries = getMenuEntries();

        // Set the adapter for the list view
        mDrawerList.setAdapter(new DrawerArrayAdapter(this,
                R.layout.drawer_list_item, menuEntries));
        // Set the list's click listener
        mDrawerList.setOnItemClickListener(getDrawerOnClickListener());

        mToolbar = (Toolbar) findViewById(R.id.custom_toolbar);
        setSupportActionBar(mToolbar);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.drawer_open, R.string.drawer_close) {

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mTitle);
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle(mDrawerTitle);
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeButtonEnabled(true);
    }

    private ArrayList<DrawerEntry> getMenuEntries() {
        ArrayList<DrawerEntry> list = new ArrayList<>();

        list.add(new DrawerEntry("Hash Search", R.drawable.ic_launcher));
        list.add(new DrawerEntry("Draw", R.drawable.ic_launcher));
        list.add(new DrawerEntry("Extra", R.drawable.ic_launcher));

        return list;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        if(mDrawerLayout.isDrawerOpen(Gravity.START|Gravity.LEFT)){
            mDrawerLayout.closeDrawers();
            return;
        }
        super.onBackPressed();
    }

    public AdapterView.OnItemClickListener getDrawerOnClickListener() {
        return new AdapterView.OnItemClickListener() {
            int previous;

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Fragment fragment = null;
                if(previous == 0) {
                    mToolbar.setVisibility(View.VISIBLE);
                }
                switch (position) {
                    case 0:
                        fragment = new InstagramListFragment();
                        mToolbar.setVisibility(View.GONE);
                        break;
                    case 1:
                        fragment = new FingerPaintingFragment();
                        break;
                    case 2:
                        fragment = new GraphicsFragment();
                        break;
                    default:
                        break;
                }

                // Insert the fragment by replacing any existing fragment
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame, fragment)
                        .commit();

                // Highlight the selected item, update the title, and close the drawer
                mDrawerList.setItemChecked(position, true);
                setTitle(mDrawerTitles[position]);
                mDrawerLayout.closeDrawer(mDrawerList);
            }
        };
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(title);
    }

    @Override
    public boolean closeDrawer() {
        mDrawerLayout.closeDrawer(Gravity.START);
        return true;
    }

    @Override
    public boolean openDrawer() {
        mDrawerLayout.openDrawer(Gravity.START);
        return true;
    }
}
