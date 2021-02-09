/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arst.concprg.prodcons;

import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hcadavid
 */
public class Consumer extends Thread{
    
    private Queue<Integer> queue;
    private Object lock;
    private Object lock2;
    
    public Consumer(Queue<Integer> queue ,Object lock , Object lock2){
        this.queue=queue;
        this.lock=lock;
        this.lock2=lock2;
    }
    
    @Override
    public void run() {
        while (true) {

            if (queue.size() > 0) {
                int elem=queue.poll();
                System.out.println("Consumer consumes "+elem);      
                synchronized(lock2){
                    lock2.notifyAll();
                }
            
            }
            else{
               synchronized(lock){
                   try {
                       lock.wait();
                   } catch (InterruptedException ex) {
                       Logger.getLogger(Consumer.class.getName()).log(Level.SEVERE, null, ex);
                   }
               }
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Producer.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
    }
}
