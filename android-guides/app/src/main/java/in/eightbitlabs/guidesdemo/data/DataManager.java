package in.eightbitlabs.guidesdemo.data;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import in.eightbitlabs.guidesdemo.data.local.DatabaseHelper;
import in.eightbitlabs.guidesdemo.data.local.PreferencesHelper;
import in.eightbitlabs.guidesdemo.data.model.Data;
import in.eightbitlabs.guidesdemo.data.model.Guides;
import in.eightbitlabs.guidesdemo.data.remote.GuidesService;
import rx.Observable;
import rx.functions.Func1;

@Singleton
public class DataManager {

    private final GuidesService mGuidesService;
    private final DatabaseHelper mDatabaseHelper;
    private final PreferencesHelper mPreferencesHelper;

    @Inject
    public DataManager(GuidesService guidesService, PreferencesHelper preferencesHelper,
                       DatabaseHelper databaseHelper) {
        mGuidesService = guidesService;
        mPreferencesHelper = preferencesHelper;
        mDatabaseHelper = databaseHelper;
    }

    public PreferencesHelper getPreferencesHelper() {
        return mPreferencesHelper;
    }

    public Observable<Data> syncGuidesData() {
        return mGuidesService.getUpcomingGuides()
                .concatMap(new Func1<Guides, Observable<Data>>() {
                    @Override
                    public Observable<Data> call(Guides guides) {
                        return mDatabaseHelper.setGuidesData(guides.getData());
                    }
                });
    }

    public Observable<List<Data>> getGuidesData() {
        return mDatabaseHelper.getGuidesData();
    }

    public Observable<List<Data>> getGuidesInCart() {
        return mDatabaseHelper.getGuidesInCart();
    }

    public int updateData(Data data) {
        return mDatabaseHelper.updateData(data);
    }
}
