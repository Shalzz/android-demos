package in.eightbitlabs.guidesdemo.ui.guides;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import in.eightbitlabs.guidesdemo.data.DataManager;
import in.eightbitlabs.guidesdemo.data.local.PreferencesHelper;
import in.eightbitlabs.guidesdemo.data.model.Data;
import in.eightbitlabs.guidesdemo.injection.ConfigPersistent;
import in.eightbitlabs.guidesdemo.ui.base.BasePresenter;
import in.eightbitlabs.guidesdemo.util.RxUtil;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

@ConfigPersistent
public class GuidesPresenter extends BasePresenter<GuidesMvpView> {

    private final DataManager mDataManager;
    private Subscription mSubscription;
    private Subscription mSyncSubscription;
    private PreferencesHelper mPreferencesHelper;

    @Inject
    public GuidesPresenter(DataManager dataManager, PreferencesHelper preferencesHelper) {
        mDataManager = dataManager;
        mPreferencesHelper = preferencesHelper;
    }

    @Override
    public void attachView(GuidesMvpView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        if (mSubscription != null) mSubscription.unsubscribe();
        if (mSyncSubscription != null) mSyncSubscription.unsubscribe();
    }

    public void syncGuides() {
        checkViewAttached();
        getMvpView().showLoading();
        RxUtil.unsubscribe(mSyncSubscription);
        mSyncSubscription = mDataManager.syncGuidesData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Data>() {
                    @Override
                    public void onCompleted() {
                        mPreferencesHelper.setFirstBoot();
                        loadGuides();
                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().dismissLoading();
                        Timber.e(e, "There was an error loading the guides.");
                        if(e instanceof IOException)
                            getMvpView().showError("No connection");
                        else
                            getMvpView().showError(e.getMessage());
                    }

                    @Override
                    public void onNext(Data data) {
                        getMvpView().dismissLoading();
                    }
                });
    }

    public void loadGuides() {
        checkViewAttached();
        RxUtil.unsubscribe(mSubscription);
        mSubscription = mDataManager.getGuidesData()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<List<Data>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e, "There was an error loading the guides.");
                    }

                    @Override
                    public void onNext(List<Data> data) {
                        if (data.isEmpty()) {
                            getMvpView().showGuidesEmpty();
                        } else {
                            getMvpView().showGuides(data);
                        }
                    }
                });
    }

    public void loadGuidesinCart() {
        checkViewAttached();
        RxUtil.unsubscribe(mSubscription);
        mSubscription = mDataManager.getGuidesInCart()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<List<Data>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e, "There was an error loading the guides.");
                    }

                    @Override
                    public void onNext(List<Data> data) {
                        Timber.d(data.toString());
                        if (data.isEmpty()) {
                            getMvpView().showGuidesEmpty();
                        } else {
                            getMvpView().showGuides(data);
                        }
                    }
                });
    }

    public void updateData(Data data) {
        mDataManager.updateData(data);
    }
}
