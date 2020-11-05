/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.rmi.RemoteException;
import java.rmi.registry.*;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author dream
 */
public class RMIClient {
    private void startClient() throws Exception {
        try {
            Registry registry = LocateRegistry.createRegistry(2020);
            registry.rebind("IClientServices", new ClientServices());
        } catch (RemoteException ex) {
            System.out.println(ex.getMessage());
        }
        System.out.println("Client is online!");
    }
    
    public static void main(String[] args) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                RMIClient client = new RMIClient();
                try {
                    client.startClient();
                } catch (Exception ex) {
                }
            }
        }).start();
    }
}
