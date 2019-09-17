package controllers;

import com.typesafe.config.Config;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import services.GraphAPI;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

public class TestController extends Controller {
    private final Config config;

    @Inject
    public TestController(Config config) {
        this.config = config;
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


}

