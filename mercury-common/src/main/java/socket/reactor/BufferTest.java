package socket.reactor;

import java.nio.ByteBuffer;

/**
 * Here be dragons
 * Created by @author Ezio on 2018/12/9 8:35 PM
 */
public class BufferTest {
    public static void main(String[] args) {

        ByteBuffer buf = ByteBuffer.allocate(48);
        // 写
        buf.put("Hello World".getBytes());
        System.err.println(buf);

    }
}
