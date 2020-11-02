package controller;

import model.process;
import view.LogInView;
import java.awt.AWTException;
import java.awt.Robot;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author dream
 */
public class TGroupClient {

    /**
     * @param args the command line arguments
     */
    public TGroupClient() {
        List<process> list = getTaskList();
        try {
            releaseKeys(new Robot());
        } catch (AWTException ex) {
            Logger.getLogger(TGroupClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    private static void killTask(String processName) {
        try {
            Runtime.getRuntime().exec("taskkill /F /IM " + processName + ".exe");
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

    }

    private int formatMem(String processMem) {
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

    private String[] formatString(String[] str) {
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

    private List<process> getTaskList() {
        List<String> list = new ArrayList<>();
        try {
            String line;
            Process p = Runtime.getRuntime().exec(System.getenv("windir") + "\\system32\\" + "tasklist.exe");
            BufferedReader input
                    = new BufferedReader(new InputStreamReader(p.getInputStream()));
            while ((line = input.readLine()) != null) {
//                if (line.contains("Console"))
                list.add(line); //<-- Parse data here.
            }
            input.close();

        } catch (Exception e) {
        }
        return formatTaskList(list);

    }

    private void releaseKeys(Robot robot) {
        robot.keyRelease(17);
        robot.keyRelease(18);
        robot.keyRelease(127);
        robot.keyRelease(524);
        robot.keyRelease(9);
    }

    private List<process> formatTaskList(List<String> list) {
        List<process> lp = new ArrayList<>();

        try {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).contains(".exe") && list.get(i).contains("Console")) {
                    String[] str = formatString(list.get(i).split("\\s++|\\."));

                    boolean check = false;
                    for (process j : lp) {
                        if (str[0].equals(j.getProcessName())) {
                            j.addProcess(formatMem(str[1]));
                            check = true;
                            break;
                        }
                    }
                    if (check == false) {
                        lp.add(new process(str[0], 1, formatMem(str[1])));
                    }
                }
            }
            Collections.sort(lp, Comparator.comparing(process::getProcessMem, (s1, s2) -> {
                return s2.compareTo(s1);
            }));
        } catch (Exception e) {
        }
        return lp;

    }

    public static void main(String[] args) {
        new TGroupClient();

        new LogInView().setVisible(true);

         
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {

                try {
                    try {
                        int count = 0;
                    while (count < 30000) {
                        killTask("taskmgr");
                        killTask("explorer");
                        count+=1000;
                        Thread.sleep(1000);
                    }
                    
                        Runtime.getRuntime().exec("explorer.exe");
                    } catch (IOException ex) {
                        Logger.getLogger(TGroupClient.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    System.exit(0);
                } catch (InterruptedException ex) {
                    Logger.getLogger(TGroupClient.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

    }

}
