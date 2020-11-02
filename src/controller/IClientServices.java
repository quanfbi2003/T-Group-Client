/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.util.List;
import java.rmi.*;
import model.Processes;
/**
 *
 * @author dream
 */
public interface IClientServices extends Remote{
    public boolean killProcess(Processes process) throws RemoteException;
    public List<Processes> update() throws RemoteException;
}
