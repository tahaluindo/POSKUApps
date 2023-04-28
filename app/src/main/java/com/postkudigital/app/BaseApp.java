package com.postkudigital.app;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;

import com.mazenrashed.printooth.Printooth;
import com.postkudigital.app.models.User;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class BaseApp extends Application {
    private static final int SCHEMA_VERSION = 0;
    private User loginUser;
    private Realm realmInstance;
    public static BaseApp getInstance(Context context) {
        return (BaseApp) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Printooth.INSTANCE.init(this);
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .schemaVersion(SCHEMA_VERSION)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(config);
        realmInstance = Realm.getDefaultInstance();
//        realmInstance.beginTransaction();
//        realmInstance.delete(FirebaseToken.class);
//        realmInstance.copyToRealm(token);
//        realmInstance.commitTransaction();

        start();
    }
    public User getLoginUser() {
        return loginUser;
    }

    public void setLoginUser(User loginUser) {
        this.loginUser = loginUser;
    }

    public final Realm getRealmInstance() {
        return realmInstance;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    private void start() {
        Realm realm = getRealmInstance();
        User user = realm.where(User.class).findFirst();
        if (user != null) {
            setLoginUser(user);
        }
    }
}
