package cache;

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
public class CommandService {

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

        CacheDriver cacheDriver = new CacheDriver();

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

            if (line.equals(CommandType.NEW_TABLE.getCmd())) {
                line = reader.readLine();
                if (line != null && !line.equals("")) {
                    CacheDriver.newTable(line);
                    return "created";
                }
                throw new CacheException("NEW_TABLE 出错");
            }

            if (line.equals(CommandType.DEL_TABLE.getCmd())) {
                line = reader.readLine();
                if (line != null && !line.equals("")) {
                    CacheDriver.removeTable(line);
                    return "deleted";
                }
                throw new CacheException("DEL_TABLE 出错");
            }

            if (line.equals(CommandType.SET.getCmd())) {
                String tableName = reader.readLine();
                if (tableName != null && !tableName.equals("")) {
                    throw new CacheException("TABLE_NAME IS NULL");
                }
                String key = reader.readLine();
                if (key != null && !key.equals("")) {
                    throw new CacheException("KEY IS NULL");
                }
                String value = reader.readLine();
                CacheDriver.setData(tableName, key, value);
                return "ok";
            }

            if (line.equals(CommandType.GET.getCmd())) {
                String tableName = reader.readLine();
                if (tableName != null && !tableName.equals("")) {
                    throw new CacheException("TABLE_NAME IS NULL");
                }
                String key = reader.readLine();
                if (key != null && !key.equals("")) {
                    throw new CacheException("KEY IS NULL");
                }

                return CacheDriver.getData(tableName, key);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "111";
    }
}
