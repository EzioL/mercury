package server.handler;

import entity.MercuryPacket;

/**
 * Here be dragons Created by @author Ezio on 2019-01-18 19:00
 */
public interface MercuryServerHandler {

    MercuryPacket transformRequest(MercuryPacket request);

    // String handle(T t);

    MercuryPacket transformResponse(MercuryPacket result);


}
