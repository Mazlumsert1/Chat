package clientHolder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.Server;

public class ClientHolder extends Thread {

    Socket s;
    String username;
    static PrintWriter out;
    static BufferedReader bf;
    static String input;
    static Server server;

    public ClientHolder(String username, Socket socket, Server server) {
        this.server = server;
        this.s = socket;
        this.username = username;
    }

    public Socket getS() {
        return s;
    }

    public String getUsername() {
        return username;
    }

    public void sendMsg(String msg) throws IOException {
        out = new PrintWriter(s.getOutputStream(), true);
        out.println(msg);
    }

    @Override
    public void run() {
        try {
            bf = new BufferedReader(new InputStreamReader(s.getInputStream()));
        } catch (IOException ex) {
            Logger.getLogger(ClientHolder.class.getName()).log(Level.SEVERE, null, ex);
        }

        while (true) {
            try {
                input = bf.readLine();
                if (input.equals("STOP#")) {
                    removeClient(); 
                
                }
//                } else if (input.equals("USERLIST#")){
//                        server.printClientList();
//                } 
                else{
                    server.commandAccept(input, username);
                }
            } catch (IOException ex) {
                Logger.getLogger(ClientHolder.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void removeClient() {
        try {
            server.removeClient(this);
            s.close();
            bf.close();
            out.close();
           
        } catch (IOException ex) {
            Logger.getLogger(ClientHolder.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void checkInput(String input) {
        List<String> splitted = Arrays.asList(input.split("#"));
        String command = splitted.get(0);
        String message = splitted.get(2);

        switch (command) {
            case "USERLIST":
                Server.printClientList();
                break;
            case "MSG": {
                try {
                    Server.distributingSendMethods(message, username);
                } catch (IOException ex) {
                    Logger.getLogger(ClientHolder.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            break;
        }
    }

}
