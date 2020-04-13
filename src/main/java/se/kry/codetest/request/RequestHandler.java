package se.kry.codetest.request;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.ext.web.RoutingContext;

import se.kry.codetest.common.constants.ServiceConstants;

public class RequestHandler {

    public void create(Vertx vertx, RoutingContext context){
        String createQuery = ServiceConstants.Query.CREATE_RECORD;
        JsonArray params = new JsonArray();
        params.add(context.getBodyAsJson().getString(ServiceConstants.Property.SERVICE_NAME));
        params.add(context.getBodyAsJson().getString(ServiceConstants.Property.SERVICE_URL));
        params.add(context.getBodyAsJson().getString(ServiceConstants.Property.SERVICE_STATUS));
        ServiceRepository.query(vertx, createQuery, params, context);
    }

    public void update(Vertx vertx, RoutingContext context){
        String updateQuery = ServiceConstants.Query.UPDATE_URL;
        JsonArray params = new JsonArray();
        params.add(context.getBodyAsJson().getString(ServiceConstants.Property.SERVICE_URL));
        params.add(context.getBodyAsJson().getString(ServiceConstants.Property.SERVICE_NAME));
        ServiceRepository.query(vertx, updateQuery, params, context);
    }

    public void delete(Vertx vertx, RoutingContext context){
        JsonArray params = new JsonArray();
        params.add(context.getBodyAsJson().getString(ServiceConstants.Property.SERVICE_NAME));
        ServiceRepository.query(vertx, ServiceConstants.Query.DELETE_RECORD, params, context);
    }

    public void fetchAll(Vertx vertx, RoutingContext context){
        ServiceRepository.query(vertx, ServiceConstants.Query.SELECT_ALL, null, context);
    }

}
