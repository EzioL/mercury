package cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import java.util.concurrent.TimeUnit;

/**
 * Here be dragons
 * Created by haotian on 2018/11/11 7:28 PM
 */
public class CacheDriver {


    final static Cache<String, String> cache = CacheBuilder.newBuilder()
        //设置cache的初始大小
        .initialCapacity(9999)
        //设置并发数为5，即同一时间最多只能有5个线程往cache执行写入操作
        .concurrencyLevel(5)
        //设置cache中的数据在写入之后的存活时间
        .expireAfterWrite(3, TimeUnit.MINUTES)
        //构建cache实例
        .build();


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


    }

    /**
     * 插入数据
     */
    public static void setData(String key, String value) {
        cache.put(key, value);
    }

    /**
     * 获取数据
     */
    public static String getData(String key) {

        // 不存在返回Null
        return cache.getIfPresent(key);
        //try {
        //    cache.get(key, new Callable<String>() {
        //        @Override public String call() throws Exception {
        //            return null;
        //        }
        //    })
        //} catch (ExecutionException e) {
        //    e.printStackTrace();
        //}

    }

    /**
     * 删除表格
     */
    public static void del(String key) {
        cache.invalidate(key);
    }
}
