package in.eightbitlabs.todo.data.local;

import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Inject;
import javax.inject.Singleton;

import in.eightbitlabs.todo.injection.ApplicationContext;

@Singleton
public class PreferencesHelper {

    public static final String PREF_FILE_NAME = "todo_pref_file";

    private final SharedPreferences mPref;

    @Inject
    public PreferencesHelper(@ApplicationContext Context context) {
        mPref = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
    }

    public void clear() {
        mPref.edit().clear().apply();
    }

    public boolean isFirstBoot() {
        return mPref.getBoolean("first_boot", true);
    }

    public void setFirstBoot() {
        mPref.edit().putBoolean("first_boot", false).apply();
    }

}
