/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.threads;
import edu.eci.arsw.blacklistvalidator.Monitor;
import edu.eci.arsw.spamkeywordsdatasource.HostBlacklistsDataSourceFacade;
import java.util.LinkedList;
/**
 *
 * @author David Coronado
 */
public class BlackListThread extends Thread{
    
    LinkedList<Integer> blackListOcurrences=new LinkedList<>();
    private final int minValue;
    private final int maxValue;
    private final HostBlacklistsDataSourceFacade skds;
    private int checkedListsCount;
    private final String ipaddress;
    private Monitor monitor;
    private int ocurrencesCount;
    
    public BlackListThread(int minValue,int maxValue,HostBlacklistsDataSourceFacade skds,String ipaddress,Monitor monitor){
        this.minValue=minValue;
        this.maxValue=maxValue;
        this.skds=skds;
        this.checkedListsCount=0;
        this.ipaddress=ipaddress;
        this.monitor=monitor;
        this.ocurrencesCount=0;
        
    }
    
    @Override
    public void run(){
        for (int i=minValue;i<=maxValue;i++){
            if (monitor.validate()){
                break;
            
            }
            checkedListsCount++;
            if (skds.isInBlackListServer(i, ipaddress)){                  
                if(monitor.incrementCurrent()){
                    ocurrencesCount++;
                    blackListOcurrences.add(i);
                }        
                else{
                    break;
                    
                }
            }


        }
        
    }

    public int getOcurrencesCount() {
        return ocurrencesCount;
    }

    public int getCheckedListsCount() {
        //System.out.println(checkedListsCount);
        return checkedListsCount;
    }

    
    
    
}
