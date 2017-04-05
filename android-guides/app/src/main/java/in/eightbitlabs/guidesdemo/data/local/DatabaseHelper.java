package in.eightbitlabs.guidesdemo.data.local;

import android.database.sqlite.SQLiteDatabase;

import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import in.eightbitlabs.guidesdemo.data.model.Data;
import rx.Observable;
import rx.schedulers.Schedulers;

@Singleton
public class DatabaseHelper {

    private final BriteDatabase mDb;

    @Inject
    public DatabaseHelper(DbOpenHelper dbOpenHelper) {
        SqlBrite sqlBrite = new SqlBrite.Builder().build();
        mDb = sqlBrite.wrapDatabaseHelper(dbOpenHelper, Schedulers.io());
    }

    public BriteDatabase getBriteDb() {
        return mDb;
    }

    public Observable<Data> setGuidesData(final Collection<Data> newData) {
        return Observable.create(subscriber -> {
            if (subscriber.isUnsubscribed()) return;
            BriteDatabase.Transaction transaction = mDb.newTransaction();
            try {
                mDb.delete(Data.TABLE_NAME, null);
                for (Data data : newData) {
                    long result = mDb.insert(Data.TABLE_NAME,
                            Data.FACTORY.marshal()
                                    .name(data.name())
                                    .startDate(data.startDate())
                                    .endDate(data.endDate())
                                    .url(data.url())
                                    .loginRequired(data.loginRequired())
                                    .objType(data.objType())
                                    .icon(data.icon())
                                    .asContentValues(),
                            SQLiteDatabase.CONFLICT_REPLACE);
                    if (result >= 0) subscriber.onNext(data);
                }
                transaction.markSuccessful();
                subscriber.onCompleted();
            } finally {
                transaction.end();
            }
        });
    }

    public Observable<List<Data>> getGuidesData() {
        return mDb.createQuery(Data.TABLE_NAME, Data.FACTORY.select_all().statement)
                .mapToList(Data.SELECT_ALL_MAPPER::map);
    }

    public Observable<List<Data>> getGuidesInCart() {
        return mDb.createQuery(Data.TABLE_NAME, Data.FACTORY.select_by_cart().statement)
                .mapToList(Data.SELECT_BY_CART_MAPPER::map);
    }

    public int updateData(Data data) {
        return mDb.update(Data.TABLE_NAME,
                Data.FACTORY.marshal(data).asContentValues(),
                Data._ID + " = ?",
                String.valueOf(data._id()));
    }
}
