package se.kry.codetest.migrate;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import se.kry.codetest.common.DBConnector;
import se.kry.codetest.common.constants.ServiceConstants;

public class DBMigration {

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    DBConnector connector = new DBConnector(vertx);
    connector.query(ServiceConstants.Query.CREATE_TABLE, new JsonArray()).setHandler(done -> {
      if(done.succeeded()){
        System.out.println(ServiceConstants.Message.TABLE_CREATE);
      } else {
        done.cause().printStackTrace();
      }
      vertx.close(shutdown -> {
        System.exit(0);
      });
    });
  }
}
