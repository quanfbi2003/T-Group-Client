/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author dream
 */

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author dream
 */
public class process implements Serializable{
    public static final long serialVersionUID = 2020;
    private String processName;
    private int processNum;
    private int processMem;

    public process() {
    }

    public process(String processName, int processNum, int processMem) {
        this.processName = processName;
        this.processNum = processNum;
        this.processMem = processMem;
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public int getProcessNum() {
        return processNum;
    }

    public void setProcessNum(int processNum) {
        this.processNum = processNum;
    }

    public int getProcessMem() {
        return processMem;
    }

    public void setProcessMem(int processMem) {
        this.processMem = processMem;
    }
    
    public void addProcess(int processMem) {
        this.processMem+=processMem;
        this.processNum++;
    }
    
    public Object toObject() {
        return new Object[] {processName,processNum,String.format("%.1f",processMem/1024.0)+" MB"};
    }
    
    @Override
    public String toString() {
        return processName+" "+processNum +" " +String.format("%.1f",processMem/1024.0)+" MB";
    }
}

