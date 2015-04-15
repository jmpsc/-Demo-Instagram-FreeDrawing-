package com.jc.tpdemo.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.wearable.DataMap;
import com.jc.tpdemo.R;
import com.jc.tpdemo.TPApplication;
import com.jc.tpdemo.adapters.InstagramImagesAdapter;
import com.jc.tpdemo.data.events.DataPostStatusEvent;
import com.jc.tpdemo.data.models.TagQueryResult;
import com.jc.tpdemo.data.services.InstagramService;
import com.jc.tpdemo.data.utils.InstagramModelUtils;
import com.jc.tpdemo.listeners.EndlessScrollListener;
import com.jc.tpdemo.models.InstagramListItem;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Jorge on 11-04-2015.
 */
public class InstagramListFragment extends GoogleAPIClientFragment<InstagramListItem> {
    //TODO should not be here
    private static final String clientId = "fffbf01e91954193a6a6698825079a9c";
    private static final String TAG = "instagramlistfragment";
    public static final String LOAD_IMAGE_URI = "/show";
    private static final String IMAGE_URL_KEY = "key_url";
    private static final String TEXT_KEY = "key_text";
    private static final int NO_MESSAGE = 0;
    private static final int NUM_ELEMENTS_PER_PAGE = 20;

    private SearchView mSearchView;
    private TextView mMessageTextView;
    private ListView mListView;
    private InstagramImagesAdapter mAdapter;

    private String nextMaxId;
    private String lastQuery;

    @Inject
    public InstagramService service;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((TPApplication) getActivity().getApplication()).getApplicationGraph().inject(this);
    }

    //Hook method which translates {@code InstagramListItem} into the provided DataMap
    @Override
    void translateToDataMap(InstagramListItem instagramListItem, DataMap dataMap) {
        dataMap.putString(IMAGE_URL_KEY, instagramListItem.imageURL);
        dataMap.putString(TEXT_KEY, instagramListItem.username);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_instagram, container, false);

        mSearchView = (SearchView) layout.findViewById(R.id.search);
        mListView = (ListView) layout.findViewById(R.id.list);
        mMessageTextView = (TextView) layout.findViewById(R.id.message);

        mSearchView.setOnQueryTextListener(getQueryTextListener());
        prepareListView();

        return layout;
    }

    /**
     * Prepares the listview with the necessary listeners and with the adapter to list the
     * instagram images info
     */
    private void prepareListView() {
        mAdapter = new InstagramImagesAdapter(getActivity(), R.layout.instagram_list_item,
                new ArrayList<InstagramListItem>());
        mListView.setOnScrollListener(getScrollListener());
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(getSendToSmartwatchListener());
    }

    private AdapterView.OnItemClickListener getSendToSmartwatchListener() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                sendToSmartwatch(mAdapter.getItem(position));
            }
        };
    }

    //
    protected void sendToSmartwatch(InstagramListItem item) {
        sendDataToWearable(item, LOAD_IMAGE_URI);
    }

    @Override
    public void onResume() {
        super.onResume();
        bus.register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        bus.unregister(this);
    }

    private EndlessScrollListener getScrollListener() {
        return new EndlessScrollListener(0) {

            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                loadMoreItems();
            }
        };
    }

    private SearchView.OnQueryTextListener getQueryTextListener() {
        return new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                submitSearch(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        };
    }

    private void submitSearch(String query) {
        nextMaxId = null;
        lastQuery = query;
        service.getMediaForHashtag(query, clientId, NUM_ELEMENTS_PER_PAGE, new Callback<TagQueryResult>() {
            @Override
            public void success(TagQueryResult tagQueryResult, Response response) {
                nextMaxId = tagQueryResult.pagination.nextMaxId;
                displayNewList(InstagramModelUtils.extractImagesFromResult(tagQueryResult));
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(TAG, "Failed to fetch images:" + error.getMessage());
                showMessage(R.string.error_communicating_instagram);
            }
        });
    }

    private void loadMoreItems() {
        service.getMediaForHashtagStartingAtId(lastQuery, clientId, NUM_ELEMENTS_PER_PAGE, nextMaxId, new Callback<TagQueryResult>() {
            @Override
            public void success(TagQueryResult tagQueryResult, Response response) {
                nextMaxId = tagQueryResult.pagination.nextMaxId;
                updateList(InstagramModelUtils.extractImagesFromResult(tagQueryResult));
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(TAG, "Failed to fetch images:" + error.getMessage());
                Toast.makeText(getActivity(), R.string.error_communicating_instagram, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * @param resId id of the string to be displayed. If 0, hides the current message
     */
    private void showMessage(int resId) {
        if (resId == 0) {
            mMessageTextView.setVisibility(View.INVISIBLE);
        } else {
            mMessageTextView.setVisibility(View.VISIBLE);
            mMessageTextView.setText(resId);
        }
    }

    /**
     * Replace adapter's items with the provided {@code newItems}
     *
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
     *
     * @param newItems
     */
    private void updateList(List<InstagramListItem> newItems) {
        mAdapter.addAll(newItems);
        mAdapter.notifyDataSetChanged();
    }

    /**
     * Method responsible to alert the user of the success or insuccess of the Image Save Event.
     * Here could also be added a ProgressBar or something similar, as the operation might take
     * a while in some devices.
     *
     * @param event
     */
    @Subscribe
    public void dataDeliveredAnswer(DataPostStatusEvent event) {
        if (event.dataDelivered)
            Toast.makeText(getActivity(), R.string.data_delivered, Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(getActivity(), R.string.data_not_delivered, Toast.LENGTH_SHORT).show();
    }
}
