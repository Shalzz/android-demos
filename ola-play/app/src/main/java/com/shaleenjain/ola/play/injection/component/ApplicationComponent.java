package com.shaleenjain.ola.play.injection.component;

import android.app.Application;
import android.content.Context;

import com.shaleenjain.ola.play.MusicService;
import com.shaleenjain.ola.play.data.DataManager;
import com.shaleenjain.ola.play.data.local.DatabaseHelper;
import com.shaleenjain.ola.play.data.local.PreferencesHelper;
import com.shaleenjain.ola.play.data.remote.APIService;
import com.shaleenjain.ola.play.injection.ApplicationContext;
import com.shaleenjain.ola.play.injection.module.ApplicationModule;
import com.shaleenjain.ola.play.injection.module.NetworkModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class, NetworkModule.class})
public interface ApplicationComponent {


    @ApplicationContext
    Context context();
    Application application();
    APIService ribotsService();
    PreferencesHelper preferencesHelper();
    DatabaseHelper databaseHelper();
    DataManager dataManager();

    void inject(MusicService musicService);
}
