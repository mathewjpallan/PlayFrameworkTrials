package controllers;

import actor.Middleware;
import actor.Request;
import akka.actor.ActorRef;
import akka.pattern.Patterns;
import akka.util.Timeout;
import modules.ActorModule;
import play.mvc.Controller;
import play.mvc.Result;
import scala.compat.java8.FutureConverters;
import scala.concurrent.Await;
import scala.concurrent.Future;

import javax.inject.Inject;
import javax.inject.Named;
import java.time.Duration;
import java.util.concurrent.CompletionStage;

public class TestAkkaController extends Controller {
    @Inject
    @Named(ActorModule.GRAPH_ENGINEASYNC)
    ActorRef graphAsyncEngine;

    public Result getDataAkka() throws Exception {
        Future<Object> future = Middleware.getGraphData();
        return ok((String) Await.result(future, Timeout.create(Duration.ofSeconds(5)).duration()));
    }

    public CompletionStage<Result> getDataAkkaAsync() {
        return FutureConverters.toJava(Patterns.ask(graphAsyncEngine, new Request(), 30000))
                .thenApply(data -> ok(data.toString()));
    }

    public CompletionStage<Result> getDataAkkaAsync2() {
        return FutureConverters.toJava(Patterns.ask(graphAsyncEngine, new Request(), 30000))
            .thenCompose(out -> FutureConverters.toJava(Patterns.ask(graphAsyncEngine, new Request(), 30000)))
            .thenApply(data -> ok(data.toString()));
    }

}
