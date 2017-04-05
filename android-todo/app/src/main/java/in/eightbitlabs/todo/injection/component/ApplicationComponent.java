package in.eightbitlabs.todo.injection.component;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Component;
import in.eightbitlabs.todo.data.DataManager;
import in.eightbitlabs.todo.data.local.DatabaseHelper;
import in.eightbitlabs.todo.data.local.PreferencesHelper;
import in.eightbitlabs.todo.data.remote.TodoService;
import in.eightbitlabs.todo.injection.ApplicationContext;
import in.eightbitlabs.todo.injection.module.ApplicationModule;
import in.eightbitlabs.todo.injection.module.NetworkModule;
import in.eightbitlabs.todo.util.RxEventBus;

@Singleton
@Component(modules = {ApplicationModule.class, NetworkModule.class})
public interface ApplicationComponent {


    @ApplicationContext Context context();
    Application application();
    TodoService todoService();
    PreferencesHelper preferencesHelper();
    DatabaseHelper databaseHelper();
    DataManager dataManager();
    RxEventBus eventBus();

}
