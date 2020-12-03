package controller.RMIClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import model.Processes;
import java.util.List;
import java.rmi.server.*;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.logging.Level;
import java.util.logging.Logger;
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
        try {
            Runtime.getRuntime().exec("taskkill /F /IM " + process.getProcessName() + ".exe"); //shutdown -t 0
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public List<Processes> update() throws RemoteException {
        List<String> list = new ArrayList<>();
        try {
            String line;
            Process p = Runtime.getRuntime().exec(System.getenv("windir") + "\\system32\\" + "tasklist.exe");
            BufferedReader input
                    = new BufferedReader(new InputStreamReader(p.getInputStream()));
            while ((line = input.readLine()) != null) {
                list.add(line);
            }
            input.close();

        } catch (Exception e) {
        }
        return formatProcesses(list);
    }

    @Override
    public Devices getDevice() throws RemoteException {
        return devices; //To change body of generated methods, choose Tools | Templates.
    }

    public List<Processes> formatProcesses(List<String> list) {
        List<Processes> lp = new ArrayList<>();

        try {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).contains(".exe") && list.get(i).contains("Console")) {
                    String[] str = splitString(list.get(i).split("\\s++|\\."));

                    boolean check = false;
                    for (Processes j : lp) {
                        if (str[0].equals(j.getProcessName())) {
                            j.addProcess(formatMem(str[1]));
                            check = true;
                            break;
                        }
                    }
                    if (check == false) {
                        lp.add(new Processes(str[0], 1, formatMem(str[1])));
                    }
                }
            }
            Collections.sort(lp, Comparator.comparing(Processes::getProcessMem, (s1, s2) -> {
                return s2.compareTo(s1);
            }));
        } catch (Exception e) {
        }
        return lp;
    }

    public String[] splitString(String[] str) {
        String temp[] = new String[2];
        temp[0] = "";
        for (int i = 0; i < str.length; i++) {
            if (str[i].equalsIgnoreCase("exe")) {
                temp[0] = temp[0].trim();
                temp[1] = str[i + 4];
                break;
            }
            str[i] = str[i].substring(0, 1).toUpperCase() + str[i].substring(1);
            temp[0] += str[i] + " ";
        }
        return temp;
    }

    public int formatMem(String processMem) {
        int mem = 0;
        for (int i = 0; i < processMem.length(); i++) {
            if (processMem.charAt(i) != ',') {
                try {
                    mem = mem * 10 + Integer.parseInt(processMem.charAt(i) + "");
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        }
        return mem;
    }

    @Override
    public void shutdown() throws RemoteException {
        try {
            Runtime.getRuntime().exec("shutdown -s -t 0");
        } catch (IOException ex) {
        }
    }

    @Override
    public void restart() throws RemoteException {
        try {
            Runtime.getRuntime().exec("shutdown -r");
        } catch (IOException ex) {
        }
    }

}
