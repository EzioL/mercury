package server.mltipleReactor;

import java.io.IOException;

/**
 * Here be dragons Created by @author Ezio on 2019-01-20 10:43
 */
public class MainReactorTest {
    public static void main(String[] args) {

        try {
            MainReactor reactor = new MainReactor(9999);
            reactor.run();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
