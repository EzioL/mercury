package socket.reactor;

import java.io.IOException;

/**
 * Here be dragons
 * Created by @author Ezio on 2018/12/9 9:49 PM
 */
public class MercuryReactorTest {

    public static void main(String[] args) throws IOException {

        MercuryReactor reactor = new MercuryReactor(9999);
        reactor.run();
    }
}
