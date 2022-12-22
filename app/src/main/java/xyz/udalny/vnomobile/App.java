package xyz.udalny.vnomobile;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;

public class App extends Application {

    private static Context context;
    private static Resources resources;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        resources = getResources();
    }

    public static Context getContext(){
        return context;
    }

    public static Resources getRes() {
        return resources;
    }
}
