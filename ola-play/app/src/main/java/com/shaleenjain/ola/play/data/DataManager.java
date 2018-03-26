package com.shaleenjain.ola.play.data;


import com.shaleenjain.ola.play.data.local.DatabaseHelper;
import com.shaleenjain.ola.play.data.local.PreferencesHelper;
import com.shaleenjain.ola.play.data.model.Playlist;
import com.shaleenjain.ola.play.data.model.Track;
import com.shaleenjain.ola.play.data.remote.APIService;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

@Singleton
public class DataManager {

    private final APIService mAPIservice;
    private final DatabaseHelper mDatabaseHelper;
    private final PreferencesHelper mPreferencesHelper;

    @Inject
    public DataManager(APIService ribotsService, PreferencesHelper preferencesHelper,
                       DatabaseHelper databaseHelper) {
        mAPIservice = ribotsService;
        mPreferencesHelper = preferencesHelper;
        mDatabaseHelper = databaseHelper;
    }

    public PreferencesHelper getPreferencesHelper() {
        return mPreferencesHelper;
    }

    public Observable<Track> syncTracks() {
        return mAPIservice.getTracks()
                .concatMap(new Function<List<Track>, ObservableSource<? extends Track>>() {
                    @Override
                    public ObservableSource<? extends Track> apply(@NonNull List<Track> tracks)
                            throws Exception {
                        return mDatabaseHelper.setRibots(tracks);
                    }
                });
    }

    public Observable<List<Track>> getTracks() {
        return mDatabaseHelper.getTracks().distinct();
    }

    public Observable<List<Track>> getTracks(String query) {
        return mDatabaseHelper.getTracks(query).distinct();
    }

    public Observable<Track> getTrack(String mediaid) {
        return mDatabaseHelper.getTrack(mediaid);
    }

    public Observable<Playlist> getPlaylist() {
        return mDatabaseHelper.getPlaylist();
    }

    public void addToPlaylist(String id) {
        mDatabaseHelper.addToPlaylist(id);
    }
}
