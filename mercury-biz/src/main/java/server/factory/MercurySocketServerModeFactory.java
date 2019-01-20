package server.factory;

import server.handler.MercuryServerHandler;
import server.handler.MercurySocketServerHandler;
import server.mode.ClassicMercurySocketServerHandler;
import server.mode.ReactorMercurySocketServerHandler;

/**
 * Here be dragons Created by haotian on 2018/11/23 6:34 PM
 *
 * 多方法静态工厂
 */
public class MercurySocketServerModeFactory {

    public static MercurySocketServerHandler createClassicBasicHandler(int port, MercuryServerHandler handler) {
        return new ClassicMercurySocketServerHandler(port, handler);
    }

    public static MercurySocketServerHandler createReactorBasicHandler(int port, MercuryServerHandler serverHandler) {
        return new ReactorMercurySocketServerHandler(port, serverHandler);
    }

    // TODO: 2018/11/23  socket线程池处理

    // TODO: 2018/11/23  nio多线程线程处理
    //public static ReactorMercurySocketServerHandlerWithThreadPool createReactorBasicHandlerWithThreadPool(int port,
    //    MercuryServerHandler serverHandler) {
    //    return new ReactorMercurySocketServerHandlerWithThreadPool(port, serverHandler);
    //}
}
