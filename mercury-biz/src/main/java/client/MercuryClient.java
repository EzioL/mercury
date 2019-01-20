package client;

import bizsocket.core.Configuration;
import bizsocket.tcp.Packet;
import bizsocket.tcp.Request;
import com.google.gson.Gson;
import entity.MercuryPacket;
import java.util.concurrent.TimeUnit;
import lombok.Data;
import okio.ByteString;
import tcp.MercuryRequest;
import tcp.MercuryResponseHandler;

/**
 * Here be dragons Created by @author Ezio on 2019-01-20 00:06
 */
@Data
public class MercuryClient implements Runnable {

    private MercuryClientConfiguration clientConfiguration;

    private MercuryResponseHandler responseHandler;

    private MercuryRequest request;

    private MercuryBIOClient bioClient;

    public MercuryClient(MercuryClientConfiguration clientConfiguration) {
        this.clientConfiguration = clientConfiguration;
    }

    public void request(MercuryRequest request, MercuryResponseHandler responseHandler) {
        if (request == null) {
            throw new IllegalArgumentException("request can not be null!");
        } else {
            this.request = request;
            this.responseHandler = responseHandler;
            run();
        }
    }

    @Override
    public void run() {

        if (clientConfiguration.getMode() == MercuryClientConfiguration.MODE_BIO) {
            if (bioClient == null) {
                Configuration configuration = new Configuration.Builder()
                    .host(clientConfiguration.getHost())
                    .port(clientConfiguration.getPort())
                    .readTimeout(TimeUnit.MINUTES, 2)
                    .heartbeat(120)
                    .build();

                bioClient = new MercuryBIOClient(configuration);
            }
            if (!bioClient.isConnected()){
                try {
                    bioClient.connect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            Request bioRequest = new Request.Builder().command(request.getCommand())
                .utf8body(request.getBody())
                .build();
            bioClient.request(bioRequest, new bizsocket.core.ResponseHandler() {
                @Override public void sendSuccessMessage(int i, ByteString byteString, Packet packet) {
                    responseHandler.sendSuccessMessage(i, new Gson().fromJson(packet.getContent(), MercuryPacket.class));
                }

                @Override public void sendFailureMessage(int i, Throwable throwable) {

                }
            });
        } else if (clientConfiguration.getMode() == MercuryClientConfiguration.MODE_NIO) {
            new MercurySocketChannelReactor(clientConfiguration, request, responseHandler).run();
        } else {
            throw new IllegalArgumentException("client mode in unknown !");
        }
    }
}
