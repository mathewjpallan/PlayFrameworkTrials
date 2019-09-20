package modules;

import actor.GraphEngineAsync;
import com.google.inject.AbstractModule;
import play.libs.akka.AkkaGuiceSupport;

public class ActorModule extends AbstractModule implements AkkaGuiceSupport {

    public static final String GRAPH_ENGINEASYNC = "graph-engineasync";

    @Override
    protected void configure() {
        bindActor(GraphEngineAsync.class, GRAPH_ENGINEASYNC);
    }
}