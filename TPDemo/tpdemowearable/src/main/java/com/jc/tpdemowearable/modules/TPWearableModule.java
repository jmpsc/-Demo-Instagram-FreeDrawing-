package com.jc.tpdemowearable.modules;

import android.content.Context;

import com.jc.tpdemowearable.TPWearableApplication;
import com.jc.tpdemowearable.activities.MainActivity;
import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Jorge on 14-04-2015.
 */
@Module(
        injects = MainActivity.class,
        library = true
)
public class TPWearableModule {
    private final TPWearableApplication application;

    public TPWearableModule(TPWearableApplication application) {
        this.application = application;
    }

    /**
     * Allow the application context to be injected
     */
    @Provides
    @Singleton
    Context provideApplicationContext() {
        return application;
    }

    @Provides @Singleton
    Bus provideBus() {
        return new Bus();
    }
}
