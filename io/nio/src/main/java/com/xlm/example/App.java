package com.xlm.example;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        Selector selector = Selector.open();
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        while (true) {
            int i = selector.select(1000);
            while (i > 0) {
                Set<SelectionKey> selectionKey = selector.selectedKeys();
                Iterator<SelectionKey> selectionKeyIterator = selectionKey.iterator();
                while (selectionKeyIterator.hasNext()) {
                    SelectionKey key = selectionKeyIterator.next();
                    selectionKeyIterator.remove();
                    if (key.isAcceptable()) {
                        SocketChannel socketChannel = serverSocketChannel.accept();
                        socketChannel.configureBlocking(false);
                        socketChannel.register(selector, SelectionKey.OP_READ);
                    } else if (key.isReadable()) {

                        SocketChannel socketChannel = (SocketChannel) key.channel();
                        int read = socketChannel.read(buffer);

                        while (buffer.hasRemaining()){

                        }
                    }
                }
            }
        }
    }

    class Loop {

    }


}
