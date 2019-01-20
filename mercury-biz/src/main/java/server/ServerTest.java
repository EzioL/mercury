package server;

import entity.MercuryPacket;
import server.enmus.MercurySocketServerMode;
import server.handler.MercuryServerHandler;

/**
 * Here be dragons Created by @author Ezio on 2019-01-20 01:12
 */
public class ServerTest {
    public static void main(String[] args) {
        MercurySocketServerBuilder
            .newBuilder()
            .port(9999)
            .mode(MercurySocketServerMode.REACTOR_BASIC)
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
