package network;

import bizsocket.core.AbstractBizSocket;
import bizsocket.core.Configuration;
import bizsocket.tcp.PacketFactory;
import network.common.MercuryPacketFactory;

/**
 * Here be dragons
 * Created by haotian on 2018/11/20 12:09 PM
 */
public class MercuryClient extends AbstractBizSocket {


    public MercuryClient(Configuration configuration) {
        super(configuration);
    }

    @Override
    protected PacketFactory createPacketFactory() {
        return new MercuryPacketFactory();
    }


}
