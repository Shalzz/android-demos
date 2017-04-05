package in.eightbitlabs.todo.data.local;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import in.eightbitlabs.todo.data.model.Data;
import rx.Observable;
import rx.Subscriber;

@Singleton
public class DatabaseHelper {

    private final BriteDatabase mDb;

    @Inject
    public DatabaseHelper(DbOpenHelper dbOpenHelper) {
        mDb = SqlBrite.create().wrapDatabaseHelper(dbOpenHelper);
    }

    public BriteDatabase getBriteDb() {
        return mDb;
    }

    public Observable<Data> setTasks(final Collection<Data> tasks) {
        return Observable.create(new Observable.OnSubscribe<Data>() {
            @Override
            public void call(Subscriber<? super Data> subscriber) {
                if (subscriber.isUnsubscribed()) return;
                for (Data task : tasks) {
                    long result = mDb.insert(Data.TABLE_NAME,
                            Data.FACTORY.marshal(task).asContentValues(),
                            SQLiteDatabase.CONFLICT_REPLACE);
                    if (result >= 0) subscriber.onNext(task);
                }
                subscriber.onCompleted();
            }
        });
    }

    public Observable<List<Data>> getTasks(int state) {
        return mDb.createQuery(Data.TABLE_NAME, Data.SELECT_BY_STATE,
                new String[]{ String.valueOf(state) })
                .mapToList(Data.MAPPER::map);
    }

    public long addTask(Data data) {
        ContentValues values = new ContentValues();
        values.put(Data.NAME, data.name());
        values.put(Data.STATE, data.state());
        return mDb.insert(Data.TABLE_NAME,
                values);
    }

    public int updateTask(Data data) {
        return mDb.update(Data.TABLE_NAME,
                Data.FACTORY.marshal(data).asContentValues(),
                Data.ID + " = ?",
                String.valueOf(data.id()));
    }

    public int deleteTask(Data data) {
        return mDb.delete(Data.TABLE_NAME,
                Data.ID + "=" + data.id());
    }

}
