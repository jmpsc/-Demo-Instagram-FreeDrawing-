package com.jc.tpdemo.modules;

import android.content.Context;

import com.jc.tpdemo.TPApplication;
import com.jc.tpdemo.activities.MainActivity;
import com.jc.tpdemo.asynctasks.SaveImageAsyncTask;
import com.jc.tpdemo.data.services.InstagramService;
import com.jc.tpdemo.fragments.FingerPaintingFragment;
import com.jc.tpdemo.fragments.InstagramListFragment;
import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.RestAdapter;

/**
 * Created by Jorge on 11-04-2015.
 */
@Module(
        injects = {
                FingerPaintingFragment.class,
                SaveImageAsyncTask.class,
                InstagramListFragment.class
        },
        library = true
)
public class TPDemoModule {
    private final TPApplication application;

    public TPDemoModule(TPApplication application) {
        this.application = application;
    }

    /**
     * Allow the application context to be injected
     */
    @Provides @Singleton
    Context provideApplicationContext() {
        return application;
    }

    @Provides
    InstagramService provideInstagramService() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("https://api.instagram.com/v1")
                .build();

        return restAdapter.create(InstagramService.class);
    }

    @Provides @Singleton
    Bus provideBus() {
        return new Bus();
    }
}
