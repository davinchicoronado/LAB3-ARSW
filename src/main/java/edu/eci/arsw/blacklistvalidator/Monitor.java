/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.blacklistvalidator;

/**
 *
 * @author David Coronado
 */
public class Monitor {
    
    private int ocurrencesCount;
    private static final int BLACK_LIST_ALARM_COUNT=5;
    
    public Monitor(){
        ocurrencesCount=0;
        
    
    }
    
    public synchronized boolean incrementCurrent(){
        boolean value=false;
        if(ocurrencesCount<BLACK_LIST_ALARM_COUNT){
            ocurrencesCount++;
            value=true;
        }
        return value;
    
    }
    public synchronized boolean validate(){
        boolean value=false;
        if(ocurrencesCount==5){
            value=true;
        }
        return value;
    }

    public int getOcurrencesCount() {
        return ocurrencesCount;
    }
    
    
    
}
