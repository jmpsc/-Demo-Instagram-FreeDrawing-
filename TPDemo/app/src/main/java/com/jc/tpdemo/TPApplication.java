package com.jc.tpdemo;

import android.app.Application;

import com.jc.tpdemo.modules.TPDemoModule;

import java.util.Arrays;
import java.util.List;

import dagger.ObjectGraph;

/**
 * Created by Jorge on 12-04-2015.
 */
public class TPApplication extends Application {
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
        return Arrays.<Object>asList(new TPDemoModule(this));
    }

    public ObjectGraph getApplicationGraph() {
        return applicationGraph;
    }
}
