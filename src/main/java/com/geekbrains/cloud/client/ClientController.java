package com.geekbrains.cloud.client;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class ClientController implements Initializable {
    public ListView<String> listView;
    public TextField textField;

    private DataInputStream is;
    private DataOutputStream os;

    public void sendMessage(ActionEvent actionEvent) throws IOException {
        String message = textField.getText ();
        os.writeUTF (message);
        os.flush ();
        textField.clear ();
        //listView.getItems ().add (message);
    }

    private void read() {
        try {
            while (true) {
                String message = is.readUTF ();
                Platform.runLater (() -> listView.getItems ().add (message));
            }
        } catch (Exception e) {
            e.printStackTrace ();
            //add reconnect to server
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            Socket socket = new Socket ("localhost", 8189);
            is = new DataInputStream (socket.getInputStream ());
            os = new DataOutputStream (socket.getOutputStream ());
            Thread readThread = new Thread (this::read);
            readThread.setDaemon (true);
            readThread.start ();

        } catch (Exception e) {
            e.printStackTrace ();
        }
    }
}
