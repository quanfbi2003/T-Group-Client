/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.RMIServer;

import controller.RMIServer.IRMIServerServices;
import java.rmi.registry.*;
import model.Definitions;

/**
 *
 * @author dream
 */
public class LookupServices {

    public IRMIServerServices iRMIServices;

    public LookupServices() {
    }
   
    public boolean connect() {
        try {
            Registry registry = LocateRegistry.getRegistry(Definitions.SERVER_IP, Definitions.SERVER_PORT);
            iRMIServices = (IRMIServerServices) registry.lookup(Definitions.REGISTERED_NAME);
        } catch (Exception ex) {
            return false;
        }
        return true;
    }
}
