package network.common;

import bizsocket.core.AbstractSerialContext;
import bizsocket.core.RequestContext;
import bizsocket.core.RequestQueue;
import bizsocket.core.SerialSignal;
import bizsocket.tcp.Packet;

/**
 * Here be dragons
 * Created by haotian on 2018/11/20 7:13 PM
 */
public class MercurySerialContext extends AbstractSerialContext {

    public MercurySerialContext(SerialSignal serialSignal, RequestContext requestContext) {
        super(serialSignal, requestContext);
    }

    @Override
    public Packet processPacket(RequestQueue requestQueue, Packet packet) {

        return null;
    }
}
