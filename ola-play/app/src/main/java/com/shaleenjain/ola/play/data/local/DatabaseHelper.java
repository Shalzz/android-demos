package com.shaleenjain.ola.play.data.local;

import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.VisibleForTesting;

import com.shaleenjain.ola.play.data.model.Track;
import com.squareup.sqlbrite2.BriteDatabase;
import com.squareup.sqlbrite2.SqlBrite;
import com.squareup.sqldelight.SqlDelightStatement;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;

@Singleton
public class DatabaseHelper {

    private final BriteDatabase mDb;

    @Inject
    public DatabaseHelper(DbOpenHelper dbOpenHelper) {
        this(dbOpenHelper, Schedulers.io());
    }

    @VisibleForTesting
    public DatabaseHelper(DbOpenHelper dbOpenHelper, Scheduler scheduler) {
        SqlBrite.Builder briteBuilder = new SqlBrite.Builder();
        mDb = briteBuilder.build().wrapDatabaseHelper(dbOpenHelper, scheduler);
    }

    public BriteDatabase getBriteDb() {
        return mDb;
    }

    public Observable<Track> setRibots(final Collection<Track> newTracks) {
        return Observable.create(new ObservableOnSubscribe<Track>() {
            @Override
            public void subscribe(ObservableEmitter<Track> emitter) throws Exception {
                if (emitter.isDisposed()) return;
                BriteDatabase.Transaction transaction = mDb.newTransaction();
                try {
                    mDb.delete(Track.TABLE_NAME, null);
                    for (Track track : newTracks) {
                        long result = mDb.insert(Track.TABLE_NAME,
                                Track.FACTORY.marshal(track).asContentValues(),
                                SQLiteDatabase.CONFLICT_REPLACE);
                        if (result >= 0) emitter.onNext(track);
                    }
                    transaction.markSuccessful();
                    emitter.onComplete();
                } finally {
                    transaction.end();
                }
            }
        });
    }

    public Observable<List<Track>> getTracks() {
        return mDb.createQuery(Track.FACTORY.selectALL().tables,
                Track.FACTORY.selectALL().statement)
                    .mapToList(Track.SELECT_ALL_MAPPER::map);
    }

    public Observable<List<Track>> getTracks(String query) {
        SqlDelightStatement stm = Track.FACTORY.select_like_name("%"+query+"%");
        return mDb.createQuery(stm.tables,
                stm.statement,
                stm.args)
                .mapToList(Track.FACTORY.select_like_nameMapper()::map);
    }

    public Observable<Track> getTrack(String mediaid) {
        SqlDelightStatement stm = Track.FACTORY.select_by_id(mediaid);
        return mDb.createQuery(stm.tables,
                stm.statement,
                stm.args)
                .mapToOne(Track.FACTORY.select_by_idMapper()::map);
    }
}
