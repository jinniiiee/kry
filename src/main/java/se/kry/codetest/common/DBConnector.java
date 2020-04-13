package se.kry.codetest.common;

import io.netty.handler.codec.http.HttpResponseStatus;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.ResultSet;
import io.vertx.ext.sql.SQLClient;
import io.vertx.ext.web.RoutingContext;
import se.kry.codetest.common.constants.ServiceConstants;
import se.kry.codetest.common.constants.Status;

public class DBConnector {

  private final SQLClient client;

  public DBConnector(Vertx vertx){
    JsonObject config = new JsonObject()
        .put(ServiceConstants.Database.PROP_URL, ServiceConstants.Database.URL + ServiceConstants.Database.DB_PATH)
        .put(ServiceConstants.Database.PROP_DRIVER, ServiceConstants.Database.DRIVER)
        .put(ServiceConstants.Database.PROP_MAX_POOL_SIZE, ServiceConstants.Database.MAX_POOL_SIZE);

    client = JDBCClient.createShared(vertx, config);
  }

  public SQLClient getClient() {
    return client;
  }

  public Future<ResultSet> query(String query, JsonArray params) {
    if(query == null || query.isEmpty()) {
      return Future.failedFuture(ServiceConstants.Message.EMPTY_QUERY);
    }
    query = formatQuery(query);
    Future<ResultSet> queryResultFuture = Future.future();
    client.queryWithParams(query, params, result -> {
      if(result.failed()){
        queryResultFuture.fail(result.cause());
      } else {
        queryResultFuture.complete(result.result());
      }
    });
    return queryResultFuture;
  }

  public void query(String query, RoutingContext context) {
    query(query, new JsonArray(), context);
  }

  public void query(String query, JsonArray params, RoutingContext context) {
    if(query == null || query.isEmpty()) {
      if(context != null) {
        context.fail(HttpResponseStatus.BAD_REQUEST.code());
      }
      return;
    }
    query = formatQuery(query);
    client.queryWithParams(query, params, result -> {
        if(context != null) {
          if (result.failed()) {
              context.fail(result.cause());
          } else if (result.result() == null) {
              context.response()
                      .setStatusCode(HttpResponseStatus.OK.code())
                      .putHeader(ServiceConstants.Common.PROP_CONTENT_TYPE, ServiceConstants.Common.CONTENT_TYPE)
                      .end(Status.OK.toString());
          } else {
              context.response()
                      .setStatusCode(HttpResponseStatus.OK.code())
                      .putHeader(ServiceConstants.Common.PROP_CONTENT_TYPE, ServiceConstants.Common.CONTENT_TYPE)
                      .end(new JsonArray(result.result().getRows()).encode());
          }
        }
    });
  }

  private String formatQuery(String query) {
    if (!query.endsWith(ServiceConstants.Common.QUERY_TERMINATOR)) {
      query = query + ServiceConstants.Common.QUERY_TERMINATOR;
    }
    return query;
  }
}
