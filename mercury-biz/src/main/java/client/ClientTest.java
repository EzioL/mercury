package client;

import com.google.gson.Gson;
import domain.MercuryGetDataRequest;
import domain.MercuryPutDataRequest;
import entity.MercuryCommand;
import entity.MercuryPacket;
import tcp.MercuryRequest;
import tcp.MercuryResponseHandler;

/**
 * Here be dragons Created by @author Ezio on 2019-01-20 00:08
 */
public class ClientTest {

    public static void main(String[] args) {




        MercuryClientConfiguration configuration = MercuryClientConfiguration.builder()
            .host("127.0.0.1")
            .port(9999)
            .mode(MercuryClientConfiguration.MODE_NIO)
            .build();

        MercuryClient client = new MercuryClient(configuration);

        MercuryPutDataRequest putDataRequest = new MercuryPutDataRequest("myKey", "myValue");
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

        client.request(putRequest, new MercuryResponseHandler() {
            @Override public void sendSuccessMessage(int cmd, MercuryPacket packet) {
                System.err.println(packet);
            }

            @Override public void sendFailureMessage(int cmd, Throwable error) {
                System.err.println(cmd);
            }
        });

       // client.request(getRequest, responseHandler);

    }
}
