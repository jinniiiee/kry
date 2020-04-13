package se.kry.codetest.request;

import io.vertx.core.Vertx;

import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.StaticHandler;
import se.kry.codetest.common.constants.ServiceConstants;

public class ServiceRouter {

    private Vertx vertx;

    public ServiceRouter(Router router, Vertx vertx){
        this.vertx = vertx;
        router.route(ServiceConstants.Path.ROUTE_PATH).handler(StaticHandler.create());
        router.get(ServiceConstants.Path.ALL_SERVICE_PATH).handler(this::fetchServiceRoute);
        router.post(ServiceConstants.Path.CREATE_SERVICE_PATH).handler(this::createServiceRoute);
        router.post(ServiceConstants.Path.UPDATE_SERVICE_PATH).handler(this::updateServiceRoute);
        router.delete(ServiceConstants.Path.DELETE_SERVICE_PATH).handler(this::deleteServiceRoute);
    }

    public void fetchServiceRoute(RoutingContext context){
        new RequestHandler().fetchAll(vertx, context);
    }

    public void createServiceRoute(RoutingContext context) {
        new RequestHandler().create(vertx, context);
    }

    public void updateServiceRoute(RoutingContext context){
        new RequestHandler().update(vertx, context);
    }

    public void deleteServiceRoute(RoutingContext context){
        new RequestHandler().delete(vertx, context);
    }
}
