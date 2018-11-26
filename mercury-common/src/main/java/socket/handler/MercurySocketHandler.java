package socket.handler;

import java.net.Socket;

/**
 * Here be dragons
 * Created by haotian on 2018/11/22 10:36 PM
 */
public interface MercurySocketHandler {

    /**
     * Socket的处理
     * @param socket
     */
    void handle(Socket socket);
}
