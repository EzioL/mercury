import java.net.Socket;
import socket.MercurySocketServerBuilder;
import socket.enmus.MercurySocketServerMode;
import socket.handler.MercurySocketHandler;

/**
 * Here be dragons
 * Created by haotian on 2018/11/26 6:44 PM
 */
public class HappyServerApplication {

    public static void main(String[] args) {
        MercurySocketHandler mercurySocketHandler = new MercurySocketHandler() {
            @Override
            public void handle(Socket socket) {


            }
        };

        MercurySocketServerBuilder.newBuilder()
            .port(MercurySocketServerBuilder.DEFAULT_PORT)
            .mode(MercurySocketServerMode.CLASSIC_BASIC)
            .addMercurySocketHandler(mercurySocketHandler)
            .build();


    }
}
