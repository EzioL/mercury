package server.mltipleReactor;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * Here be dragons Created by @author Ezio on 2019-01-20 02:19
 */
public class SubReactor implements Runnable {

    private final ServerSocketChannel serverSocketChannel;
    private final Selector selector;
    private boolean restart = false;
    int num;

    public SubReactor(Selector selector, ServerSocketChannel serverSocketChannel, int i) {

        this.serverSocketChannel = serverSocketChannel;
        this.selector = selector;
        this.num = num;
    }

    @Override public void run() {
        while (!Thread.interrupted()) {

            System.out.println("waiting for restart");
            while (!Thread.interrupted() && !restart) {
                try {
                    if (selector.select() == 0) {
                        continue;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> it = selectedKeys.iterator();
                while (it.hasNext()) {
                    dispatch((SelectionKey) (it.next()));
                    it.remove();
                }
            }
        }
    }

    void setRestart(boolean restart) {
        this.restart = restart;
    }

    private void dispatch(SelectionKey key) {
        Runnable r = (Runnable) (key.attachment());
        if (r != null) {
            r.run();
        }
    }
}
