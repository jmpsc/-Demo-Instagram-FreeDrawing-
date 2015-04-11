package com.jc.tpdemo.modules;

import com.jc.tpdemo.activities.MainActivity;
import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Jorge on 11-04-2015.
 */
@Module(
        injects = MainActivity.class,
        library = true
)
public class TPDemoModule {
    @Provides
    @Singleton Bus provideBus() {
        return new Bus();
    }
}
