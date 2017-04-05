package in.eightbitlabs.guidesdemo;

import android.app.Application;
import android.content.Context;

import in.eightbitlabs.guidesdemo.injection.component.ApplicationComponent;
import in.eightbitlabs.guidesdemo.injection.component.DaggerApplicationComponent;
import in.eightbitlabs.guidesdemo.injection.module.ApplicationModule;
import timber.log.Timber;

public class BoilerplateApplication extends Application  {

    ApplicationComponent mApplicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }

    public static BoilerplateApplication get(Context context) {
        return (BoilerplateApplication) context.getApplicationContext();
    }

    public ApplicationComponent getComponent() {
        if (mApplicationComponent == null) {
            mApplicationComponent = DaggerApplicationComponent.builder()
                    .applicationModule(new ApplicationModule(this))
                    .build();
        }
        return mApplicationComponent;
    }

    // Needed to replace the component with a test specific one
    public void setComponent(ApplicationComponent applicationComponent) {
        mApplicationComponent = applicationComponent;
    }
}
