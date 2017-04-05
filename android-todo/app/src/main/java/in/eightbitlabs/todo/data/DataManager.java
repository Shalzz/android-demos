package in.eightbitlabs.todo.data;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import in.eightbitlabs.todo.data.local.DatabaseHelper;
import in.eightbitlabs.todo.data.local.PreferencesHelper;
import in.eightbitlabs.todo.data.model.Data;
import in.eightbitlabs.todo.data.model.Tasks;
import in.eightbitlabs.todo.data.remote.TodoService;
import rx.Observable;
import rx.functions.Func1;

@Singleton
public class DataManager {

    private final TodoService mTodoService;
    private final DatabaseHelper mDatabaseHelper;
    private final PreferencesHelper mPreferencesHelper;

    @Inject
    public DataManager(TodoService todoService, PreferencesHelper preferencesHelper,
                       DatabaseHelper databaseHelper) {
        mTodoService = todoService;

        mPreferencesHelper = preferencesHelper;
        mDatabaseHelper = databaseHelper;
    }

    public PreferencesHelper getPreferencesHelper() {
        return mPreferencesHelper;
    }

    public Observable<Data> syncTasks() {
        return mTodoService.getTasks()
                .concatMap(new Func1<Tasks, Observable<Data>>() {
                    @Override
                    public Observable<Data> call(Tasks tasks) {
                        return mDatabaseHelper.setTasks(tasks.getData());
                    }
                });
    }

    public Observable<List<Data>> getTasks(int state) {
        return mDatabaseHelper.getTasks(state);
    }

    public long addTask(Data data) {
        return mDatabaseHelper.addTask(data);
    }

    public int updateTask(Data data) {
        return mDatabaseHelper.updateTask(data);
    }

    public int deleteTask(Data data) {
        return mDatabaseHelper.deleteTask(data);
    }
}
