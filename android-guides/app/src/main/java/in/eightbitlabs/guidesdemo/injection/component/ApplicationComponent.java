package in.eightbitlabs.guidesdemo.injection.component;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Component;
import in.eightbitlabs.guidesdemo.data.DataManager;
import in.eightbitlabs.guidesdemo.data.local.DatabaseHelper;
import in.eightbitlabs.guidesdemo.data.local.PreferencesHelper;
import in.eightbitlabs.guidesdemo.data.remote.GuidesService;
import in.eightbitlabs.guidesdemo.injection.ApplicationContext;
import in.eightbitlabs.guidesdemo.injection.module.ApplicationModule;
import in.eightbitlabs.guidesdemo.injection.module.NetworkModule;
import in.eightbitlabs.guidesdemo.util.RxEventBus;

@Singleton
@Component(modules = {ApplicationModule.class, NetworkModule.class})
public interface ApplicationComponent {

    @ApplicationContext Context context();
    Application application();
    GuidesService guidesService();
    PreferencesHelper preferencesHelper();
    DatabaseHelper databaseHelper();
    DataManager dataManager();
    RxEventBus eventBus();

}
