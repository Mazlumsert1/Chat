package client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Observable;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class client extends Observable implements Runnable {

    String ip;
    int port;
    Socket socket;
    Scanner br;
    PrintWriter pw;
    boolean run = true;

    public client(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    @Override
    public void run() {
        try {
            socket = new Socket(ip, port);
            br = new Scanner(socket.getInputStream());
            pw = new PrintWriter(socket.getOutputStream(), true);  //Set to true, to get auto flush behaviour
        } catch (IOException ex) {
            Logger.getLogger(client.class.getName()).log(Level.SEVERE, null, ex);
        }
       
        while (run) {
            String msg = br.nextLine();
            System.out.println("Msg i client " + msg);
            setChanged();
            notifyObservers(msg);
            if (msg.equals("STOP#")) {
                logout();
                run = false;
            }
            
        }
    }

    public void send(String msg) {
        pw.println(msg);
        //byg protokol strengen
        //send den til server over pw
    }

    public void login(String username) {
        pw.println("Log in please");
        pw.println(username);
        //send login til serveren
        //user#ulla

    }

    public void logout() {
        try {
            socket.close();
            br.close();
            pw.close();
            //stop# til serveren
        } catch (IOException ex) {
            Logger.getLogger(client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
