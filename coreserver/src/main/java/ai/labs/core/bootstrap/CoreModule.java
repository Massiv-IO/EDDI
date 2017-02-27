package ai.labs.core.bootstrap;

import ai.labs.core.CoreRuntime;
import ai.labs.core.behavior.BehaviorRulesEvaluationTask;
import ai.labs.core.behavior.BehaviorSerialization;
import ai.labs.core.behavior.IBehaviorSerialization;
import ai.labs.core.normalizing.NormalizeInputTask;
import ai.labs.core.output.SimpleOutputTask;
import ai.labs.core.rest.internal.RestBotAdministration;
import ai.labs.core.rest.internal.RestBotEngine;
import ai.labs.lifecycle.ILifecycleTask;
import ai.labs.parser.InputParserTask;
import ai.labs.rest.rest.IRestBotAdministration;
import ai.labs.rest.rest.IRestBotEngine;
import ai.labs.runtime.bootstrap.AbstractBaseModule;
import com.google.inject.Scopes;
import com.google.inject.multibindings.MapBinder;

import java.io.InputStream;

public class CoreModule extends AbstractBaseModule {
    public CoreModule(InputStream... inputStream) {
        super(inputStream);
    }

    @Override
    protected void configure() {
        registerConfigFiles(configFiles);

        bind(IBehaviorSerialization.class).to(BehaviorSerialization.class).in(Scopes.SINGLETON);

        bind(IRestBotEngine.class).to(RestBotEngine.class);
        bind(IRestBotAdministration.class).to(RestBotAdministration.class);
        /*bind(IRestBotUI.class).to(RestBotUI.class);
        bind(IRestBotmarklet.class).to(RestBotmarklet.class);*/

        MapBinder<String, ILifecycleTask> lifecycleTaskPlugins
                = MapBinder.newMapBinder(binder(), String.class, ILifecycleTask.class);
        lifecycleTaskPlugins.addBinding("ai.labs.normalizer").to(NormalizeInputTask.class);
        lifecycleTaskPlugins.addBinding("ai.labs.parser").to(InputParserTask.class);
        lifecycleTaskPlugins.addBinding("ai.labs.behavior").to(BehaviorRulesEvaluationTask.class);
        lifecycleTaskPlugins.addBinding("ai.labs.output").to(SimpleOutputTask.class);

        bind(CoreRuntime.class).asEagerSingleton();
    }
}
