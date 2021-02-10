/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.blacklistvalidator;

import edu.eci.arsw.spamkeywordsdatasource.HostBlacklistsDataSourceFacade;
import edu.eci.arsw.threads.BlackListThread;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hcadavid
 */
public class HostBlackListsValidator {

    private static final int BLACK_LIST_ALARM_COUNT=5;
    private ArrayList<BlackListThread> hilos = new ArrayList<>();
    private HostBlacklistsDataSourceFacade skds=HostBlacklistsDataSourceFacade.getInstance();
    private Monitor lock;


    
    /**
     * Check the given host's IP address in all the available black lists,
     * and report it as NOT Trustworthy when such IP was reported in at least
     * BLACK_LIST_ALARM_COUNT lists, or as Trustworthy in any other case.
     * The search is not exhaustive: When the number of occurrences is equal to
     * BLACK_LIST_ALARM_COUNT, the search is finished, the host reported as
     * NOT Trustworthy, and the list of the five blacklists returned.
     * @param ipaddress suspicious host's IP address.
     * @param numofThreads number of threads to create
     * @return  Blacklists numbers where the given host's IP address was found.
     * 
     */
    public List<Integer> checkHost(String ipaddress, int numofThreads){
        
        LinkedList<Integer> blackListOcurrences=new LinkedList<>();
        
        
        int ocurrencesCount=0;
        
        int checkedListsCount=0;
        createThreads(ipaddress,numofThreads);
        
        for (BlackListThread h : hilos){
            h.start();
            
        }
        
        for(BlackListThread h : hilos){
            try{
            h.join();
            ocurrencesCount+=h.getOcurrencesCount();
            checkedListsCount+=h.getCheckedListsCount();
            blackListOcurrences.add(h.getOcurrencesCount());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        checkedListsCount--;
         
        /*
        for (int i=0;i<skds.getRegisteredServersCount() && ocurrencesCount<BLACK_LIST_ALARM_COUNT;i++){
            
            checkedListsCount++;
            
            if (skds.isInBlackListServer(i, ipaddress)){
                
                blackListOcurrences.add(i);
                
                ocurrencesCount++;
            }
        }
        */
        
        
        if (ocurrencesCount>=BLACK_LIST_ALARM_COUNT){
            skds.reportAsNotTrustworthy(ipaddress);
        }
        else{
            skds.reportAsTrustworthy(ipaddress);
        }                
        
        
        LOG.log(Level.INFO, "Checked Black Lists:{0} of {1}", new Object[]{checkedListsCount, skds.getRegisteredServersCount()});
        
        return blackListOcurrences;
    }
    
    
    private static final Logger LOG = Logger.getLogger(HostBlackListsValidator.class.getName());
    
    
    private void createThreads(String ipaddress,int numofThreads){
        
        
        int numThreadforServer=skds.getRegisteredServersCount()/numofThreads;
        int numServers = skds.getRegisteredServersCount();
        lock= new Monitor();
                
        for (int i=0;i<skds.getRegisteredServersCount();i+=(numThreadforServer)){
            BlackListThread hilo;
            if ((i+(numThreadforServer*2))>numServers){
                //System.out.println(i+1+" "+numServers);
                hilo = new BlackListThread(i+1,numServers,skds,ipaddress,lock);
                hilos.add(hilo);
                break;
              
            }
            else if(i==0){
                //System.out.println(i+" "+(i+numThreadforServer));
                hilo = new BlackListThread(i,i+numThreadforServer,skds,ipaddress,lock);
                hilos.add(hilo);
            
            }
            else{
                //System.out.println(i+1+" "+(i+numThreadforServer));
                hilo = new BlackListThread(i+1,i+numThreadforServer,skds,ipaddress,lock);
                hilos.add(hilo);
            }
            
            
        }
       
    }

}
