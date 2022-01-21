package com.geekbrains.cloud.nio;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;

import static java.nio.file.StandardWatchEventKinds.*;

public class PathExample {

    public static void main(String[] args) throws IOException {
        // Path path = Paths.get("dir/dir1/dir2/file.txt");
        Path path = Paths.get("dir", "dir1", "file.txt");
        Path dir = Paths.get("serverDir");
        Path fxml = dir.resolve("123.fxml");
        System.out.println(Files.exists(fxml));
        System.out.println(Files.size(fxml));

        System.out.println(dir);
        System.out.println(dir.toAbsolutePath());

        startListening(dir);
    }

    private static void startListening(Path path) throws IOException {
        WatchService service = FileSystems.getDefault().newWatchService();
        new Thread(() -> {
            try {
                while (true) {
                    WatchKey key = service.take();
                    List<WatchEvent<?>> events = key.pollEvents();
                    for (WatchEvent<?> event : events) {
                        System.out.println(event.context() + " " + event.kind());
                    }
                    key.reset();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
        path.register(service, ENTRY_CREATE, ENTRY_MODIFY, ENTRY_DELETE);
    }
}
