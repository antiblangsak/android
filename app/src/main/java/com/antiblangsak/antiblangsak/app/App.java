package com.antiblangsak.antiblangsak.app;

import android.app.Application;
import android.os.StrictMode;

import com.antiblangsak.antiblangsak.R;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by Syukri on 12/13/17.
 */

public class App extends Application {
    @Override public void onCreate() {
        super.onCreate();

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath(AppConfig.REGULAR_FONT)
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
    }
}
