import java.net.Socket;
import socket.MercurySocketServerBuilder;
import socket.enmus.MercurySocketServerMode;
import socket.handler.MercurySocketHandler;

/**
 * Here be dragons
 * Created by haotian on 2018/11/22 4:24 PM
 */
public class Hello {

    public static void main(String[] args) {
        MercurySocketServerBuilder
            .newBuilder()
            .port(MercurySocketServerBuilder.DEFAULT_PORT)
            .mode(MercurySocketServerMode.CLASSIC_BASIC)
            .addMercurySocketHandler(new MercurySocketHandler() {
                @Override
                public void handle(Socket socket) {



                }
            })
            .build();

        //Channels

    }
}
