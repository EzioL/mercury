package client;

import com.google.gson.Gson;
import domain.MercuryGetDataRequest;
import domain.MercuryPutDataRequest;
import entity.MercuryCommand;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import tcp.MercuryRequest;

/**
 * Here be dragons Created by @author Ezio on 2019-01-20 00:08
 */
public class Client100Test {

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(100);
        for (int i = 0; i < 2; i++) {
            executorService.execute(new Runnable() {
                @Override public void run() {

                    request();
                }
            });
        }
        executorService.shutdown();
        while (!executorService.isTerminated()) {
        }
        //for (int i = 0; i < 10; i++) {
        //    request();
        //}

    }

    private static void request() {
        MercuryClientConfiguration configuration = MercuryClientConfiguration.builder()
            .host("127.0.0.1")
            .port(9999)
            .build();

        MercuryClient client = new MercuryClient(configuration);

        MercuryPutDataRequest putDataRequest = new MercuryPutDataRequest("myKey", "myValue" + Math.random());
        MercuryGetDataRequest getDataRequest = new MercuryGetDataRequest("myKey");

        MercuryRequest putRequest = MercuryRequest.builder().command(MercuryCommand.PUT_DATA.getValue())
            .body(new Gson().toJson(putDataRequest))
            .build();

        MercuryRequest getRequest = MercuryRequest.builder().command(MercuryCommand.GET_DATA.getValue())
            .body(new Gson().toJson(getDataRequest))
            .build();
        //ResponseHandler responseHandler = new ResponseHandler() {
        //    @Override public void sendSuccessMessage(int var1, String var2) {
        //
        //    }
        //
        //    @Override public void sendFailureMessage(int var1, Throwable var2) {
        //
        //    }
        //};

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
      //  client.request(putRequest, responseHandler);
    }

    // client.request(putRequest, responseHandler);
}
