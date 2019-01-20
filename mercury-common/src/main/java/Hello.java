import socket.MercurySocketServerBuilder;
import socket.enmus.MercurySocketServerMode;
import socket.entity.MercuryPacket;
import socket.handler.MercuryServerHandler;

/**
 * Here be dragons Created by haotian on 2018/11/22 4:24 PM
 */
public class Hello {

    public static void main(String[] args) {
        MercurySocketServerBuilder
            .newBuilder()
            .port(9999)
            .mode(MercurySocketServerMode.REACTOR_MULTITHREADED)
            .addServerHandler(new MercuryServerHandler() {
                @Override public MercuryPacket transformRequest(MercuryPacket request) {
                    System.err.println("MercuryServerHandler transformRequest " + request);
                    return request;
                }

                @Override public MercuryPacket transformResponse(MercuryPacket result) {
                    System.err.println("MercuryServerHandler transformResponse " + result);
                    return result;
                }
            })
            .build();
    }





}
