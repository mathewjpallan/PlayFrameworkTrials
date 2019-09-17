package services;

import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResultCursor;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class GraphAPI {

    private static final Driver driver;

    static {
        driver = GraphDatabase.driver( "bolt://localhost:7687");
    }

    public static CompletionStage<String> getDataFromNeo4jAsync() {
        Session session = driver.session();
        CompletionStage<StatementResultCursor> cursorStage = session.runAsync("MATCH (n) RETURN n LIMIT 1");
        CompletableFuture<String> result = new CompletableFuture<>();

        cursorStage.thenCompose(StatementResultCursor::singleAsync)
                .thenApply(record -> record.get( 0 ).asMap().toString())
                .thenApply(data -> result.complete(data))
                .thenCompose(ignore -> session.closeAsync());
        return result;
    }

    public static String getDataFromNeo4j() {
        Session session = driver.session();
        String result = session.run("MATCH (n) RETURN n LIMIT 1").single().get(0).asMap().toString();
        session.close();
        return result;
    }
}
