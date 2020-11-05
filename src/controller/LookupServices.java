/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.rmi.RemoteException;
import java.rmi.registry.*;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import model.Devices;

/**
 *
 * @author dream
 */
public class LookupServices {

    private String ip;
    private int port;
    public IServerServices iserverservices;

    public LookupServices() {
    }

    public LookupServices(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public boolean connect() throws Exception {
        try {
            Registry registry = LocateRegistry.getRegistry(ip, port);
            iserverservices = (IServerServices) registry.lookup("IServerServices");
        } catch (Exception ex) {
            JOptionPane optionPane = new JOptionPane("Server is offline!!!");
            JDialog dialog = optionPane.createDialog("Warning!!!");
            dialog.setAlwaysOnTop(true);
            dialog.setVisible(true);
            return false;
        }
        return true;
    }

    public void setStatus(Devices devices) throws RemoteException {
        iserverservices.setStatus(devices);
    }
}
