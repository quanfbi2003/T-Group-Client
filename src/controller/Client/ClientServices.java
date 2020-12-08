package controller.Client;

import controller.remote.InitConnection;
import java.io.*;
import java.util.List;
import java.rmi.server.*;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.*;
import model.*;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author dream
 */
public class ClientServices extends UnicastRemoteObject implements IRMIClientServices {

//    private static LookupServices lookupServies;
    private Devices devices;

    public ClientServices() throws RemoteException {
        runnableThread();
        remote();
    }

    public void runnableThread() {

        Thread runThread = new Thread(new Runnable() { // Constructor Thread
            @Override
            public void run() {
                devices = new Devices();

                Date startTime = new Date();
                SimpleDateFormat date_format = new SimpleDateFormat("hh:mm:ss");
                devices.setStartTime(date_format.format(startTime));
                devices.setStatus(Definitions.ONLINE);
            }
        });
        runThread.start();

        Thread blackListThread = new Thread(new Runnable() { // BlackList Thread
            @Override
            public void run() {
                while (true) {
                    String[] blprocess = devices.getBlackList().split(",");
                    for (String i : blprocess) {
                        try {
                            killProcess(new Processes(i.replaceAll(".exe", "").trim()));
                        } catch (RemoteException ex) {
                        }
                    }
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException ex) {
                    }
                }
            }
        });
        blackListThread.start();
    }

    @Override
    public boolean killProcess(Processes process) throws RemoteException { //Kill the process from server
        try {
            Runtime.getRuntime().exec("taskkill /F /IM " + process.getProcessName() + ".exe");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public List<Processes> update() throws RemoteException { //Send and update list of processes to server
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
    public Devices getDevice(Devices devices) throws RemoteException { // Send device infomation (stauts and start time) to server
        this.devices.setBlackList(devices.getBlackList());
        return this.devices;
    }

    public List<Processes> formatProcesses(List<String> list) { //Format the processes and create list
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

    public String[] splitString(String[] str) { //Format name of the processes
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

    public int formatMem(String processMem) { //Format memory of the processes
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
    public void shutdown() throws RemoteException { // Shutdown feature
        try {
            Runtime.getRuntime().exec("shutdown -s -t 0");
        } catch (IOException ex) {
        }
    }

    @Override
    public void restart() throws RemoteException { // Restart feature
        try {
            Runtime.getRuntime().exec("shutdown -g -t 0");
        } catch (IOException ex) {
        }
    }

    public void remote() throws RemoteException { // Remote feature
        System.out.println("Remoting....");
        Thread remoteThread = new Thread(new Runnable() {
            @Override
            public void run() {
                new InitConnection(Definitions.REMOTE_PORT);
            }
        });
        remoteThread.start();

    }
}
