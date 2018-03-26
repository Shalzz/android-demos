package com.shaleenjain.ola.play;

import android.content.Context;

import com.shaleenjain.ola.play.injection.component.ApplicationComponent;
import com.shaleenjain.ola.play.injection.component.DaggerApplicationComponent;
import com.shaleenjain.ola.play.injection.module.ApplicationModule;

import timber.log.Timber;

public class Application extends android.app.Application {

    ApplicationComponent mApplicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();

          // TODO: uncomment this when you have added the Bugsnag API key
//        Bugsnag.init(this);
//        Timber.plant(new BugsnagTree());

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }

    public static Application get(Context context) {
        return (Application) context.getApplicationContext();
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
