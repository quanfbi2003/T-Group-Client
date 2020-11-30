/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.RMIClient;

import java.rmi.RemoteException;
import java.rmi.registry.*;
import model.Definitions;
/**
 *
 * @author dream
 */
public class RMIClient {
    
    public static RMIClientServices rmiServices;
    
    public void startServer() {
        try {
            Registry registry = LocateRegistry.createRegistry(Definitions.CLIENT_PORT);
            rmiServices = new RMIClientServices();
            registry.rebind(Definitions.REGISTERED_NAME, rmiServices);
        } catch (RemoteException ex) {
            System.out.println(ex.getMessage());
        }
        System.out.println("RMIClient is online!");
    }
}
