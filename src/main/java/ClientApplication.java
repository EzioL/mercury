import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Here be dragons
 * Created by haotian on 2018/11/7 6:27 PM
 * 快乐启动器 项目入口
 */
public class ClientApplication {

    public static void main(String[] args) {



        try {
            Socket socket = new Socket("127.0.0.1", 5489);

            String cmd = "*1\r\n$5\r\nHELLO\r\n";

            OutputStream os = socket.getOutputStream();
            os.write(cmd.getBytes());
            os.flush();

            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String response = reader.readLine();
            reader.close();
            os.close();
            socket.close();

            System.err.println(response);

        } catch (IOException e) {

            e.printStackTrace();
        }
    }
}
