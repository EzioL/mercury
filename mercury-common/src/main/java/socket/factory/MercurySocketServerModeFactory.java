package socket.factory;

import socket.handler.ClassicMercurySocketServerHandler;
import socket.handler.MercurySocketHandler;
import socket.handler.MercurySocketServerHandler;
import socket.handler.ReactorMercurySocketServerHandler;

/**
 * Here be dragons
 * Created by haotian on 2018/11/23 6:34 PM
 *
 * 多方法静态工厂
 */
public class MercurySocketServerModeFactory {

    public static MercurySocketServerHandler createClassicBasicHandler(int port , MercurySocketHandler handler) {
        return new ClassicMercurySocketServerHandler(port,handler);
    }

    public static MercurySocketServerHandler createReactorBasicHandler(int port) {
        return new ReactorMercurySocketServerHandler(port);
    }

    // TODO: 2018/11/23  socket线程池处理

    // TODO: 2018/11/23  nio多线程线程处理

}
