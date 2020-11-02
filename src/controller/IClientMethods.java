/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.util.List;
import model.Processes;

/**
 *
 * @author dream
 */
public interface IClientMethods {
    public List<Processes> formatProcesses(List<String> list);
    public String[] splitString(String[] str);
    public int formatMem(String processMem);
}
