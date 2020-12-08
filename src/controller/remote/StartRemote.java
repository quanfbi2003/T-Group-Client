package controller.remote;

import java.io.DataInputStream;
import java.net.Socket;
import model.Definitions;

public class StartRemote {

    DataInputStream verification = null;
    String width = "", height = "";

    public void initialize(String ip, int port) {
        try {
            Socket sc = new Socket(ip, port);
            System.out.println("Connecting to the Server");
            verification = new DataInputStream(sc.getInputStream());
            width = verification.readUTF();
            height = verification.readUTF();
            CreateFrame abc = new CreateFrame(sc, width, height);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
