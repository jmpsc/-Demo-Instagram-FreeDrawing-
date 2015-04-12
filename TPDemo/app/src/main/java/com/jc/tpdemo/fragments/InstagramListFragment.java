package com.jc.tpdemo.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SearchView;

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

    private IDrawerManager mCallback;
    private SearchView mSearchView;
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
        mListView.setOnScrollListener(new EndlessScrollListener(){

            @Override
            public void onLoadMore(int page, int totalItemsCount) {
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
                //why was there a failure?
            }
        });
    }

    private void loadMoreItems(){
        service.getMediaForHashtagStartingAtId(lastQuery, clientId, 5, nextMaxId, new Callback<TagQueryResult>() {
            @Override
            public void success(TagQueryResult tagQueryResult, Response response) {
                nextMaxId = tagQueryResult.pagination.nextMaxId;
                updateList(InstagramModelUtils.extractImagesFromResult(tagQueryResult));
            }

            @Override
            public void failure(RetrofitError error) {
                //why was there a failure?
            }
        });
    }

    private void
    displayNewList(List<InstagramListItem> newItems) {
        mAdapter.clear();
        mAdapter.addAll(newItems);
        mAdapter.notifyDataSetChanged();
    }

    private void updateList(List<InstagramListItem> newItems) {
        mAdapter.addAll(newItems);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mCallback = (IDrawerManager) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement IDrawerManager");
        }

    }
}
