package client;

import bizsocket.core.Configuration;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * Here be dragons Created by @author Ezio on 2019-01-20 00:09
 */

@Data
@Builder
@AllArgsConstructor
public class MercuryClientConfiguration extends Configuration {

    public static final int MODE_BIO = 1;

    public static final int MODE_NIO = 2;

    private int mode = MODE_BIO;

    private String host;

    private int port;


}
