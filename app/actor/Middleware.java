package actor;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.pattern.Patterns;
import akka.routing.FromConfig;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import scala.concurrent.Future;

public class Middleware {
    private static Config config =
            ConfigFactory.systemEnvironment().withFallback(ConfigFactory.load());
    private static ActorSystem system;
    protected static ActorRef requestRouter;

    public static Future<Object> getGraphData() {
        return Patterns.ask(requestRouter, new Request(), 30000);
    }

    public static void init() {
        Config conf = config.getConfig("AkkaMW");
        system = ActorSystem.create("AkkaMW", conf);
        requestRouter = system.actorOf(FromConfig.getInstance().props(RequestRouter.props()).withDispatcher("mw-dispatcher"), RequestRouter.class.getSimpleName());
        System.out.println("Init completed");
    }

}
