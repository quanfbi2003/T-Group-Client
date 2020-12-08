/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.RMIClient;

import java.util.List;
import java.rmi.*;
import model.Devices;
import model.Processes;

/**
 *
 * @author dream
 */
public interface IRMIClientServices extends Remote {

    public boolean killProcess(Processes process) throws RemoteException;

    public List<Processes> update() throws RemoteException;

    public Devices getDevice(Devices devices) throws RemoteException;
    
    public void shutdown() throws RemoteException;
    
    public void  restart() throws RemoteException;
    
    public void remote() throws RemoteException;

}
