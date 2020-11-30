package controller.RMIClient;

import model.Processes;
import java.util.List;
import java.rmi.server.*;
import java.rmi.RemoteException;
import model.Devices;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author dream
 */
public class RMIClientServices extends UnicastRemoteObject implements IRMIClientServices {
    public Devices devices;

    public RMIClientServices() throws RemoteException {
    }

    
    @Override
    public boolean killProcess(Processes process) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Processes> update() throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Devices getDevice() throws RemoteException {
        return devices; //To change body of generated methods, choose Tools | Templates.
    }

    

   
}
