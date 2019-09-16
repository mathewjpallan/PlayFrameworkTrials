package controllers;

import com.typesafe.config.Config;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

import javax.inject.Inject;

public class TestController extends Controller {
    private final Config config;

    @Inject
    public TestController(Config config) {
        this.config = config;
    }

    public Result testApi() {
         return ok("works");
    }
}

