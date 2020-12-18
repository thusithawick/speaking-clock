/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package speak;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Thusitha
 */
public class Speeker {

    private static SpeechUtils su;
    private static LinkedBlockingDeque<String> q;
    private static Thread t;
    public static boolean sounds = true;
    
    public static synchronized void start(){
        su = new SpeechUtils();
        q = new LinkedBlockingDeque<>();
        q.push("E tack Clock");
        t = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (sounds && q.size()>0) {
                        su.speek(q.pop());
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Speeker.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
        t.start();
    }
    
    public static synchronized void speek(String text) {
        q.clear();
        q.push(text);
    }
    

}
