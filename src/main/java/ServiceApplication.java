import network.SocketService;

/**
 * Here be dragons
 * Created by haotian on 2018/11/7 6:27 PM
 * 快乐启动器 项目入口
 */
public class ServiceApplication {

    public static void main(String[] args) {
        SocketService socketService = new SocketService();

        socketService.init();
    }
}
