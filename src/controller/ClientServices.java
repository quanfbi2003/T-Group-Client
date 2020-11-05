package controller;

import model.Processes;
import view.LogInView;
import java.awt.AWTException;
import java.awt.Robot;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.rmi.server.*;
import java.rmi.RemoteException;
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
public class ClientServices extends UnicastRemoteObject implements IClientServices, IClientMethods {

    public LogInView logInView;
    public Devices devices;
    public Thread loginThread;
    public boolean login = false;
    public LookupServices lookupServices;

    public ClientServices() throws RemoteException {
        try {
            RunableThread();
        } catch (Exception ex) {
        }
    }

    private void releaseKeys(Robot robot) {
        robot.keyRelease(17);
        robot.keyRelease(18);
        robot.keyRelease(127);
        robot.keyRelease(524);
        robot.keyRelease(9);
    }

    public void RunableThread() throws Exception {
        loginThread = new Thread(new Runnable() {
            @Override
            public void run() {
                logInView = new LogInView();
                logInView.setVisible(true);
                try {
                    releaseKeys(new Robot());
                } catch (AWTException ex) {
                }
                while(!login) {
                    login = logInView.login;
                    System.out.print("");
                }
                if (login) {
                    logInView.dispose();
                    devices.setStatus("ONLINE");
                    try {
                        lookupServices.iserverservices.setStatus(devices);
                    } catch (RemoteException ex) {
                    }
                }
            }
        });
        loginThread.start();
        
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean check = false;
                try {
                    while (!check) {
                        Thread.sleep(1000);
                        check = checkStatus();

                    }
                } catch (Exception ex) {
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    try {
                        Processes kill1 = new Processes("taskmgr");
                        Processes kill2 = new Processes("explorer");
                        boolean login = true;

                        while (login) {
                            System.out.println(loginThread.isAlive()+ " Thread");
                            if (!loginThread.isAlive()) {
                                login = false;
                            }
                            killProcess(kill1);
                            killProcess(kill2);
                            Thread.sleep(1000);
                            System.out.println("Killing...");
                        }
                        Runtime.getRuntime().exec("explorer.exe");
                    } catch (IOException ex) {
                    }
                } catch (InterruptedException ex) {
                }
            }
        }).start();
    }

    public boolean checkStatus() throws Exception {
        System.out.println("Start checking server....");
        lookupServices = new LookupServices("localhost", 2021);
        boolean check = false;
        try {
            if (lookupServices.connect()) {
                logInView.logInBt.setEnabled(true);
                devices = getDevice();
                devices.setIp("localhost");
                devices.setStatus("AVAILABLE");
                lookupServices.setStatus(devices);
                check = true;
            } else {
                logInView.logInBt.setEnabled(false);
            }
        } catch (Exception ex) {
        }
        System.out.println("Checking server successfully!!!");
        return check;
    }

    @Override
    public boolean killProcess(Processes process) {
        try {
            Runtime.getRuntime().exec("taskkill /F /IM " + process.getProcessName() + ".exe");
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public List<Processes> update() {
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

    @Override
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

    @Override
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
    public Devices getDevice() throws RemoteException {
        String file = new File("..\\T-Group-Client\\src\\controller\\GetDevice.vbs").getAbsolutePath();
        String[] propNames = new String[]{"Name", "UUID"};
        Devices device = null;
        try {
            Process p = Runtime.getRuntime().exec("cscript //NoLogo " + file);
            BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));

            Map<String, String> map = new HashMap<String, String>();
            String line;
            int i = 0;
            while ((line = input.readLine()) != null) {
                if (i >= propNames.length) {
                    break;
                }
                String key = propNames[i];
                map.put(key, line);
                i++;
            }
            input.close();
            //
            device = new Devices(map.get(propNames[0]), map.get(propNames[1]));
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return device;
    }
}
