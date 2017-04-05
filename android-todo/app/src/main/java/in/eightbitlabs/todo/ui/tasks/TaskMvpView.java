package in.eightbitlabs.todo.ui.tasks;

import java.util.List;

import in.eightbitlabs.todo.data.model.Data;
import in.eightbitlabs.todo.ui.base.MvpView;

public interface TaskMvpView extends MvpView {

    void showLoading();

    void dismissLoading();

    void showTasks(List<Data> tasks);

    void showError(String msg);

    void showTasksEmpty();
}
