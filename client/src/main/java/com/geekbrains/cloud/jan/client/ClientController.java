package com.geekbrains.cloud.jan.client;

import com.geekbrains.cloud.jan.model.FileMessage;
import com.geekbrains.cloud.jan.model.FileRequest;
import com.geekbrains.cloud.jan.model.FilesList;
import io.netty.handler.codec.serialization.ObjectDecoderInputStream;
import io.netty.handler.codec.serialization.ObjectEncoderOutputStream;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.ResourceBundle;

public class ClientController implements Initializable {

    public ListView<String> clientView;
    public ListView<String> serverView;
    public TextField textField;
    public Label clientLabel;
    public Label serverLabel;
    private Path currentDir;
    private Net net;
    // sync mode
    // recommended mode
    private ObjectEncoderOutputStream os;
    private ObjectDecoderInputStream is;

    private void fillCurrentDirFiles() {
        Platform.runLater(() -> {
            clientView.getItems().clear();
            clientView.getItems().add("..");
            clientView.getItems().addAll(currentDir.toFile().list());
            clientLabel.setText(getClientFilesDetails());
        });
    }

    private String getClientFilesDetails() {
        File[] files = currentDir.toFile().listFiles();
        long size = 0;
        String label;
        if (files != null) {
            label = files.length + " files in current dir. ";
            for (File file : files) {
                size += file.length();
            }
            label += "Summary size: " + size + " bytes.";
        } else {
            label = "Current dir is empty";
        }
        return label;
    }

    private void initClickListener() {
        clientView.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                String fileName = clientView.getSelectionModel().getSelectedItem();
                System.out.println("Выбран файл: " + fileName);
                Path path = currentDir.resolve(fileName);
                if (Files.isDirectory(path)) {
                    currentDir = path;
                    fillCurrentDirFiles();
                }
            }
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            currentDir = Paths.get(System.getProperty("user.home"));
            fillCurrentDirFiles();
            initClickListener();
            net = Net.getInstance();
            net.setCallback(message -> {
                switch (message.getType()) {
                    case FILE_MESSAGE:
                        FileMessage fileMessage = (FileMessage) message;
                        Files.write(currentDir.resolve(fileMessage.getFileName()), fileMessage.getBytes());
                        fillCurrentDirFiles();
                        break;
                    case LIST:
                        FilesList list = (FilesList) message;
                        updateServerView(list.getList());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateServerView(List<String> names) {
        Platform.runLater(() -> {
            serverView.getItems().clear();
            serverView.getItems().addAll(names);
        });
    }

    public void download(ActionEvent actionEvent) throws IOException {
        String fileName = serverView.getSelectionModel().getSelectedItem();
        net.write(new FileRequest(fileName));
    }

    public void upload(ActionEvent actionEvent) throws IOException {
        String fileName = clientView.getSelectionModel().getSelectedItem();
        FileMessage fileMessage = new FileMessage(currentDir.resolve(fileName));
        net.write(fileMessage);
    }
}
