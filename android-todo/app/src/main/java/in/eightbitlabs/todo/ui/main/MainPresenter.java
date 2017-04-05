package in.eightbitlabs.todo.ui.main;

import javax.inject.Inject;

import in.eightbitlabs.todo.data.DataManager;
import in.eightbitlabs.todo.injection.ConfigPersistent;
import in.eightbitlabs.todo.ui.base.BasePresenter;
import rx.Subscription;

@ConfigPersistent
public class MainPresenter extends BasePresenter<MainMvpView> {

    @Inject
    public MainPresenter() {

    }

    @Override
    public void attachView(MainMvpView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
    }

}
