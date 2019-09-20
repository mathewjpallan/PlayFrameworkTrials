package actor;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.pattern.Patterns;
import services.GraphAPI;

public class GraphEngine extends AbstractActor {

    public Receive createReceive() {
        return receiveBuilder()
                .match(Request.class,
                        message -> {
                            Patterns.pipe(GraphAPI.getDataFromNeo4jAsync(), getContext().getDispatcher()).to(getSender());
                })
                .build();
    }

    static public Props props() {
        return Props.create(GraphEngine.class, () -> new GraphEngine());
    }

}
