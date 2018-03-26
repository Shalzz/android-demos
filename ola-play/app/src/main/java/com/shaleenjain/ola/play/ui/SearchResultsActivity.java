package com.shaleenjain.ola.play.ui;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.shaleenjain.ola.play.R;
import com.shaleenjain.ola.play.data.DataManager;
import com.shaleenjain.ola.play.data.model.Track;
import com.shaleenjain.ola.play.utils.RxUtil;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class SearchResultsActivity extends BaseActivity implements TrackAdapter.Callback{

    @Inject
    DataManager mDataManager;

    @Inject
    TrackAdapter mTrackAdapter;
    private Disposable mDisposable;

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @BindView((R.id.toolbar))
    Toolbar mToolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        activityComponent().inject(this);
        ButterKnife.bind(this);

        initializeToolbar();
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        mRecyclerView.setAdapter(mTrackAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            loadTracks(query);
        }
    }

    public void loadTracks(String query) {
        RxUtil.dispose(mDisposable);
        mDataManager.getTracks(query)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<List<Track>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        mDisposable = d;
                    }

                    @Override
                    public void onNext(@NonNull List<Track> tracks) {
                        if (tracks.isEmpty()) {
                            showTracksEmpty();
                        } else {
                            showTracks(tracks);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Timber.e(e, "There was an error loading the ribots.");
                        showError();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void showTracks(List<Track> tracks) {
        mTrackAdapter.setRibots(tracks);
        mTrackAdapter.notifyDataSetChanged();
    }

    public void showError() {
        Toast.makeText(this, "Unexpected error loading tracks!", Toast.LENGTH_LONG).show();
    }

    public void showTracksEmpty() {
        mTrackAdapter.setRibots(Collections.<Track>emptyList());
        mTrackAdapter.notifyDataSetChanged();
        Toast.makeText(this, "No matching track found " , Toast.LENGTH_LONG).show();
    }

    @Override
    public void onItemClicked() {
        finish();
    }

}
