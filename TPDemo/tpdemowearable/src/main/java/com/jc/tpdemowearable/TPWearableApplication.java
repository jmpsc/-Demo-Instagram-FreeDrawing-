package com.jc.tpdemowearable;

import android.app.Application;

import com.jc.tpdemowearable.modules.TPWearableModule;

import java.util.Arrays;
import java.util.List;

import dagger.ObjectGraph;

/**
 * Created by Jorge on 14-04-2015.
 */
public class TPWearableApplication extends Application {
    private ObjectGraph applicationGraph;

    @Override public void onCreate() {
        super.onCreate();

        applicationGraph = ObjectGraph.create(getModules().toArray());
    }

    /**
     * A list of modules to use for the application graph. Subclasses can override this method to
     * provide additional modules provided they call {@code super.getModules()}.
     */
    protected List<Object> getModules() {
        return Arrays.<Object>asList(new TPWearableModule(this));
    }

    public ObjectGraph getApplicationGraph() {
        return applicationGraph;
    }
}
