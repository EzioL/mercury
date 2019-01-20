package client;

import bizsocket.core.AbstractBizSocket;
import bizsocket.core.Configuration;
import bizsocket.tcp.PacketFactory;

/**
 * Here be dragons Created by @author Ezio on 2019-01-20 11:20
 */
public class MercuryBIOClient extends AbstractBizSocket {

    public MercuryBIOClient(Configuration configuration) {
        super(configuration);
    }

    @Override
    protected PacketFactory createPacketFactory() {
        return new MercuryPacketFactory();
    }


}
