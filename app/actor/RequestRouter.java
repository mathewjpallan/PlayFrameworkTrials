package actor;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.routing.ActorRefRoutee;
import akka.routing.RoundRobinRoutingLogic;
import akka.routing.Routee;
import akka.routing.Router;

import java.util.ArrayList;
import java.util.List;

public class RequestRouter extends AbstractActor {

    private Router router;

    static public Props props() {
        return Props.create(RequestRouter.class, () -> new RequestRouter());
    }

    public RequestRouter() {
        List<Routee> routees = new ArrayList<Routee>();
        for (int i = 0; i < 5; i++) {
            ActorRef r = getContext().actorOf(Props.create(GraphEngine.class));
            getContext().watch(r);
            routees.add(new ActorRefRoutee(r));
        }
        router = new Router(new RoundRobinRoutingLogic(), routees);
        System.out.println("Router created" + getContext().getDispatcher().toString());
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(
                        Request.class,
                        message -> {
                            router.route(message, getSender());
                        })
                .build();
    }
}