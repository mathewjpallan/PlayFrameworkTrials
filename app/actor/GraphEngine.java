package actor;

import akka.actor.AbstractActor;
import akka.actor.Props;
import services.GraphAPI;

public class GraphEngine extends AbstractActor {

    public Receive createReceive() {
        return receiveBuilder()
                .match(Request.class,
                        message -> {
                            getSender().tell(GraphAPI.getDataFromNeo4j(), getSelf());
                        })
                .build();
    }

    static public Props props() {
        return Props.create(GraphEngine.class, () -> new GraphEngine());
    }

}
