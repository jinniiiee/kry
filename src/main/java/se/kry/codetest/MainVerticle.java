package se.kry.codetest;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import se.kry.codetest.common.constants.ServiceConstants;
import se.kry.codetest.request.ServiceRouter;

public class MainVerticle extends AbstractVerticle {

  private BackgroundPoller poller = new BackgroundPoller();

  @Override
  public void start(Future<Void> startFuture) {
    Router router = Router.router(vertx);
    router.route().handler(BodyHandler.create());
    vertx.setPeriodic(ServiceConstants.Common.POLL_DELAY, timerId -> poller.pollServices(vertx));
    setRoutes(router);
    vertx
        .createHttpServer()
        .requestHandler(router)
        .listen(ServiceConstants.Common.APP_PORT, result -> {
          if (result.succeeded()) {
            System.out.println(ServiceConstants.Message.APPLICATION_IS_UP);
            startFuture.complete();
          } else {
            startFuture.fail(result.cause());
          }
        });
  }

  private void setRoutes(Router router){
      new ServiceRouter(router, vertx);
  }

}