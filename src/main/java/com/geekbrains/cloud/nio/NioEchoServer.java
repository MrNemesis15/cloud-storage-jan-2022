package com.geekbrains.cloud.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class NioEchoServer {

    private ServerSocketChannel serverChannel;
    private Selector selector;
    private ByteBuffer buf;

    public NioEchoServer() throws IOException {
        buf = ByteBuffer.allocate (5);
        serverChannel = ServerSocketChannel.open ();
        selector = Selector.open ();
        serverChannel.configureBlocking (false);
        serverChannel.bind (new InetSocketAddress (8189));
        serverChannel.register (selector, SelectionKey.OP_ACCEPT);
        System.out.println ("Server started...");
        while (serverChannel.isOpen ()) {
            selector.select (); //block
            System.out.println ("Keys selected...");
            Set<SelectionKey> keys = selector.selectedKeys ();
            Iterator<SelectionKey> iterator = keys.iterator ();
            while (iterator.hasNext ()) {
                SelectionKey key = iterator.next ();
                if (key.isAcceptable ()) {
                    handleAccept ();
                }
                if (key.isReadable ()) {
                    handleRead (key);
                }
                iterator.remove ();
            }
        }
    }

    private void handleRead(SelectionKey key) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel ();
        StringBuilder s = new StringBuilder ();
        int read = 0;
        while (true) {
            read = channel.read (buf);
            if (read == 0) {
                break;
            }
            if (read < 0) {
                channel.close ();
                return;
            }
            buf.flip ();
            while (buf.hasRemaining ()) {
                s.append ((char) buf.get ());
            }
            buf.clear ();
        }
        System.out.println("Received: " + s);
        channel.write(ByteBuffer.wrap(s.toString().getBytes(StandardCharsets.UTF_8)));

        String message = s.toString();
        String[] words = message.split(" ");
        if (message.equals("ls")) {
            Path path = Paths.get("cloud-storage-jan-2022", "serverDir");
            try (DirectoryStream<Path> files = Files.newDirectoryStream(path)) {
                for (Path paths : files)
                    channel.write(ByteBuffer.wrap((paths.toString() + "\n").getBytes(StandardCharsets.UTF_8)));
            }
        } else if (words[0].equals("cat")) {
            Path path = Paths.get("cloud-storage-jan-2022", "serverDir", words[1]);
            List<String> list = Files.readAllLines(path);
            for (String str : list) {
                channel.write(ByteBuffer.wrap((str + "\n").getBytes(StandardCharsets.UTF_8)));
            }
        } else {
            System.out.println(message);
        }

    }

    private void handleAccept() throws IOException {
        SocketChannel channel = serverChannel.accept ();
        channel.configureBlocking (false);
        channel.register (selector, SelectionKey.OP_READ);
        channel.write (ByteBuffer.wrap ("Hello user. Welcome to our terminal\n\r".getBytes (StandardCharsets.UTF_8)));
        System.out.println ("Client accepted...");
    }

    public static void main(String[] args) throws IOException {
        new NioEchoServer ();

    }
    /*
      ?????????????? ????????????????, ?????????????? ?????????? ???????????????????????? ??????????????:
      ls - ???????????? ???????????? ?? ????????????????????
      cd dir_name - ?????????????????????????? ?? ????????????????????
      cat file_name - ?????????????????????? ???????????????????? ?????????? ???? ??????????
      mkdir dir_name - ?????????????? ???????????????????? ?? ??????????????
      touch file_name - ?????????????? ???????????? ???????? ?? ?????????????? ????????????????????
      */
}
