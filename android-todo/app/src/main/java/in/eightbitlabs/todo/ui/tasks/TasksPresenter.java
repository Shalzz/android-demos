package in.eightbitlabs.todo.ui.tasks;

import java.util.List;

import javax.inject.Inject;

import in.eightbitlabs.todo.data.DataManager;
import in.eightbitlabs.todo.data.local.PreferencesHelper;
import in.eightbitlabs.todo.data.model.Data;
import in.eightbitlabs.todo.ui.base.BasePresenter;
import in.eightbitlabs.todo.util.RxUtil;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class TasksPresenter extends BasePresenter<TaskMvpView> {

    private Subscription mSubscription;
    private Subscription mSyncSubscription;
    private DataManager mDataManager;
    private PreferencesHelper mPreferenceHelper;

    @Inject
    TasksPresenter(DataManager dataManager, PreferencesHelper preferencesHelper) {
        mDataManager = dataManager;
        mPreferenceHelper = preferencesHelper;
    }

    @Override
    public void attachView(TaskMvpView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        RxUtil.unsubscribe(mSubscription);
        RxUtil.unsubscribe(mSyncSubscription);
    }

    void syncTasks() {
        checkViewAttached();
        getMvpView().showLoading();
        RxUtil.unsubscribe(mSyncSubscription);
        mSyncSubscription = mDataManager.syncTasks()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Data>() {
                    @Override
                    public void onCompleted() {
                        mPreferenceHelper.setFirstBoot();
                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().dismissLoading();
                        getMvpView().showError(e.getMessage());
                    }

                    @Override
                    public void onNext(Data data) {
                        getMvpView().dismissLoading();
                    }
                });
    }

    void loadTasks(int state) {
        Timber.i("Presenter %s", this);
        checkViewAttached();
        RxUtil.unsubscribe(mSubscription);
        mSubscription = mDataManager.getTasks(state)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Data>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().showError(e.getMessage());
                    }

                    @Override
                    public void onNext(List<Data> tasks) {
                        if (tasks.size() == 0)
                            getMvpView().showTasksEmpty();
                        else
                            getMvpView().showTasks(tasks);
                    }
                });
    }

    public long addTask(String taskName) {
        return mDataManager.addTask(Data.create(0 // use any id as it will be auto incremented
                ,taskName,0));
    }

    int updateTask(Data data) {
        return mDataManager.updateTask(data);
    }

    int deleteTask(Data data) {
        return mDataManager.deleteTask(data);
    }
}
