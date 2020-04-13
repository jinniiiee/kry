package se.kry.codetest;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import se.kry.codetest.common.DBConnector;
import se.kry.codetest.common.constants.ServiceConstants;
import se.kry.codetest.common.constants.Status;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class BackgroundPoller {

    public Future<List<String>> pollServices(Vertx vertx) {
        return pollServices(ServiceConstants.Query.SELECT_ALL, new DBConnector(vertx));
    }

    public Future<List<String>> pollServices(String query, DBConnector dbConnector) {
        Future<List<String>> future = Future.future();
        dbConnector.getClient().queryWithParams(query, new JsonArray(), result -> {
            if(result.failed()){
                future.fail(ServiceConstants.Message.SERVICE_NOT_FOUND);
            } else {
                Map<String, String> services = result.result().getRows().stream()
                        .collect(Collectors.toMap(x -> x.getString(ServiceConstants.Property.SERVICE_URL)
                                                    , x -> x.getString(ServiceConstants.Property.SERVICE_STATUS)));
                for(String url : services.keySet()){
                    String urlStatus = services.get(url);
                    pollAndUpdateStatus(dbConnector, url, urlStatus);
                }
            }
        });
        return future;
    }

    private void pollAndUpdateStatus(DBConnector dbConnector, String url, String urlStatus) {
        CloseableHttpAsyncClient client = HttpAsyncClients.createDefault();
        client.start();
        java.util.concurrent.Future<HttpResponse> pollerFuture = client.execute(new HttpGet(formatTestURI(url)), null);
        try {
            HttpResponse response = pollerFuture.get();
            if(response != null
                    && response.getStatusLine().getStatusCode() == HttpResponseStatus.OK.code()
                    && !urlStatus.equals(Status.OK.toString())){
                updatePollerStatus(dbConnector, url, Status.OK);
            }else if(!urlStatus.equals(Status.FAIL.toString())) {
                    updatePollerStatus(dbConnector, url, Status.FAIL);
            }
        } catch (InterruptedException | ExecutionException e) {
            if(!urlStatus.equals(Status.FAIL.toString())) {
                updatePollerStatus(dbConnector, url, Status.FAIL);
            }
        }
    }

    private void updatePollerStatus(DBConnector dbConnector, String url, Status status) {
        String updateQuery = ServiceConstants.Query.UPDATE_STATUS;
        JsonArray params = new JsonArray();
        params.add(status.toString());
        params.add(url);
        dbConnector.query(updateQuery, params, null);
    }

    private String formatTestURI(String url) {
        String testUrl = url;
        if(!url.startsWith(ServiceConstants.Common.URL_PREFIX)){
            testUrl = ServiceConstants.Common.FORMAT_URL_PREFIX + url;
        }
        return testUrl;
    }
}
