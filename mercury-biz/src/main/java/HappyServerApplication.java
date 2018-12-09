import com.google.gson.Gson;
import java.io.IOException;
import network.common.MercuryPacket;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;
import resp.Resp;
import socket.MercurySocketServerBuilder;
import socket.enmus.MercurySocketServerMode;

/**
 * Here be dragons
 * Created by haotian on 2018/11/26 6:44 PM
 */
public class HappyServerApplication {

    public static void main(String[] args) {



        MercurySocketServerBuilder.newBuilder()
            .port(MercurySocketServerBuilder.DEFAULT_PORT)
            .mode(MercurySocketServerMode.CLASSIC_BASIC)
            .addMercurySocketHandler(socket -> {
                try {
                    BufferedSource reader = Okio.buffer(Okio.source(socket.getInputStream()));
                    MercuryPacket packet = MercuryPacket.build(reader);

                    packet.setResponse(new Gson().toJsonTree(Resp.success("ByeBye")).getAsJsonObject());

                    BufferedSink writer = Okio.buffer(Okio.sink(socket.getOutputStream()));
                    writer.write(packet.toBytes());
                    writer.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            })
            .build();


    }
}
