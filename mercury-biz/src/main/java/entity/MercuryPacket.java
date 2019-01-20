package entity;

import bizsocket.tcp.Packet;
import com.google.gson.JsonObject;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import lombok.Data;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.ByteString;
import okio.Okio;

/**
 * Here be dragons Created by haotian on 2018/11/20 11:59 AM
 */
@Data
public class MercuryPacket extends Packet {

    static volatile int currentSeq = 0;
    public int length;//包大小
    public int cmd;//命令号
    public int seq;//包id  packetId

    public String content;

    public MercuryPacket() {
    }

    public MercuryPacket(int cmd, String requestBody) {
        this.content = requestBody;
        this.cmd = cmd;
        this.seq = nextSeq();
    }

    private int nextSeq() {
        currentSeq++;
        if (currentSeq == Integer.MAX_VALUE) {
            currentSeq = 1;
        }
        return currentSeq;
    }

    public MercuryPacket(int cmd, int seq, String content) {
        this.cmd = cmd;
        this.seq = seq;
        this.content = content;
    }

    //public void setResponse(Map<String,String> map) {
    //
    //    this.content = SampleServer.map2json(map);
    //}
    //
    public void setResponse(JsonObject object) {
        this.content = object.toString();
    }

    @Override
    public byte[] toBytes() {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        BufferedSink bufferedSink = Okio.buffer(Okio.sink(bos));

        try {
            //包长 = 内容长度 + 包头固定的12个字节
            ByteString byteString = ByteString.encodeUtf8(content);
            bufferedSink.writeInt(byteString.size() + 12);
            bufferedSink.writeInt(cmd);
            bufferedSink.writeInt(seq);
            bufferedSink.write(byteString);
            bufferedSink.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bos.toByteArray();
    }

    @Override
    public int getCommand() {
        return cmd;
    }

    @Override
    public void setCommand(int command) {
        this.cmd = command;
    }

    @Override
    public String getDescription() {
        return MercuryCommand.fromValue(getCommand()).getDesc();
    }

    @Override
    public String getPacketID() {
        return String.valueOf(seq);
    }

    @Override
    public void setPacketID(String packetID) {
        try {
            int id = Integer.valueOf(packetID);
            this.seq = id;
        } catch (NumberFormatException e) {

        }
    }

    @Override
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Packet{" +
            "cmd=" + cmd +
            ", seq=" + seq +
            ", content='" + content + '\'' +
            '}';
    }

    public static MercuryPacket build(BufferedSource reader) {
        MercuryPacket packet = new MercuryPacket();

        try {
            packet.length = reader.readInt();
            packet.cmd = reader.readInt();
            packet.seq = reader.readInt();
            //减去协议头的12个字节长度
            packet.content = reader.readString(packet.length - 12, Charset.forName("utf-8"));
        } catch (IOException e) {
            // e.printStackTrace();
        }

        return packet;
    }
}
