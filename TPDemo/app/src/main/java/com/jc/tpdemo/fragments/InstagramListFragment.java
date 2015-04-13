package com.jc.tpdemo.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.jc.tpdemo.R;
import com.jc.tpdemo.activities.IDrawerManager;
import com.jc.tpdemo.adapters.InstagramImagesAdapter;
import com.jc.tpdemo.data.models.TagQueryResult;
import com.jc.tpdemo.data.services.InstagramService;
import com.jc.tpdemo.data.utils.InstagramModelUtils;
import com.jc.tpdemo.listeners.EndlessScrollListener;
import com.jc.tpdemo.models.InstagramListItem;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Jorge on 11-04-2015.
 */
public class InstagramListFragment extends android.app.Fragment {

    private static final String TAG = "tpdemo.instagramlistfragment";
    private static final int NO_MESSAGE = 0;
    private IDrawerManager mCallback;
    private SearchView mSearchView;
    private TextView mMessageTextView;
    private ProgressBar mProgressBar;
    private InstagramService service;
    private String clientId = "fffbf01e91954193a6a6698825079a9c";
    private ListView mListView;
    private ArrayList<InstagramListItem> items;
    private String nextMaxId;
    private String lastQuery;
    private InstagramImagesAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_instagram, container, false);

        mSearchView = (SearchView) layout.findViewById(R.id.search);
        mListView = (ListView) layout.findViewById(R.id.list);
        mProgressBar = (ProgressBar) layout.findViewById(R.id.progress_bar);
        mMessageTextView = (TextView) layout.findViewById(R.id.message);

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                submitSearch(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });

        items = new ArrayList<>();

        service = getInstagramService();

        //maybe the end should be notified here?
        mListView.setOnScrollListener(new EndlessScrollListener(0) {

            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                showFooterProgressBar(true);
                loadMoreItems();
            }
        });
        mAdapter = new InstagramImagesAdapter(getActivity(), R.layout.instagram_list_item, items);
        mListView.setAdapter(mAdapter);

        return layout;
    }

    private InstagramService getInstagramService() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("https://api.instagram.com/v1")
                .build();

        return restAdapter.create(InstagramService.class);
    }

    private void submitSearch(String query) {
        nextMaxId = null;
        lastQuery = query;
        service.getMediaForHashtag(query, clientId, 20, new Callback<TagQueryResult>() {
            @Override
            public void success(TagQueryResult tagQueryResult, Response response) {
                nextMaxId = tagQueryResult.pagination.nextMaxId;
                displayNewList(InstagramModelUtils.extractImagesFromResult(tagQueryResult));
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(TAG,"Failed to fetch images:" + error.getMessage());
                showMessage(R.string.error_communicating_instagram);
            }
        });
    }

    private void loadMoreItems() {
        service.getMediaForHashtagStartingAtId(lastQuery, clientId, 5, nextMaxId, new Callback<TagQueryResult>() {
            @Override
            public void success(TagQueryResult tagQueryResult, Response response) {
                nextMaxId = tagQueryResult.pagination.nextMaxId;
                updateList(InstagramModelUtils.extractImagesFromResult(tagQueryResult));
                showFooterProgressBar(false);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(TAG,"Failed to fetch images:" + error.getMessage());
                Toast.makeText(getActivity(), R.string.error_communicating_instagram, Toast.LENGTH_SHORT).show();
                showFooterProgressBar(false);
            }
        });
    }

    /**
     * @param resId id of the string to be displayed. If 0, hides the current message
     */
    private void showMessage(int resId){
         if(resId == 0){
             mMessageTextView.setVisibility(View.INVISIBLE);
         }else{
             mMessageTextView.setVisibility(View.VISIBLE);
             mMessageTextView.setText(resId);
         }
    }

    /**
     *
     * @param show if true, shows the progressbar at the bottom of the screen
     */
    private void showFooterProgressBar(boolean show) {
        //mProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    /** Replace adapter's items with the provided {@code newItems}
     * @param newItems
     */
    private void displayNewList(List<InstagramListItem> newItems) {
        // Hide the message if being displayed
        showMessage(NO_MESSAGE);

        mAdapter.clear();
        mAdapter.addAll(newItems);
        mAdapter.notifyDataSetChanged();
    }

    /**
     * Adds {@code newItems} to the adapter's items
     * @param newItems
     */
    private void updateList(List<InstagramListItem> newItems) {
        mAdapter.addAll(newItems);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // Save instance of the parent activity to control the drawer
        // If the parent activity doesn't implements IDrawerManager, throws ClassCastException
        try {
            mCallback = (IDrawerManager) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement IDrawerManager");
        }
    }
}
