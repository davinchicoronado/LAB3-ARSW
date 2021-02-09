/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arst.concprg.prodcons;

import java.util.Queue;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hcadavid
 */
public class Producer extends Thread {

    private Queue<Integer> queue = null;

    private int dataSeed = 0;
    private Random rand=null;
    private final Integer stockLimit;
    private Object lock;
    private Object lock2;

    public Producer(Queue<Integer> queue,Integer stockLimit,Object lock,Object lock2) {
        this.queue = queue;
        rand = new Random(System.currentTimeMillis());
        this.stockLimit=stockLimit;
        this.lock=lock;
        this.lock2=lock2;
    }

    @Override
    public void run() {
        while (true) {
            dataSeed = dataSeed + rand.nextInt(100);
            System.out.println("Producer added " + dataSeed);
            queue.add(dataSeed);
            
       
            if(!queue.isEmpty()){
                synchronized(lock){
                    lock.notifyAll();
                }
            }
            if (queue.size()>=stockLimit){
                synchronized(lock2){
                    try {
                        lock2.wait();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Producer.class.getName()).log(Level.SEVERE, null, ex);
                    }
                
                }
            
            }
            

        }
    }
}
