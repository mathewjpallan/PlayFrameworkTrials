package controllers;

import actor.Middleware;
import akka.util.Timeout;
import com.typesafe.config.Config;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import scala.concurrent.Await;
import scala.concurrent.Future;
import services.GraphAPI;

import javax.inject.Inject;
import java.time.Duration;
import java.util.concurrent.CompletionStage;

public class TestController extends Controller {
    private final Config config;

    @Inject
    public TestController(Config config) {
        this.config = config;
        Middleware.init();
    }

    public Result testApi() {
        return ok("works");
    }

    public Result getData() {
        return ok(GraphAPI.getDataFromNeo4j());
    }

    public CompletionStage<Result> getDataAsync() {
        CompletionStage<String> data = GraphAPI.getDataFromNeo4jAsync();
        return data.thenApply(output -> ok(output));
    }

    public Result getDataAkka() throws Exception {
        Future<Object> future = Middleware.getGraphData();
        return ok((String)Await.result(future, Timeout.create(Duration.ofSeconds(5)).duration()));
    }
}

