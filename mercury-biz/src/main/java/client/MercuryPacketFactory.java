package client;

import bizsocket.tcp.Packet;
import bizsocket.tcp.PacketFactory;
import bizsocket.tcp.Request;
import entity.MercuryCommand;
import entity.MercuryPacket;
import java.io.IOException;
import okio.BufferedSource;
import okio.ByteString;

/**
 * Here be dragons
 * Created by haotian on 2018/11/20 12:10 PM
 */
public class MercuryPacketFactory extends PacketFactory {
    @Override
    public Packet getRequestPacket(Packet reusable, Request request) {

        return new MercuryPacket(request.command(), request.body().utf8());
    }

    @Override
    public Packet getHeartBeatPacket(Packet reusable) {
        return new MercuryPacket(MercuryCommand.HEARTBEAT.getValue(),
            ByteString.encodeUtf8("{}").utf8());
    }

    @Override
    public Packet getRemotePacket(Packet reusable, BufferedSource source) throws IOException {
        return MercuryPacket.build(source);
    }
}
