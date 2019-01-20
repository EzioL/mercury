package db;

import enmus.CommandType;
import exception.CacheException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Here be dragons
 * Created by haotian on 2018/11/12 12:22 PM
 */
public class CacheCommandService {

    public static final String CMD_HEADER = "*";

    public String getResponse(List<String> cmds) {

        if (cmds == null || cmds.size() == 0) {
            throw new CacheException("未知命令");
        }
        if (!cmds.get(0).equals(CMD_HEADER)) {
            throw new CacheException("未知命令");
        }
        // TODO: 2018/11/12
        if (cmds.get(1).equals(CommandType.HELLO.getCmd())) {
            return CommandType.HELLO.getDesc();
        }

        return "111";
    }

    public String getResponse(InputStream inputStream) {

        LocalDriver cacheDriver = new LocalDriver();

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        //读取客户端发送来的消息
        String line = null;
        try {
            line = reader.readLine();
            if (!line.equals(CMD_HEADER)) {
                throw new CacheException("未知命令");
            }
            line = reader.readLine();
            if (line.equals(CommandType.HELLO.getCmd())) {
                return CommandType.HELLO.getDesc();
            }

            if (line.equals(CommandType.SET.getCmd())) {
                String key = reader.readLine();
                if (key != null && !key.equals("")) {
                    throw new CacheException("KEY IS NULL");
                }

                String value = reader.readLine();
                CacheDriver.setData(key, value);
                return "ok";
            }

            if (line.equals(CommandType.GET.getCmd())) {
                String key = reader.readLine();
                if (key != null && !key.equals("")) {
                    throw new CacheException("KEY IS NULL");
                }

                return CacheDriver.getData(key);
            }

            if (line.equals(CommandType.DELETE.getCmd())) {
                String key = reader.readLine();
                if (key != null && !key.equals("")) {
                    throw new CacheException("KEY IS NULL");
                }

                CacheDriver.del(key);
                return "ok";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "111";
    }
}
