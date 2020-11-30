/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.RMIServer;

import java.rmi.*;
import model.Devices;
/**
 *
 * @author dream
 */
public interface IRMIServerServices extends Remote{
    public void setStatus(Devices devices) throws RemoteException;
}
