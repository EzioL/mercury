package socket.reactor;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

/**
 * Here be dragons
 * Created by @author Ezio on 2018/12/9 8:35 PM
 */
public class BufferTest {
    public static void main(String[] args) throws UnsupportedEncodingException {

        ByteBuffer buffer = ByteBuffer.allocate(1024);
        // 写
        buffer.put("Hello Client".getBytes("UTF-8"));

        // 从channel读到buffer

        String content = System.currentTimeMillis() + " 来自服务端的: ";
        // 代表读完毕了,准备写(即打印出来)

        // 为write()准备
        buffer.flip();
        // =====取出buffer里的数据// 创建字节数组
        byte[] bytes = new byte[buffer.remaining()];
        // 将数据取出放到字节数组里
        buffer.get(bytes);
        content += new String(bytes, "utf-8");
        content += "============";
        System.out.println(content);
        // doWrite(sc, content);

    }
}
