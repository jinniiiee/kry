package se.kry.codetest.request;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.ext.web.RoutingContext;
import se.kry.codetest.common.DBConnector;

public class ServiceRepository {

    private static DBConnector getConnection(Vertx vertx){
        DBConnector connector = new DBConnector(vertx);
        return connector;
    }

    public static void query(Vertx vertx, String query, JsonArray params, RoutingContext context){
        if(params == null){
            getConnection(vertx).query(query, context);
        }
        getConnection(vertx).query(query, params, context);
    }

}
