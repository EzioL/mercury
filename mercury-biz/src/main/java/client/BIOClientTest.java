package client;

import com.google.gson.Gson;
import domain.MercuryGetDataRequest;
import domain.MercuryPutDataRequest;
import entity.MercuryCommand;
import entity.MercuryPacket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import tcp.MercuryRequest;
import tcp.MercuryResponseHandler;

/**
 * Here be dragons Created by @author Ezio on 2019-01-20 11:38
 */
public class BIOClientTest {
    public static void main(String[] args) {
        //put();
        getRequest();
    }

    private static void put() {
        ExecutorService executorService = Executors.newFixedThreadPool(100);
        for (int i = 0; i < 100; i++) {
            executorService.execute(new Runnable() {
                @Override public void run() {
                    putRequest();
                }
            });
        }
        executorService.shutdown();
        while (!executorService.isTerminated()) {
        }


        getRequest();
    }

    private static void putRequest() {
        MercuryClientConfiguration configuration = MercuryClientConfiguration.builder()
            .host("127.0.0.1")
            .port(9999)
            .mode(MercuryClientConfiguration.MODE_BIO)
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
        client.request(putRequest, new MercuryResponseHandler() {
            @Override
            public void sendSuccessMessage(int cmd, MercuryPacket packet) {
                System.err.println(cmd);
            }

            @Override
            public void sendFailureMessage(int cmd, Throwable error) {

            }
        });
    }

    private static void getRequest() {
        MercuryClientConfiguration configuration = MercuryClientConfiguration.builder()
            .host("127.0.0.1")
            .port(9999)
            .mode(MercuryClientConfiguration.MODE_BIO)
            .build();

        MercuryClient client = new MercuryClient(configuration);

        MercuryPutDataRequest putDataRequest = new MercuryPutDataRequest("myKey", "myValue" + Math.random());
        MercuryGetDataRequest getDataRequest = new MercuryGetDataRequest("myKey");

        MercuryRequest getRequest = MercuryRequest.builder().command(MercuryCommand.GET_DATA.getValue())
            .body(new Gson().toJson(getDataRequest))
            .build();
        client.request(getRequest, new MercuryResponseHandler() {
            @Override
            public void sendSuccessMessage(int cmd, MercuryPacket packet) {
                System.err.println(cmd);
            }

            @Override
            public void sendFailureMessage(int cmd, Throwable error) {

            }
        });
    }
}
