package network.common;

import bizsocket.base.Body;
import bizsocket.base.Request;
import com.alibaba.fastjson.JSONObject;
import io.reactivex.Observable;

/**
 * Here be dragons
 * Created by haotian on 2018/11/20 12:22 PM
 */
public interface MercuryService {


    @Request(cmd = 300,desc = "存储数据")
    Observable<JSONObject> put(@Body String params);

    @Request(cmd = 400,desc = "获取数据")
    Observable<JSONObject> get(@Body JSONObject params);


}
