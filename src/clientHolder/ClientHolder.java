package clientHolder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author AlexanderNielsen
 */
public class ClientHolder implements Runnable {
    
    Socket s;
    String username;
    static PrintWriter out;
    static BufferedReader bf;
    static String input; 
    
    public ClientHolder(String username, Socket socket) {
        this.s = socket;
        this.username = username;
    }
    
    public void sendMsg(String msg) throws IOException {
        out = new PrintWriter(s.getOutputStream(),true);
        out.println(msg);
    }

    @Override
    public void run() {
        try {
            bf = new BufferedReader (new InputStreamReader(s.getInputStream()));
        } catch (IOException ex) {
            Logger.getLogger(ClientHolder.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        while(true){
            try {
                input = bf.readLine();
            } catch (IOException ex) {
                Logger.getLogger(ClientHolder.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }

}
