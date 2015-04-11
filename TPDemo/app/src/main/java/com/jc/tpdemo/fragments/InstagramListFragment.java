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
import com.jc.tpdemo.data.models.TagQueryResult;
import com.jc.tpdemo.data.services.InstagramService;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Jorge on 11-04-2015.
 */
public class InstagramListFragment extends android.app.Fragment {

    private IDrawerManager mCallback;
    private SearchView searchView;
    private InstagramService service;
    private String clientId = "fffbf01e91954193a6a6698825079a9c";
    private ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_instagram, container, false);

        searchView = (SearchView) layout.findViewById(R.id.search);
        listView = (ListView) layout.findViewById(R.id.list);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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

        service = getInstagramService();

        return layout;
    }

    private InstagramService getInstagramService() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("https://api.instagram.com/v1")
                .build();

        return restAdapter.create(InstagramService.class);
    }

    private void submitSearch(String query) {
        service.getMediaForHashtag(query, clientId, 5, new Callback<TagQueryResult>() {
            @Override
            public void success(TagQueryResult tagQueryResult, Response response) {
                updateList(tagQueryResult);
            }

            @Override
            public void failure(RetrofitError error) {
                //why was there a failure?
            }
        });
    }

    private void updateList(TagQueryResult tagQueryResult) {

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
