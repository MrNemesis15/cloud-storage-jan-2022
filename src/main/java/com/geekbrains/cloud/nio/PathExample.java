package com.geekbrains.cloud.nio;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import static java.nio.file.Files.size;

public class PathExample {
    public static void main(String[] args) throws IOException {
        //Path path = Paths.get ("dir/dir1/dir2/file.txt");
        Path path = Paths.get ("dir,dir1,dir2,file.txt");
        Path dir = Paths.get ("serverDir");
        Path fxml = dir.resolve ("123.fxml");
        System.out.println (Files.exists (fxml));
        System.out.println (size (fxml));

        System.out.println (dir);
        System.out.println (dir.toAbsolutePath ());
    }

}
