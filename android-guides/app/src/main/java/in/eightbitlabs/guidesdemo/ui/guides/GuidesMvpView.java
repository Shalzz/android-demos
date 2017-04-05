package in.eightbitlabs.guidesdemo.ui.guides;

import java.util.List;

import in.eightbitlabs.guidesdemo.data.model.Data;
import in.eightbitlabs.guidesdemo.ui.base.MvpView;

public interface GuidesMvpView extends MvpView {

    public void showLoading();

    public void dismissLoading();

    void showGuides(List<Data> data);

    void showGuidesEmpty();

    void showError();

    void showError(String error);

}
