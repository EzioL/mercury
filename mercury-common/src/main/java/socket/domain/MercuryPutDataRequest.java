package socket.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Here be dragons
 * Created by haotian on 2018/11/20 8:53 PM
 */
@Data
@AllArgsConstructor
public class MercuryPutDataRequest {
    private String key;
    private String value;
}
