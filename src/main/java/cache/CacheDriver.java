package cache;

import com.google.common.collect.Lists;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import common.domain.Resp;
import common.utils.EzioFileUtils;
import exception.CacheException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

/**
 * Here be dragons
 * Created by haotian on 2018/11/11 7:28 PM
 */
public class CacheDriver {

    private static final String DB = "src/main/db";

    public static void main(String[] args) throws Exception {
        //ArrayList<String> exts = Lists.newArrayList();
        //exts.add("json");
        //
        //List<File> files =
        //    EzioFileUtils.listFile(new File(DB), exts, true);
        //
        //System.err.println(files);
        //newTable("a");
        //JSONObject object = new JSONObject();
        //System.err.println(object.toJSONString());

        String data = getData("a", "mykey1");
        System.err.println(data);
    }

    /**
     * 插入数据
     */
    public static void setData(String tableName, String key, String value) {

        Resp.assertion(getTargetTable(tableName), "表格不存在")
            .next(table -> {
                return Resp.assertion(getTableJson(table), "获取表格数据失败");
            })
            .next(jsonObject -> {
                if (jsonObject.get(key).getAsString() == null) {

                }

                // 新建的时候 写入数据

                // 更新的时候 更新数据

                jsonObject.addProperty(key, value);

                return Resp.success("ok");
            })
        ;
    }

    /**
     * 获取数据
     */
    public static String getData(String tableName, String key) {

        Resp<String> resp = Resp.assertion(getTargetTable(tableName), "表格不存在")
            .next(table -> {
                return Resp.assertion(getTableJson(table), "获取表格数据失败");
            })
            .next(jsonObject -> {
                return Resp.success(jsonObject.get(key).toString());
            });

        if (resp.isSuccess()) {
            return resp.getData();
        }
        throw new CacheException(resp.getErrorHint());
    }

    /**
     * 删除表格
     */
    public static void removeTable(String table) {
        File tableFile = new File(DB + "/" + table + ".json");
        if (!tableFile.exists()) {
            throw new CacheException("表格不存在");
        }
        tableFile.delete();
    }

    /**
     * 新建表格
     */
    public static void newTable(String table) {

        File tableFile = new File(DB + "/" + table + ".json");
        if (tableFile.exists()) {
            throw new CacheException("表格已经存在");
        }
        try {
            tableFile.createNewFile();
            FileOutputStream out = new FileOutputStream(tableFile, true);
            out.write("{\r\n}".getBytes("utf-8"));
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Optional<JsonObject> getTableJson(File table) {

        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(table));
            String temp = null;
            StringBuilder sb = new StringBuilder();
            temp = br.readLine();
            while (temp != null) {
                sb.append(temp).append(" ");
                temp = br.readLine();
            }
            return Optional.of(new JsonParser().parse(sb.toString()).getAsJsonObject());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    private static Optional<File> getTargetTable(String tableName) {
        ArrayList<String> exts = Lists.newArrayList();
        exts.add("json");
        return EzioFileUtils.listFile(new File(DB), exts, true).stream().filter(e -> e.getName().equals(tableName + ".json"))
            .findFirst();
    }
}
