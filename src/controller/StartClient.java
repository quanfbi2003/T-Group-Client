/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import controller.RMIServer.LookupServices;
import controller.RMIClient.RMIClient;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Definitions;
import model.Devices;

/**
 *
 * @author dream
 */
public class StartClient {

    private static RMIClient serverRMI;
    private static LookupServices lookupServies;
    private Devices devices;

    public StartClient() {
        runnableThread();
    }

    public void runnableThread() {
        
        Thread runThread = new Thread(new Runnable() {
            @Override
            public void run() {
                devices = new Devices();
                Calendar calTime = Calendar.getInstance();
                long startTime = calTime.getTimeInMillis();
                devices.setStartTime(startTime);
                devices.setStatus(Definitions.ONLINE);
            }
        });
        runThread.start();
        Thread serverRMIThread = new Thread(new Runnable() {
            @Override
            public void run() {
                serverRMI = new RMIClient();
                serverRMI.startServer();
                
            }
        });
        serverRMIThread.start();
        try {
            runThread.join();
            serverRMIThread.join();
        } catch (InterruptedException ex) {
        }
        serverRMI.rmiServices.devices = devices;
    }

    public static void main(String[] args) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                new StartClient();
            }
        }).start();
    }

}
