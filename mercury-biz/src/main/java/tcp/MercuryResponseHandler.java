package tcp;

import entity.MercuryPacket;

/**
 * Here be dragons Created by @author Ezio on 2019-01-20 00:07
 */
public interface MercuryResponseHandler {

    void sendSuccessMessage(int cmd, MercuryPacket packet);

    void sendFailureMessage(int cmd, Throwable error);
}
